package org.gayan.dls.service;

import org.gayan.dls.dto.BorrowerRequestDto;
import org.gayan.dls.dto.BorrowerResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.exception.BookException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/** Author: Gayan Sanjeewa User: gayan Date: 9/20/25 Time: 12:28 AM */
public interface BorrowerManagementService {
  ResponseEntity<ApiResponse<BorrowerResponseDto>> persistBorrower(
      BorrowerRequestDto borrowerRequestDto) throws BookException;

  ResponseEntity<ApiResponse<BorrowerResponseDto>> getBorrowerById(String borrowerId)
      throws BookException;

  ResponseEntity<ApiResponse<Page<BorrowerResponseDto>>> getAllBorrowersWithPagination(
      Pageable pageable) throws BookException;
}
