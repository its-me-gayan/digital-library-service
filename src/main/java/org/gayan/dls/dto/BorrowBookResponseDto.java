package org.gayan.dls.dto;


/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 11:21 PM
 */
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BorrowBookResponseDto {
    private String bookId;
    private String isbn;
    private String bookName;
    private String borrowerId;
    private String borrowerName;
    private String borrowerEmail;
    private String availableCopiesToBorrow;
    private LocalDateTime borrowedAt;

}
