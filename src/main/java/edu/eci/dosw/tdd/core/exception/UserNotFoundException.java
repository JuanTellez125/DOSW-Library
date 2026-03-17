package edu.eci.dosw.tdd.core.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("User with id '" + id + "' was not found.");
    }
}
