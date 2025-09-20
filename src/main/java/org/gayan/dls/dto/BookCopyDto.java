package org.gayan.dls.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.gayan.dls.entity.Borrower;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/21/25
 * Time: 12:11 AM
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BookCopyDto {
    private UUID id;
    private Boolean isBorrowed;
    private Borrower borrowedBy;
    private LocalDateTime borrowedAt;
}
