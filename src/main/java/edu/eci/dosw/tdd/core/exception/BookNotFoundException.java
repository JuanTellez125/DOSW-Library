package edu.eci.dosw.tdd.core.exception;

/**
 * Thrown when a book is not found in the system.
 */
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String bookId) {
        super("Book with id '" + bookId + "' was not found.");
    }
}
