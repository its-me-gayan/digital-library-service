package org.gayan.dls.dto;

/** Author: Gayan Sanjeewa User: gayan Date: 9/19/25 Time: 11:21 PM */
import jakarta.validation.constraints.NotNull;
import org.gayan.dls.validation.annotation.ValidUUID;

public record BorrowBookRequestDto(
    @NotNull(message = "bookId is required")
        @ValidUUID(message = "Invalid Book id - should be a UUID")
        String bookId,
    @NotNull(message = "borrowerId is required")
        @ValidUUID(message = "Invalid borrower Id - should be a UUID")
        String borrowerId) {}
