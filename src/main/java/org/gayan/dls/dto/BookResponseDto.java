package org.gayan.dls.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/** Author: Gayan Sanjeewa User: gayan Date: 9/20/25 Time: 12:26 AM */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BookResponseDto {
  private UUID id;
  private String isbn;
  private String title;
  private String author;
  private int copiesCount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<BookCopyDto> copies;
}
