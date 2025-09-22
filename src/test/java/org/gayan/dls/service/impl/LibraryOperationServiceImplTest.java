package org.gayan.dls.service.impl;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/22/25
 * Time: 11:43 PM
 */

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.gayan.dls.constant.BookStatus;
import org.gayan.dls.constant.ExceptionMessage;
import org.gayan.dls.dto.BorrowBookRequestDto;
import org.gayan.dls.dto.BorrowBookResponseDto;
import org.gayan.dls.dto.ReturnBookRequestDto;
import org.gayan.dls.dto.ReturnBookResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.entity.*;
import org.gayan.dls.exception.BookException;
import org.gayan.dls.exception.BorrowerException;
import org.gayan.dls.exception.LibraryOperationException;
import org.gayan.dls.mapper.BookMapper;
import org.gayan.dls.mapper.BorrowingHistoryMapper;
import org.gayan.dls.mapper.LibraryOperationMapper;
import org.gayan.dls.repository.*;
import org.gayan.dls.util.DlsResponseBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class LibraryOperationServiceImplTest {

    @Mock private BookRepository bookRepository;
    @Mock private BorrowerRepository borrowerRepository;
    @Mock private BorrowerHistoryRepository borrowerHistoryRepository;
    @Mock private BookCopyRepository bookCopyRepository;
    @Mock private DlsResponseBuilder responseBuilder;
    @Mock private BookMapper bookMapper;
    @Mock private BorrowingHistoryMapper borrowingHistoryMapper;
    @Mock private LibraryOperationMapper libraryOperationMapper;

    @InjectMocks private LibraryOperationServiceImpl libraryOperationService;

    private UUID bookId;
    private UUID borrowerId;
    private Book book;
    private Borrower borrower;
    private BookCopy bookCopy;
    private BorrowingHistory borrowingHistory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookId = UUID.randomUUID();
        borrowerId = UUID.randomUUID();
        borrower = Borrower.builder().id(borrowerId).name("John Doe").build();
        book = Book.builder().id(bookId).title("Test Book").build();
        bookCopy = BookCopy.builder()
                .id(UUID.randomUUID())
                .book(book)
                .isBorrowed(false)
                .bookStatus(BookStatus.AVAILABLE)
                .build();
        borrowingHistory = BorrowingHistory.builder()
                .id(UUID.randomUUID())
                .bookCopy(bookCopy)
                .borrower(borrower)
                .borrowedAt(LocalDateTime.now())
                .build();
    }

    // -----------------------------
    // borrowBook Tests
    // -----------------------------
    @Test
    void borrowBook_Success() throws Exception {
        BorrowBookRequestDto request = new BorrowBookRequestDto(bookId.toString(), borrowerId.toString());
        book.setCopies(List.of());
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));
        when(bookCopyRepository.findFirstAvailableCopyForUpdate(bookId)).thenReturn(Optional.of(bookCopy));

        BorrowingHistory mockHistory = new BorrowingHistory();
        when(borrowingHistoryMapper.buildBorrowingHistory(any(), any())).thenReturn(mockHistory);

        BorrowBookResponseDto responseDto = new BorrowBookResponseDto();
        when(libraryOperationMapper.buildBorrowResponseDto(any(), any(), any())).thenReturn(responseDto);

        ApiResponse<BorrowBookResponseDto> apiResponse = ApiResponse.<BorrowBookResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .responseCode("200")
                .message("success")
                .timestamp(Instant.now())
                .data(responseDto)
                .build();

        when(responseBuilder.success(any(BorrowBookResponseDto.class), anyString()))
                .thenReturn(ResponseEntity.ok(apiResponse));

        ResponseEntity<ApiResponse<BorrowBookResponseDto>> response = libraryOperationService.borrowBook(request);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals("success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());

        verify(bookCopyRepository).save(any(BookCopy.class));
        verify(borrowerHistoryRepository).save(any(BorrowingHistory.class));
    }


    @Test
    void borrowBook_BookNotFound_ThrowsException() {
        BorrowBookRequestDto request = new BorrowBookRequestDto(bookId.toString(), borrowerId.toString());
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookException.class, () -> libraryOperationService.borrowBook(request));
    }

    @Test
    void borrowBook_BorrowerNotFound_ThrowsException() {
        BorrowBookRequestDto request = new BorrowBookRequestDto(bookId.toString(), borrowerId.toString());
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.empty());

        assertThrows(BorrowerException.class, () -> libraryOperationService.borrowBook(request));
    }

    @Test
    void borrowBook_NoCopiesAvailable_ThrowsException() {
        book.setCopies(List.of());
        BorrowBookRequestDto request = new BorrowBookRequestDto(bookId.toString(), borrowerId.toString());
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));
        when(bookCopyRepository.findFirstAvailableCopyForUpdate(bookId)).thenReturn(Optional.empty());

        assertThrows(BookException.class, () -> libraryOperationService.borrowBook(request));
    }

    // -----------------------------
    // returnBook Tests
    // -----------------------------
    @Test
    void returnBook_Success() throws Exception {
        ReturnBookRequestDto request = new ReturnBookRequestDto(bookId.toString(), borrowerId.toString());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));
        when(borrowerHistoryRepository.findBorrowingHistoryByBorrowerAndReturnedAtIsNull(borrower))
                .thenReturn(List.of(borrowingHistory));

        ReturnBookResponseDto responseDto = new ReturnBookResponseDto();
        when(libraryOperationMapper.buildReturnResponseDto(any(), any(), any(), any())).thenReturn(responseDto);

        ApiResponse<ReturnBookResponseDto> apiResponse = ApiResponse.<ReturnBookResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .responseCode("200")
                .message("success")
                .timestamp(Instant.now())
                .data(responseDto)
                .build();

        when(responseBuilder.success(any(ReturnBookResponseDto.class), anyString()))
                .thenReturn(ResponseEntity.ok(apiResponse));

        ResponseEntity<ApiResponse<ReturnBookResponseDto>> response = libraryOperationService.returnBook(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getMessage());
        verify(borrowerHistoryRepository).save(any(BorrowingHistory.class));
    }

    @Test
    void returnBook_NoBorrowingHistory_ThrowsException() {
        ReturnBookRequestDto request = new ReturnBookRequestDto(bookId.toString(), borrowerId.toString());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));
        when(borrowerHistoryRepository.findBorrowingHistoryByBorrowerAndReturnedAtIsNull(borrower))
                .thenReturn(List.of());

        assertThrows(LibraryOperationException.class, () -> libraryOperationService.returnBook(request));
    }

    @Test
    void returnBook_TryingToReturnDifferentBook_ThrowsException() {
        ReturnBookRequestDto request = new ReturnBookRequestDto(bookId.toString(), borrowerId.toString());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));
        BorrowingHistory differentBookHistory = BorrowingHistory.builder()
                .bookCopy(BookCopy.builder().book(Book.builder().id(UUID.randomUUID()).build()).build())
                .borrower(borrower)
                .build();
        when(borrowerHistoryRepository.findBorrowingHistoryByBorrowerAndReturnedAtIsNull(borrower))
                .thenReturn(List.of(differentBookHistory));

        assertThrows(LibraryOperationException.class, () -> libraryOperationService.returnBook(request));
    }
}