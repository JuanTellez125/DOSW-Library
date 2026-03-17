package edu.eci.dosw.tdd.core.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String id) {
        super("Book with id '" + id + "' was not found.");
    }
}
