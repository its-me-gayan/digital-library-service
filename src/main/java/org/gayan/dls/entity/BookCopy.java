package org.gayan.dls.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/20/25
 * Time: 10:50 PM
 */
@Builder
@Entity
@Table(name = "book_copy")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    @Column(name = "is_borrowed", nullable = false)
    private Boolean isBorrowed = false;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowed_by")
    private Borrower borrowedBy;

    @Column(name = "borrowed_at")
    private LocalDateTime borrowedAt;

}