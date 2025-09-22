package org.gayan.dls.service.impl;

import org.gayan.dls.dto.BookRequestDto;
import org.gayan.dls.dto.BookResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.entity.Book;
import org.gayan.dls.entity.BookCopy;
import org.gayan.dls.exception.NoContentFoundException;
import org.gayan.dls.mapper.BookMapper;
import org.gayan.dls.repository.BookRepository;
import org.gayan.dls.util.DlsResponseBuilder;
import org.gayan.dls.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.gayan.dls.constant.ResponseMessage.RPM_BOOK_CREATED_SUCCESS;
import static org.gayan.dls.constant.ResponseMessage.RPM_BOOK_UPDATED_WITH_COPIES_SUCCESS;
import static org.gayan.dls.constant.ResponseMessage.RPM_BOOK_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookManagementServiceImpl
 */
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class BookManagementServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private Validator<Book> bookValidator;

    @Mock
    private DlsResponseBuilder responseBuilder;

    @InjectMocks
    private BookManagementServiceImpl bookService;

    private BookRequestDto requestDto;
    private BookResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new BookRequestDto("12345" , "Test Book","Gayan sanjeewa");
        responseDto = new BookResponseDto();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void persistBook_whenValidatorReturnsExistingBook_shouldAddCopyAndReturnUpdatedMessage() throws Exception {
        // Arrange
        Book candidateBook = mock(Book.class);
        Book validatedBook = mock(Book.class);
        BookCopy copyBook = mock(BookCopy.class);
        Book savedBook = mock(Book.class);

        // ensure getCopies() returns a real mutable list (so add() works)
        List<BookCopy> copiesList = new ArrayList<>();
        when(validatedBook.getCopies()).thenReturn(copiesList);

        when(bookMapper.mapBookRequestDtoToBookEntity(requestDto)).thenReturn(candidateBook);
        when(bookValidator.validateAndGet(candidateBook)).thenReturn(validatedBook);
        when(bookMapper.buildBookCopy(validatedBook)).thenReturn(copyBook);

        when(bookRepository.save(validatedBook)).thenReturn(savedBook);
        when(bookMapper.mapBookEntityToBookResponseDto(savedBook)).thenReturn(responseDto);

        // responseBuilder.success(...) - return a ResponseEntity stub
        ResponseEntity<ApiResponse<BookResponseDto>> returned = (ResponseEntity) ResponseEntity.ok().build();
        when(responseBuilder.success(responseDto, RPM_BOOK_UPDATED_WITH_COPIES_SUCCESS)).thenReturn(returned);

        // Act
        ResponseEntity<ApiResponse<BookResponseDto>> result = bookService.persistBook(requestDto);

        // Assert
        assertSame(returned, result);
        // verify a copy was built and added
        verify(bookMapper).buildBookCopy(validatedBook);
        // the copies list should now contain our copy object
        assertEquals(1, copiesList.size());
        assertSame(copyBook, copiesList.get(0));
        verify(bookRepository).save(validatedBook);
        verify(bookMapper).mapBookEntityToBookResponseDto(savedBook);
        verify(responseBuilder).success(responseDto, RPM_BOOK_UPDATED_WITH_COPIES_SUCCESS);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void persistBook_whenValidatorReturnsNull_shouldCreateNewBookAndReturnCreatedMessage() throws Exception {
        // Arrange
        Book candidateBook = mock(Book.class);
        BookCopy copyBook = mock(BookCopy.class);
        Book savedBook = mock(Book.class);

        when(bookMapper.mapBookRequestDtoToBookEntity(requestDto)).thenReturn(candidateBook);
        when(bookValidator.validateAndGet(candidateBook)).thenReturn(null);
        when(bookMapper.buildBookCopy(candidateBook)).thenReturn(copyBook);

        // When candidateBook.setCopies(List.of(copy)) is called - it's a mock: just allow it.
        when(bookRepository.save(candidateBook)).thenReturn(savedBook);
        when(bookMapper.mapBookEntityToBookResponseDto(savedBook)).thenReturn(responseDto);

        ResponseEntity<ApiResponse<BookResponseDto>> returned = (ResponseEntity) ResponseEntity.ok().build();
        when(responseBuilder.success(responseDto, RPM_BOOK_CREATED_SUCCESS)).thenReturn(returned);

        // Act
        ResponseEntity<ApiResponse<BookResponseDto>> result = bookService.persistBook(requestDto);

        // Assert
        assertSame(returned, result);
        verify(bookMapper).buildBookCopy(candidateBook);
        verify(bookRepository).save(candidateBook);
        verify(bookMapper).mapBookEntityToBookResponseDto(savedBook);
        verify(responseBuilder).success(responseDto, RPM_BOOK_CREATED_SUCCESS);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void getBookById_whenFound_shouldReturnDtoWithCopiesMessage() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        String idString = id.toString();
        Book book = mock(Book.class);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.mapBookEntityToBookResponseDtoWithCopies(book)).thenReturn(responseDto);

        ResponseEntity<ApiResponse<BookResponseDto>> returned = (ResponseEntity) ResponseEntity.ok().build();
        when(responseBuilder.success(responseDto, RPM_BOOK_FOUND)).thenReturn(returned);

        // Act
        ResponseEntity<ApiResponse<BookResponseDto>> result = bookService.getBookById(idString);

        // Assert
        assertSame(returned, result);
        verify(bookRepository).findById(id);
        verify(bookMapper).mapBookEntityToBookResponseDtoWithCopies(book);
        verify(responseBuilder).success(responseDto, RPM_BOOK_FOUND);
    }

    @Test
    void getBookById_whenNotFound_shouldThrowNoContentFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String idString = id.toString();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoContentFoundException.class, () -> bookService.getBookById(idString));
        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookMapper, responseBuilder);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void getAllBooksWithPagination_whenPageHasContent_shouldReturnPageDto() throws Exception {
        // Arrange
        Book book1 = mock(Book.class);
        Book book2 = mock(Book.class);
        List<Book> content = List.of(book1, book2);
        Page<Book> page = new PageImpl<>(content);

        Pageable pageable = mock(Pageable.class);
        when(bookRepository.findAll(pageable)).thenReturn(page);

        // mapping each Book to BookResponseDto
        BookResponseDto dto1 = new BookResponseDto();
        when(bookMapper.mapBookEntityToBookResponseDto(book1)).thenReturn(dto1);

        Page<BookResponseDto> expectedPage = page.map(bookMapper::mapBookEntityToBookResponseDto);

        ResponseEntity<ApiResponse<Page<BookResponseDto>>> returned = (ResponseEntity) ResponseEntity.ok().build();
        when(responseBuilder.success(expectedPage, RPM_BOOK_FOUND)).thenReturn(returned);

        // Act
        ResponseEntity<ApiResponse<Page<BookResponseDto>>> result = bookService.getAllBooksWithPagination(pageable);

        // Assert
        assertSame(returned, result);
        verify(bookRepository).findAll(pageable);
        verify(responseBuilder).success(expectedPage, RPM_BOOK_FOUND);
    }

    @Test
    void getAllBooksWithPagination_whenEmpty_shouldThrowNoContentFoundException() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Page<Book> emptyPage = Page.empty();
        when(bookRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act & Assert
        assertThrows(NoContentFoundException.class, () -> bookService.getAllBooksWithPagination(pageable));
        verify(bookRepository).findAll(pageable);
        verifyNoMoreInteractions(bookMapper, responseBuilder);
    }
}
