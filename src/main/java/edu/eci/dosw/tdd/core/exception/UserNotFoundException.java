package edu.eci.dosw.tdd.core.exception;

/**
 * Thrown when a user is not found in the system.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("User with id '" + userId + "' was not found.");
    }
}
