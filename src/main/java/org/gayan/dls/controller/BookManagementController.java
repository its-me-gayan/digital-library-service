package org.gayan.dls.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gayan.dls.constant.ApplicationConstant;
import org.gayan.dls.dto.BookRequestDto;
import org.gayan.dls.dto.BookResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.service.BookManagementService;
import org.gayan.dls.util.ResponseBuilder;
import org.gayan.dls.validation.annotation.ValidUUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 10:16 PM
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class BookManagementController {

    private final BookManagementService bookManagementService;

    @PostMapping("/books")
    public ResponseEntity<ApiResponse<BookResponseDto>> addBook(@Valid @RequestBody BookRequestDto bookRequestDto){
        return bookManagementService.persistBook(bookRequestDto);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<ApiResponse<BookResponseDto>> getBookById(@ValidUUID @PathVariable(value = "bookId") String bookId){
        return bookManagementService.getBookById(bookId);
    }

    @GetMapping("/books?page=0&size=20&sortBy=title&sortDir=asc")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getAllBooksWithPagination(@PathVariable("bookId") String bookId){
        return bookManagementService
                .getAllBooksWithPagination(0, 0, "", "");
    }
}
