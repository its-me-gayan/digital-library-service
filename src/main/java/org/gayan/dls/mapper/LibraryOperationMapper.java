package org.gayan.dls.mapper;


import org.gayan.dls.constant.BookStatus;
import org.gayan.dls.dto.BorrowBookResponseDto;
import org.gayan.dls.dto.ReturnBookResponseDto;
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
 * Time: 10:18 PM
 */
@Mapper(componentModel = "spring",imports = { LocalDateTime.class })
public interface LibraryOperationMapper {

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
    )
    BorrowBookResponseDto buildBorrowResponseDto(Book book , Borrower borrower , BookCopy bookCopy);

    @Mapping(target = "bookId" , source = "book.id")
    @Mapping(target = "isbn" , source = "book.isbn")
    @Mapping(target = "bookName" , source = "book.title")
    @Mapping(target = "borrowerId" , source = "borrower.id")
    @Mapping(target = "borrowerName" , source = "borrower.name")
    @Mapping(target = "borrowerEmail" , source = "borrower.email")
    @Mapping(target = "returnedAt" , source = "borrowingHistory.returnedAt")
    @Mapping(
            target = "availableCopiesToBorrow",
            source = "book",
            qualifiedByName = "mapAvailableCopies"
    )
    ReturnBookResponseDto buildReturnResponseDto(Book book , Borrower borrower , BookCopy bookCopy , BorrowingHistory borrowingHistory);




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
