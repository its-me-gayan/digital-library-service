package org.gayan.dls.repository;

import java.util.Optional;
import java.util.UUID;
import org.gayan.dls.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Author: Gayan Sanjeewa User: gayan Date: 9/19/25 Time: 10:29 PM */
@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

  Optional<Book> findBookByIsbn(String isbn);
}
