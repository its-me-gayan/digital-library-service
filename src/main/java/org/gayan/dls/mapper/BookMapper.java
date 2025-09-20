package org.gayan.dls.mapper;


import org.gayan.dls.dto.BookCopyDto;
import org.gayan.dls.dto.BookRequestDto;
import org.gayan.dls.dto.BookResponseDto;
import org.gayan.dls.entity.Book;
import org.gayan.dls.entity.BookCopy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/20/25
 * Time: 10:07 PM
 */

@Mapper(componentModel = "spring",imports = { LocalDateTime.class })
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "isbn" , target = "isbn")
    @Mapping(source = "title" , target = "title")
    @Mapping(source = "author" , target = "author")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    Book mapBookRequestDtoToBookEntity(BookRequestDto bookRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "book", target = "book") // map entity to relation
    @Mapping(target = "isBorrowed", constant = "false")
    @Mapping(target = "borrowedAt", expression = "java(null)") // not borrowed yet
    @Mapping(target = "borrowedBy", expression = "java(null)") // no borrower yet
    BookCopy buildBookCopy(Book book);


    // --------- Book mapping without copies ----------
    @Mapping(target = "copies", ignore = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "isbn", target = "isbn")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(target = "copiesCount", expression = "java(book.getCopies() != null ? book.getCopies().size() : 0)")
    BookResponseDto mapBookEntityToBookResponseDto(Book book);

    // --------- Book mapping with copies ----------
    @Mapping(source = "copies", target = "copies", qualifiedByName = "mapBookCopyToDto")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "isbn", target = "isbn")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(target = "copiesCount", expression = "java(book.getCopies() != null ? book.getCopies().size() : 0)")
    BookResponseDto mapBookEntityToBookResponseDtoWithCopies(Book book);

    @Named("mapBookCopyToDto")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "isBorrowed", target = "isBorrowed")
    @Mapping(source = "borrowedBy", target = "borrowedBy")
    @Mapping(source = "borrowedAt", target = "borrowedAt")
    BookCopyDto mapBookCopyToDto(BookCopy copy);
}
