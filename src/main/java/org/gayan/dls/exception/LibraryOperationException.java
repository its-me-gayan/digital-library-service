package org.gayan.dls.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/** Author: Gayan Sanjeewa User: gayan Date: 9/20/25 Time: 12:30 AM */
public class LibraryOperationException extends RuntimeException {

  @Getter private HttpStatus httpStatus;

  public LibraryOperationException(String message, Throwable cause) {
    super(message, cause);
    httpStatus = HttpStatus.BAD_REQUEST;
  }

  public LibraryOperationException(String message) {
    super(message);
    httpStatus = HttpStatus.BAD_REQUEST;
  }

  public LibraryOperationException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public LibraryOperationException() {}
}
