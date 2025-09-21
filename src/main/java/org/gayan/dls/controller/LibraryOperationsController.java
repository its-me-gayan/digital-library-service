package org.gayan.dls.controller;


import lombok.RequiredArgsConstructor;
import org.gayan.dls.dto.BorrowBookRequestDto;
import org.gayan.dls.dto.BorrowBookResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.service.LibraryOperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 10:17 PM
 */
@RestController
@RequestMapping("/api/v1/operation/")
@RequiredArgsConstructor
public class LibraryOperationsController {

    private final LibraryOperationService libraryOperationService;

    @PostMapping("/borrowBook")
    public ResponseEntity<ApiResponse<BorrowBookResponseDto>> borrowBook(@RequestBody BorrowBookRequestDto borrowBookRequestDto){
return libraryOperationService.borrowBook(borrowBookRequestDto);
    }

    @PostMapping("/returnBook")
    public ResponseEntity<?> returnBook(){
        return null;
    }

    @GetMapping("/getBorrowing/{borrowerId}")
    public ResponseEntity<?> getBorrowerBorrowings(@PathVariable("borrowerId") String borrowerId){
        return null;
    }
}
