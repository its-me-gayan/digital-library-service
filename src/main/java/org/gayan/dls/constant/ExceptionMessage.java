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

    public static final String EXP_MSG_BORROW_FAILED = "Borrow failed - You have already borrowed this book copy!";
    public static final String EXP_MSG_BOOK_NOT_AVAILABLE = "Book is not available for the provided ID.";
    public static final String EXP_MSG_BORROWER_NOT_AVAILABLE = "Borrower not found for the provided ID.";
    public static final String EXP_MSG_NO_COPIES_AVAILABLE = "No copies are currently available to borrow!";
    public static final String EXP_MSG_NOTHING_TO_RETURN = "Return failed - You have no borrowed books to return!";
    public static final String EXP_MSG_TRY_RETURN_DIFFERENT_BOOK = "Return failed - You are trying to return a book that you did not borrow.";
}
