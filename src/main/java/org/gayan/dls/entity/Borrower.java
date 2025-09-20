package org.gayan.dls.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 10:19 PM
 */

@Entity
@Table(name = "borrowers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    @Column(nullable = false, length = 255)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @OneToMany(mappedBy = "borrowedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<BookCopy> borrowedBooks = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

//    // Helper methods
//    public void addBorrowedBook(Book book) {
//        borrowedBooks.add(book);
//        book.setBorrowedBy(this);
//        book.setIsBorrowed(true);
//        book.setBorrowedAt(LocalDateTime.now());
//    }
//
//    public void removeBorrowedBook(Book book) {
//        borrowedBooks.remove(book);
//        book.setBorrowedBy(null);
//        book.setIsBorrowed(false);
//        book.setBorrowedAt(null);
//    }

    public int getBorrowedBooksCount() {
        return borrowedBooks != null ? borrowedBooks.size() : 0;
    }

    @Override
    public String toString() {
        return "Borrower{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", borrowedBooksCount=" + getBorrowedBooksCount() +
                ", createdAt=" + createdAt +
                '}';
    }
}