package org.gayan.dls.service;


import org.gayan.dls.dto.*;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.exception.BookException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/20/25
 * Time: 12:28 AM
 */
public interface LibraryOperationService {
    ResponseEntity<ApiResponse<BorrowBookResponseDto>> borrowBook(BorrowBookRequestDto bookRequestDto) throws BookException;
    ResponseEntity<ApiResponse<ReturnBookResponseDto>> returnBook(ReturnBookRequestDto returnBookRequestDto) throws BookException;
}
