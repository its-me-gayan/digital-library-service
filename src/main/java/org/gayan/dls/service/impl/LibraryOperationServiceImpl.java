package org.gayan.dls.service.impl;


import lombok.RequiredArgsConstructor;
import org.gayan.dls.constant.BookStatus;
import org.gayan.dls.constant.ExceptionMessage;
import org.gayan.dls.dto.BorrowBookRequestDto;
import org.gayan.dls.dto.BorrowBookResponseDto;
import org.gayan.dls.dto.ReturnBookRequestDto;
import org.gayan.dls.dto.ReturnBookResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.entity.Book;
import org.gayan.dls.entity.BookCopy;
import org.gayan.dls.entity.Borrower;
import org.gayan.dls.entity.BorrowingHistory;
import org.gayan.dls.exception.BookException;
import org.gayan.dls.exception.BorrowerException;
import org.gayan.dls.exception.LibraryOperationException;
import org.gayan.dls.mapper.BookMapper;
import org.gayan.dls.mapper.BorrowingHistoryMapper;
import org.gayan.dls.mapper.LibraryOperationMapper;
import org.gayan.dls.repository.BookCopyRepository;
import org.gayan.dls.repository.BookRepository;
import org.gayan.dls.repository.BorrowerHistoryRepository;
import org.gayan.dls.repository.BorrowerRepository;
import org.gayan.dls.service.LibraryOperationService;
import org.gayan.dls.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.gayan.dls.constant.ExceptionMessage.EXP_MSG_NOTHING_TO_RETURN;
import static org.gayan.dls.constant.ExceptionMessage.EXP_MSG_TRY_RETURN_DIFFERENT_BOOK;
import static org.gayan.dls.constant.ResponseMessage.RPM_BOOK_BORROWED_SUCCESS;
import static org.gayan.dls.constant.ResponseMessage.RPM_BOOK_RETURNED_SUCCESS;

/**
 * Service implementation that handles core library operations such as
 * borrowing and returning books.
 * This class enforces transactional consistency, prevents double-borrowing,
 * manages book copy availability, and updates borrowing history.
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/21/25
 * Time: 11:23 PM
 */
@Service
@RequiredArgsConstructor
public class LibraryOperationServiceImpl implements LibraryOperationService {

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private final BorrowerHistoryRepository borrowerHistoryRepository;
    private final ResponseBuilder responseBuilder;
    private final BookCopyRepository bookCopyRepository;
    private final BookMapper bookMapper;
    private final BorrowingHistoryMapper borrowingHistoryMapper;
    private final LibraryOperationMapper libraryOperationMapper;

    /**
     * Allows a borrower to borrow an available copy of a given book.
     *
     * Flow:
     * 1. Validate that both book and borrower exist.
     * 2. Ensure the borrower has not already borrowed the same book.
     * 3. Acquire the first available book copy using a pessimistic lock (FOR UPDATE).
     * 4. Update the copy’s status and borrower reference.
     * 5. Persist borrowing history for traceability.
     * 6. Return a success response with borrowing details.
     *
     * @param borrowBookRequestDto request containing bookId and borrowerId
     * @return ApiResponse with borrow details
     * @throws BookException if no book or copies are available
     */
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<BorrowBookResponseDto>> borrowBook(BorrowBookRequestDto borrowBookRequestDto) throws BookException {

        Book book = bookRepository
                .findById(UUID.fromString(borrowBookRequestDto.bookId())).orElseThrow(() -> new BookException(ExceptionMessage.EXP_MSG_BOOK_NOT_AVAILABLE));
        Borrower borrower = borrowerRepository
                .findById(UUID.fromString(borrowBookRequestDto.borrowerId())).orElseThrow(() -> new BorrowerException(ExceptionMessage.EXP_MSG_BORROWER_NOT_AVAILABLE));

        // Step 1: Check if borrower already borrowed this book
        boolean alreadyBorrowed = book.getCopies().stream()
                .anyMatch(copy -> Boolean.TRUE.equals(copy.getIsBorrowed()) && copy.getBookStatus().equals(BookStatus.BORROWED) &&
                        copy.getBorrowedBy() != null &&
                        copy.getBorrowedBy().getId().toString().equals(borrowBookRequestDto.borrowerId()));
        if(alreadyBorrowed){
            throw new LibraryOperationException(ExceptionMessage.EXP_MSG_BORROW_FAILED, HttpStatus.CONFLICT);
        }

        // Manually fetching the book copy using a native query with FOR UPDATE to lock the record until the transaction completes
        BookCopy availableBookCopyToBorrow =  bookCopyRepository.findFirstAvailableCopyForUpdate(book.getId()).orElseThrow(() -> new BookException(ExceptionMessage.EXP_MSG_NO_COPIES_AVAILABLE));

        bookMapper.updateBookCopyForBorrowing(availableBookCopyToBorrow, borrower,BookStatus.BORROWED);

        bookCopyRepository.save(availableBookCopyToBorrow);
        borrowerHistoryRepository.save(borrowingHistoryMapper.buildBorrowingHistory(availableBookCopyToBorrow , borrower));

        return responseBuilder.success(libraryOperationMapper.buildBorrowResponseDto(book, borrower, availableBookCopyToBorrow) , RPM_BOOK_BORROWED_SUCCESS);

    }

    /**
     * Allows a borrower to return a previously borrowed book.
     *
     * Flow:
     * 1. Validate that both book and borrower exist.
     * 2. Check if borrower has any active (non-returned) borrowing history.
     * 3. Ensure the borrowed book matches the requested book to return.
     * 4. Mark the borrowing history as returned and update the book copy’s status.
     * 5. Return a success response with return details.
     *
     * @param returnBookRequestDto request containing bookId and borrowerId
     * @return ApiResponse with return details
     * @throws BookException if book/borrower not found or invalid return
     */
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<ReturnBookResponseDto>> returnBook(ReturnBookRequestDto returnBookRequestDto) throws BookException {
        Book book = bookRepository
                .findById(UUID.fromString(returnBookRequestDto.bookId())).orElseThrow(() -> new BookException(ExceptionMessage.EXP_MSG_BOOK_NOT_AVAILABLE));
        Borrower borrower = borrowerRepository
                .findById(UUID.fromString(returnBookRequestDto.borrowerId())).orElseThrow(() -> new BorrowerException(ExceptionMessage.EXP_MSG_BORROWER_NOT_AVAILABLE));

        List<BorrowingHistory> borrowingHistory = borrowerHistoryRepository.findBorrowingHistoryByBorrowerAndReturnedAtIsNull(borrower);

        if(borrowingHistory.isEmpty()){
           throw  new LibraryOperationException(EXP_MSG_NOTHING_TO_RETURN);
        }
        BorrowingHistory bHistory = borrowingHistory
                .stream()
                .filter(bh -> bh.getBookCopy().getBook().getId().toString().equals(book.getId().toString()))
                .findFirst().orElseThrow(() -> new LibraryOperationException(EXP_MSG_TRY_RETURN_DIFFERENT_BOOK));
        bHistory.setReturnedAt(LocalDateTime.now());
        BookCopy bookCopy = bHistory.getBookCopy();
        bookMapper.updateBookCopyForReturn(bookCopy , BookStatus.AVAILABLE);
        borrowerHistoryRepository.save(bHistory);
        return responseBuilder.success(libraryOperationMapper.buildReturnResponseDto(bookCopy.getBook() , borrower , bookCopy , bHistory) , RPM_BOOK_RETURNED_SUCCESS);
    }
}
