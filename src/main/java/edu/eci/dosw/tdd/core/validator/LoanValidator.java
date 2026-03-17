package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.util.ValidationUtil;


public class LoanValidator {

    public void validateLoanRequest(String userId, String bookId) {
        ValidationUtil.requireNonBlank(userId, "userId");
        ValidationUtil.requireNonBlank(bookId, "bookId");
    }

    public void validateReturnRequest(String loanId) {
        ValidationUtil.requireNonBlank(loanId, "loanId");
    }
}
