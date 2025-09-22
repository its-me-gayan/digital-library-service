package org.gayan.dls.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gayan.dls.dto.BorrowerRequestDto;
import org.gayan.dls.dto.BorrowerResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.service.BorrowerManagementService;
import org.gayan.dls.validation.annotation.ValidUUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/** Author: Gayan Sanjeewa User: gayan Date: 9/19/25 Time: 10:16 PM */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Validated
public class BorrowerManagementController {

  private final BorrowerManagementService borrowerManagementService;

  @PostMapping("/borrowers")
  public ResponseEntity<ApiResponse<BorrowerResponseDto>> registerBorrower(
      @Valid @RequestBody BorrowerRequestDto borrowerRequestDto) {
    return borrowerManagementService.persistBorrower(borrowerRequestDto);
  }

  @GetMapping("/borrowers/{borrowerId}")
  public ResponseEntity<ApiResponse<BorrowerResponseDto>> getBorrowerById(
      @ValidUUID(message = "Invalid Borrower Id format - should be a UUID")
          @PathVariable("borrowerId")
          String borrowerId) {
    return borrowerManagementService.getBorrowerById(borrowerId);
  }

  @GetMapping("/borrowers")
  public ResponseEntity<ApiResponse<Page<BorrowerResponseDto>>> getAllBorrowersWithPagination(
      @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC)
          Pageable pageable) {
    return borrowerManagementService.getAllBorrowersWithPagination(pageable);
  }
}
