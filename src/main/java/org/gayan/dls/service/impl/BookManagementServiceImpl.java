package org.gayan.dls.service.impl;

import static org.gayan.dls.constant.ResponseMessage.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gayan.dls.dto.BookRequestDto;
import org.gayan.dls.dto.BookResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.entity.Book;
import org.gayan.dls.exception.BookException;
import org.gayan.dls.exception.NoContentFoundException;
import org.gayan.dls.mapper.BookMapper;
import org.gayan.dls.repository.BookRepository;
import org.gayan.dls.service.BookManagementService;
import org.gayan.dls.util.DlsResponseBuilder;
import org.gayan.dls.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Author: Gayan Sanjeewa User: gayan Date: 9/20/25 Time: 12:29 AM */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookManagementServiceImpl implements BookManagementService {

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;
  private final Validator<Book> bookValidator;
  private final DlsResponseBuilder responseBuilder;

  @Transactional
  @Override
  public ResponseEntity<ApiResponse<BookResponseDto>> persistBook(BookRequestDto bookRequestDto)
      throws BookException {
    log.info("Started creating a book");
    Book candidateBook = bookMapper.mapBookRequestDtoToBookEntity(bookRequestDto);
    Book validatedBook = bookValidator.validateAndGet(candidateBook);
    Book createdOrUpdateBook;
    String responseMessage;
    if (Objects.nonNull(validatedBook)) {
      log.info("Same ISBN with same title and author - creating one more copy in the system");
      validatedBook.getCopies().add(bookMapper.buildBookCopy(validatedBook));
      createdOrUpdateBook = bookRepository.save(validatedBook);

      log.info(
          "Book updated with one more copy :: {}, total copy :: {}",
          createdOrUpdateBook.getId(),
          validatedBook.getCopies().size());
      responseMessage = RPM_BOOK_UPDATED_WITH_COPIES_SUCCESS;
    } else {
      candidateBook.setCopies(List.of(bookMapper.buildBookCopy(candidateBook)));
      createdOrUpdateBook = bookRepository.save(candidateBook);
      log.info("Book created successfully with id :: {}", createdOrUpdateBook.getId());
      responseMessage = RPM_BOOK_CREATED_SUCCESS;
    }
    return responseBuilder.success(
        bookMapper.mapBookEntityToBookResponseDto(createdOrUpdateBook), responseMessage);
  }

  @Override
  public ResponseEntity<ApiResponse<BookResponseDto>> getBookById(String bookId)
      throws BookException {
    Book book =
        bookRepository.findById(UUID.fromString(bookId)).orElseThrow(NoContentFoundException::new);
    return responseBuilder.success(
        bookMapper.mapBookEntityToBookResponseDtoWithCopies(book), RPM_BOOK_FOUND);
  }

  @Override
  public ResponseEntity<ApiResponse<Page<BookResponseDto>>> getAllBooksWithPagination(
      Pageable pageable) throws BookException {
    Page<Book> booksPage = bookRepository.findAll(pageable);
    if (booksPage.isEmpty()) {
      throw new NoContentFoundException();
    }
    Page<BookResponseDto> responsePage = booksPage.map(bookMapper::mapBookEntityToBookResponseDto);
    return responseBuilder.success(responsePage, RPM_BOOK_FOUND);
  }
}
