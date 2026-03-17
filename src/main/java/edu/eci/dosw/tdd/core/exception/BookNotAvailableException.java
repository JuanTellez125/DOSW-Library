package edu.eci.dosw.tdd.core.exception;

/**
 * Thrown when a book is not available for loan.
 */
public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String bookId) {
        super("Book with id '" + bookId + "' is not available for loan.");
    }
}
