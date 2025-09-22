package org.gayan.dls.dto;

/** Author: Gayan Sanjeewa User: gayan Date: 9/19/25 Time: 11:21 PM */
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BookRequestDto(
    @NotNull(message = "ISBN is required")
        @Pattern(regexp = "\\d{13}", message = "ISBN must be 13 digits")
        String isbn,
    @NotNull(message = "Title is required")
        @Size(min = 1, max = 500, message = "Title must be between 1 and 500 characters")
        String title,
    @NotNull(message = "Author is required")
        @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
        String author) {}
