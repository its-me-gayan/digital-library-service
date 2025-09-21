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
import org.gayan.dls.exception.BookException;
import org.gayan.dls.exception.BorrowerException;
import org.gayan.dls.exception.LibraryOperationException;
import org.gayan.dls.mapper.BookMapper;
import org.gayan.dls.mapper.BorrowingHistoryMapper;
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

import java.util.UUID;

/**
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

    @Transactional
    @Override
    public ResponseEntity<ApiResponse<BorrowBookResponseDto>> borrowBook(BorrowBookRequestDto borrowBookRequestDto) throws BookException {

        Book book = bookRepository
                .findById(UUID.fromString(borrowBookRequestDto.getBookId())).orElseThrow(() -> new BookException(ExceptionMessage.EXP_MSG_BOOK_NOT_AVAILABLE));
        Borrower borrower = borrowerRepository
                .findById(UUID.fromString(borrowBookRequestDto.getBorrowerId())).orElseThrow(() -> new BorrowerException(ExceptionMessage.EXP_MSG_BORROWER_NOT_AVAILABLE));

        // Step 1: Check if borrower already borrowed this book
        boolean alreadyBorrowed = book.getCopies().stream()
                .anyMatch(copy -> Boolean.TRUE.equals(copy.getIsBorrowed()) && copy.getBookStatus().equals(BookStatus.BORROWED) &&
                        copy.getBorrowedBy() != null &&
                        copy.getBorrowedBy().getId().toString().equals(borrowBookRequestDto.getBorrowerId()));
        if(alreadyBorrowed){
            throw new LibraryOperationException(ExceptionMessage.EXP_MSG_BORROW_FAILED, HttpStatus.CONFLICT);
        }

        //manually getting the book copy with the help of native query using FOR UPDATE to lock the record until transaction ends
        BookCopy availableBookCopyToBorrow =  bookCopyRepository.findFirstAvailableCopyForUpdate(book.getId()).orElseThrow(() -> new BookException(ExceptionMessage.EXP_MSG_NO_COPIES_AVAILABLE));

        bookMapper.updateBookCopyForBorrowing(availableBookCopyToBorrow, borrower);

        bookCopyRepository.save(availableBookCopyToBorrow);
        borrowerHistoryRepository.save(borrowingHistoryMapper.buildBorrowingHistory(availableBookCopyToBorrow , borrower));

        return responseBuilder.success(borrowingHistoryMapper.buildResponseDto(book, borrower, availableBookCopyToBorrow) , "Book Borrowed successfully !");

    }

    @Override
    public ResponseEntity<ApiResponse<ReturnBookResponseDto>> returnBook(ReturnBookRequestDto returnBookRequestDto) throws BookException {
        Book book = bookRepository
                .findById(UUID.fromString(returnBookRequestDto.getBookId())).orElseThrow(() -> new BookException(ExceptionMessage.EXP_MSG_BOOK_NOT_AVAILABLE));
        Borrower borrower = borrowerRepository
                .findById(UUID.fromString(returnBookRequestDto.getBorrowerId())).orElseThrow(() -> new BorrowerException(ExceptionMessage.EXP_MSG_BORROWER_NOT_AVAILABLE));


        return null;
    }
}
