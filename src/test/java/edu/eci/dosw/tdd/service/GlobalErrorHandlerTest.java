package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GlobalErrorHandler.
 */
@DisplayName("GlobalErrorHandler Tests")
class GlobalErrorHandlerTest {

    private GlobalErrorHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalErrorHandler();
    }

    @Test
    @DisplayName("[SUCCESS] Should handle BookNotAvailableException")
    void shouldHandleBookNotAvailable() {
        var response = handler.handle(new BookNotAvailableException("B001"));
        assertEquals("BOOK_NOT_AVAILABLE", response.errorCode());
        assertTrue(response.message().contains("B001"));
    }

    @Test
    @DisplayName("[SUCCESS] Should handle BookNotFoundException")
    void shouldHandleBookNotFound() {
        var response = handler.handle(new BookNotFoundException("B002"));
        assertEquals("BOOK_NOT_FOUND", response.errorCode());
    }

    @Test
    @DisplayName("[SUCCESS] Should handle UserNotFoundException")
    void shouldHandleUserNotFound() {
        var response = handler.handle(new UserNotFoundException("U001"));
        assertEquals("USER_NOT_FOUND", response.errorCode());
    }

    @Test
    @DisplayName("[SUCCESS] Should handle LoanLimitExceededException")
    void shouldHandleLoanLimitExceeded() {
        var response = handler.handle(new LoanLimitExceededException("U001"));
        assertEquals("LOAN_LIMIT_EXCEEDED", response.errorCode());
    }

    @Test
    @DisplayName("[SUCCESS] Should handle ValidationException")
    void shouldHandleValidationException() {
        var response = handler.handle(new ValidationException("field is blank"));
        assertEquals("VALIDATION_ERROR", response.errorCode());
        assertEquals("field is blank", response.message());
    }

    @Test
    @DisplayName("[SUCCESS] Should handle unknown exception as INTERNAL_ERROR")
    void shouldHandleUnknownException() {
        var response = handler.handle(new RuntimeException("something went wrong"));
        assertEquals("INTERNAL_ERROR", response.errorCode());
        assertTrue(response.message().contains("something went wrong"));
    }
}
