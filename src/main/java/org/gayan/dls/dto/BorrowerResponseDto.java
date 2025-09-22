package org.gayan.dls.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/** Author: Gayan Sanjeewa User: gayan Date: 9/20/25 Time: 12:26 AM */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BorrowerResponseDto {
  private UUID id;
  private String name;
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
