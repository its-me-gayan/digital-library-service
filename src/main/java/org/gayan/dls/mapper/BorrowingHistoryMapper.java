package org.gayan.dls.mapper;


import org.gayan.dls.constant.BookStatus;
import org.gayan.dls.dto.BorrowBookResponseDto;
import org.gayan.dls.entity.Book;
import org.gayan.dls.entity.BookCopy;
import org.gayan.dls.entity.Borrower;
import org.gayan.dls.entity.BorrowingHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/22/25
 * Time: 12:29 AM
 */
@Mapper(componentModel = "spring",imports = { LocalDateTime.class })
public interface BorrowingHistoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "bookCopy" , target = "bookCopy")
    @Mapping(source = "borrower" , target = "borrower")
    @Mapping(target = "borrowedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "returnedAt", expression = "java(null)")
    BorrowingHistory buildBorrowingHistory(BookCopy bookCopy , Borrower borrower);

    @Mapping(target = "bookId" , source = "book.id")
    @Mapping(target = "isbn" , source = "book.isbn")
    @Mapping(target = "bookName" , source = "book.title")
    @Mapping(target = "borrowerId" , source = "borrower.id")
    @Mapping(target = "borrowerName" , source = "borrower.name")
    @Mapping(target = "borrowerEmail" , source = "borrower.email")
    @Mapping(target = "borrowedAt" , source = "bookCopy.borrowedAt")
    @Mapping(
            target = "availableCopiesToBorrow",
            source = "book",
            qualifiedByName = "mapAvailableCopies"
    )    BorrowBookResponseDto buildResponseDto(Book book ,Borrower borrower , BookCopy bookCopy);

    // ---- Helper method ----
    @Named("mapAvailableCopies")
    default int mapAvailableCopies(Book book) {
        if (book.getCopies() == null) {
            return 0;
        }
        return (int) book.getCopies().stream()
                .filter(c -> !Boolean.TRUE.equals(c.getIsBorrowed()) && c.getBookStatus().equals(BookStatus.AVAILABLE))
                .count();
    }
}
