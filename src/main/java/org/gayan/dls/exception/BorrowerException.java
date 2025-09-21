package org.gayan.dls.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/20/25
 * Time: 12:30 AM
 */

public class BorrowerException extends RuntimeException{
    @Getter
    private HttpStatus httpStatus;

    public BorrowerException(String message, Throwable cause) {
        super(message, cause);
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public BorrowerException(String message) {
        super(message);
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public BorrowerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BorrowerException() {
    }

}
