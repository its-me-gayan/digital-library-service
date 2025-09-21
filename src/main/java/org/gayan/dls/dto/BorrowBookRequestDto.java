package org.gayan.dls.dto;


/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 11:21 PM
 */
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.gayan.dls.validation.annotation.ValidUUID;

@Data
public class BorrowBookRequestDto {

    @NotNull(message = "bookId is required")
    @ValidUUID(message = "Invalid Book id - should be a UUID")
    private String bookId;

    @NotNull(message = "borrowerId is required")
    @ValidUUID(message = "Invalid borrower Id - should be a UUID")
    private String borrowerId;

}
