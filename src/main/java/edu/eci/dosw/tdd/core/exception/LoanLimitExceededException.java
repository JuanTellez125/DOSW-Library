package edu.eci.dosw.tdd.core.exception;

/**
 * Thrown when a user exceeds the maximum number of allowed loans.
 */
public class LoanLimitExceededException extends RuntimeException {
    public LoanLimitExceededException(String userId) {
        super("User with id '" + userId + "' has reached the maximum loan limit of " +
              edu.eci.dosw.tdd.core.model.User.MAX_LOANS + " books.");
    }
}
