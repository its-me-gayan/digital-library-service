package org.gayan.dls.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 10:16 PM
 */
@RestController
@RequestMapping("/api/v1")
public class BorrowerManagementController {

    @PostMapping("/borrowers")
    public ResponseEntity<?> registerBorrower(){
        return null;
    }
    @GetMapping("/borrowers/{borrowerId}")
    public ResponseEntity<?> getBorrowerById(@PathVariable("borrowerId") String borrowerId){
        return null;
    }
    @GetMapping("/borrowers?page=0&size=20&sortBy=title&sortDir=asc")
    public ResponseEntity<?> getAllBorrowersWithPagination(@PathVariable("borrowerId") String borrowerId){
        return null;
    }
}
