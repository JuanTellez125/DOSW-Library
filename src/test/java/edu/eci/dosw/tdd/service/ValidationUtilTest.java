package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.exception.ValidationException;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidationUtil Tests")
class ValidationUtilTest {

    @Test
    @DisplayName("[SUCCESS] requireNonBlank passes for valid string")
    void requireNonBlankPassesForValidString() {
        assertDoesNotThrow(() -> ValidationUtil.requireNonBlank("hello", "field"));
    }

    @Test
    @DisplayName("[ERROR] requireNonBlank throws for null")
    void requireNonBlankThrowsForNull() {
        assertThrows(ValidationException.class, () -> ValidationUtil.requireNonBlank(null, "field"));
    }

    @Test
    @DisplayName("[ERROR] requireNonBlank throws for blank string")
    void requireNonBlankThrowsForBlank() {
        assertThrows(ValidationException.class, () -> ValidationUtil.requireNonBlank("   ", "field"));
    }

    @Test
    @DisplayName("[SUCCESS] requireNonNull passes for non-null object")
    void requireNonNullPassesForNonNull() {
        assertDoesNotThrow(() -> ValidationUtil.requireNonNull(new Object(), "obj"));
    }

    @Test
    @DisplayName("[ERROR] requireNonNull throws for null object")
    void requireNonNullThrowsForNull() {
        assertThrows(ValidationException.class, () -> ValidationUtil.requireNonNull(null, "obj"));
    }

    @Test
    @DisplayName("[SUCCESS] requirePositive passes for positive number")
    void requirePositivePassesForPositive() {
        assertDoesNotThrow(() -> ValidationUtil.requirePositive(5, "count"));
    }

    @Test
    @DisplayName("[ERROR] requirePositive throws for zero")
    void requirePositiveThrowsForZero() {
        assertThrows(ValidationException.class, () -> ValidationUtil.requirePositive(0, "count"));
    }

    @Test
    @DisplayName("[ERROR] requirePositive throws for negative")
    void requirePositiveThrowsForNegative() {
        assertThrows(ValidationException.class, () -> ValidationUtil.requirePositive(-1, "count"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@domain.com", "test.name@sub.org", "a@b.io"})
    @DisplayName("[SUCCESS] requireValidEmail accepts valid emails")
    void requireValidEmailAcceptsValid(String email) {
        assertDoesNotThrow(() -> ValidationUtil.requireValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"noat", "@missing.com", "missing@", "two@@at.com"})
    @DisplayName("[ERROR] requireValidEmail rejects invalid emails")
    void requireValidEmailRejectsInvalid(String email) {
        assertThrows(ValidationException.class, () -> ValidationUtil.requireValidEmail(email));
    }
}
