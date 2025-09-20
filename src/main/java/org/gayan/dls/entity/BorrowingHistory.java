package org.gayan.dls.entity;


/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 10:23 PM
 */
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "borrowing_history",
        indexes = {
                @Index(name = "idx_borrowing_book_id", columnList = "book_id"),
                @Index(name = "idx_borrowing_borrower_id", columnList = "borrower_id"),
                @Index(name = "idx_borrowing_dates", columnList = "borrowed_at, returned_at")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private Borrower borrower;

    @Column(name = "borrowed_at", nullable = false)
    private LocalDateTime borrowedAt;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Helper methods
    public boolean isActive() {
        return returnedAt == null;
    }

    public void markAsReturned() {
        this.returnedAt = LocalDateTime.now();
    }

    public long getBorrowingDurationInDays() {
        LocalDateTime endDate = returnedAt != null ? returnedAt : LocalDateTime.now();
        return java.time.Duration.between(borrowedAt, endDate).toDays();
    }

    @Override
    public String toString() {
        return "BorrowingHistory{" +
                "id=" + id +
                ", bookId=" + (book != null ? book.getId() : null) +
                ", borrowerId=" + (borrower != null ? borrower.getId() : null) +
                ", borrowedAt=" + borrowedAt +
                ", returnedAt=" + returnedAt +
                ", isActive=" + isActive() +
                '}';
    }
}
