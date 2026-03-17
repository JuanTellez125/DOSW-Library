package edu.eci.dosw.tdd.core.exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String id) {
        super("Book with id '" + id + "' is not available for loan.");
    }
}
