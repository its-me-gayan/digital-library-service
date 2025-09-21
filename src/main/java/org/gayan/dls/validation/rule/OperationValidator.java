package org.gayan.dls.validation.rule;


import lombok.RequiredArgsConstructor;
import org.gayan.dls.entity.Book;
import org.gayan.dls.exception.BookException;
import org.gayan.dls.repository.BookRepository;
import org.gayan.dls.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/20/25
 * Time: 11:22 PM
 */
@Component
@RequiredArgsConstructor
public class OperationValidator implements Validator<Book> {

    private final BookRepository bookRepository;

    /**
     * Validate ISBN uniqueness and return the book (existing or new).
     */
    public Book validateAndGet(Book candidateBook) {
        Optional<Book> existingBookOpt = bookRepository.findBookByIsbn(candidateBook.getIsbn());

        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();

            if (!existingBook.getTitle().equalsIgnoreCase(candidateBook.getTitle()) ||
                    !existingBook.getAuthor().equalsIgnoreCase(candidateBook.getAuthor())) {
                throw new BookException(
                        "Books with the same ISBN must have the same title and author"
                );
            }
            return existingBook; // return the already existing book
        }

        return null; // return null if none exists
    }
}
