package org.gayan.dls.entity;


/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 10:22 PM
 */

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "books",
        indexes = {
                @Index(name = "idx_books_isbn", columnList = "isbn"),
                @Index(name = "idx_books_borrowed", columnList = "is_borrowed"),
                @Index(name = "idx_isbn_title_author", columnList = "isbn, title, author", unique = true)
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String isbn;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 255)
    private String author;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BookCopy> copies = new ArrayList<>();


    public boolean isSamePublication(String isbn, String title, String author) {
        return this.isbn.equals(isbn) &&
                this.title.equals(title) &&
                this.author.equals(author);
    }

//    @Override
//    public String toString() {
//        return "Book{" +
//                "id=" + id +
//                ", isbn='" + isbn + '\'' +
//                ", title='" + title + '\'' +
//                ", author='" + author + '\'' +
//                ", createdAt=" + createdAt +
//                '}';
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id != null && id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}