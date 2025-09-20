package org.gayan.dls.service;


import org.gayan.dls.dto.BookRequestDto;
import org.gayan.dls.dto.BookResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.exception.BookException;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/20/25
 * Time: 12:28 AM
 */
public interface BookManagementService {
    ResponseEntity<ApiResponse<BookResponseDto>> persistBook(BookRequestDto bookRequestDto) throws BookException;
    ResponseEntity<ApiResponse<BookResponseDto>> getBookById(String bookId) throws BookException;
    ResponseEntity<ApiResponse<List<BookResponseDto>>>  getAllBooksWithPagination(int page , int size , String sortBy , String sortDirection ) throws BookException;
}
