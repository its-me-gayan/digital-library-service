package org.gayan.dls.constant;


/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/20/25
 * Time: 12:43 AM
 */
public class ExceptionMessage {

    private ExceptionMessage() {
    }

    public static final String EXP_MSG_BORROW_FAILED = "Borrow failed - You have already bought a copy of the requested book !";
    public static final String EXP_MSG_BOOK_NOT_AVAILABLE = "Book not available for the provided id";
    public static final String EXP_MSG_BORROWER_NOT_AVAILABLE = "Borrower not available for the provided id";
    public static final String EXP_MSG_NO_COPIES_AVAILABLE = "No copies available to borrow !";
}
