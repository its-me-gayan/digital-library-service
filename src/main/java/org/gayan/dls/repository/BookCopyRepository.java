package org.gayan.dls.repository;


import jakarta.persistence.LockModeType;
import org.gayan.dls.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/21/25
 * Time: 11:50 PM
 */
@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, UUID> {
    @Query(value = "SELECT * FROM book_copy c " +
            "WHERE c.book_id = :bookId AND c.is_borrowed = false AND c.status = 'AVAILABLE'" +
            "LIMIT 1 FOR UPDATE",
            nativeQuery = true)
    Optional<BookCopy> findFirstAvailableCopyForUpdate(@Param("bookId") UUID bookId);

}
