package org.gayan.dls.mapper;

import java.time.LocalDateTime;
import org.gayan.dls.entity.BookCopy;
import org.gayan.dls.entity.Borrower;
import org.gayan.dls.entity.BorrowingHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Author: Gayan Sanjeewa User: gayan Date: 9/22/25 Time: 12:29 AM */
@Mapper(
    componentModel = "spring",
    imports = {LocalDateTime.class})
public interface BorrowingHistoryMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "bookCopy", target = "bookCopy")
  @Mapping(source = "borrower", target = "borrower")
  @Mapping(target = "borrowedAt", expression = "java(LocalDateTime.now())")
  @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
  @Mapping(target = "returnedAt", expression = "java(null)")
  BorrowingHistory buildBorrowingHistory(BookCopy bookCopy, Borrower borrower);
}
