package org.gayan.dls.exception;

/** Author: Gayan Sanjeewa User: gayan Date: 9/21/25 Time: 1:40 AM */
public class NoContentFoundException extends RuntimeException {
  public NoContentFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoContentFoundException(String message) {
    super(message);
  }

  public NoContentFoundException() {}
}
