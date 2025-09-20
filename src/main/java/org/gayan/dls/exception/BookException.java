package org.gayan.dls.exception;


/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/20/25
 * Time: 12:30 AM
 */
public class BookException extends RuntimeException{
    public BookException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookException(String message) {
        super(message);
    }

    public BookException() {
    }
}
