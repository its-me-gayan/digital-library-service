package org.gayan.dls.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/** Author: Gayan Sanjeewa User: gayan Date: 9/19/25 Time: 10:19 PM */
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

  @Column(nullable = false, length = 255)
  private String name;

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

  public int getBorrowedBooksCount() {
    return borrowedBooks != null ? borrowedBooks.size() : 0;
  }

  @Override
  public String toString() {
    return "Borrower{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", email='"
        + email
        + '\''
        + ", borrowedBooksCount="
        + getBorrowedBooksCount()
        + ", createdAt="
        + createdAt
        + '}';
  }
}
