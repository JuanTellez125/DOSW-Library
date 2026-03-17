package edu.eci.dosw.tdd.core.exception;

import edu.eci.dosw.tdd.core.model.User;

public class LoanLimitExceededException extends RuntimeException {
    public LoanLimitExceededException(String userId) {
        super("User '" + userId + "' has reached the maximum loan limit of " + User.MAX_LOANS + ".");
    }
}
