package edu.eci.dosw.tdd.core.exception;

/**
 * Global error handler that centralizes exception management for the library system.
 * Returns structured ErrorResponse objects for each type of exception.
 */
public class GlobalErrorHandler {

    public record ErrorResponse(String errorCode, String message) {}

    public ErrorResponse handle(Exception e) {
        if (e instanceof BookNotAvailableException) {
            return new ErrorResponse("BOOK_NOT_AVAILABLE", e.getMessage());
        } else if (e instanceof BookNotFoundException) {
            return new ErrorResponse("BOOK_NOT_FOUND", e.getMessage());
        } else if (e instanceof UserNotFoundException) {
            return new ErrorResponse("USER_NOT_FOUND", e.getMessage());
        } else if (e instanceof LoanLimitExceededException) {
            return new ErrorResponse("LOAN_LIMIT_EXCEEDED", e.getMessage());
        } else if (e instanceof ValidationException) {
            return new ErrorResponse("VALIDATION_ERROR", e.getMessage());
        } else {
            return new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred: " + e.getMessage());
        }
    }
}
