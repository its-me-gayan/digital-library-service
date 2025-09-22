package org.gayan.dls.repository;

import java.util.List;
import java.util.UUID;
import org.gayan.dls.entity.Borrower;
import org.gayan.dls.entity.BorrowingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Author: Gayan Sanjeewa User: gayan Date: 9/19/25 Time: 10:29 PM */
@Repository
public interface BorrowerHistoryRepository extends JpaRepository<BorrowingHistory, UUID> {

  List<BorrowingHistory> findBorrowingHistoryByBorrowerAndReturnedAtIsNull(Borrower borrower);
}
