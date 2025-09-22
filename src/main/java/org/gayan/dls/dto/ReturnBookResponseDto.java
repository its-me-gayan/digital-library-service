package org.gayan.dls.dto;

/** Author: Gayan Sanjeewa User: gayan Date: 9/19/25 Time: 11:21 PM */
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReturnBookResponseDto {
  private String bookId;
  private String isbn;
  private String bookName;
  private String borrowerId;
  private String borrowerName;
  private String borrowerEmail;
  private String availableCopiesToBorrow;
  private LocalDateTime returnedAt;
}
