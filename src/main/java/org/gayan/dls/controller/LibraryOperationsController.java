package org.gayan.dls.controller;


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
public class LibraryOperationsController {

    @PostMapping("/borrowBook")
    public ResponseEntity<?> borrowBook(){
return null;
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
