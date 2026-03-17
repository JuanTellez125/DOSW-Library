package edu.eci.dosw.tdd.core.util;

import edu.eci.dosw.tdd.core.exception.ValidationException;

/**
 * Utility class for common validation operations.
 */
public class ValidationUtil {

    private ValidationUtil() {}

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " must not be null or blank.");
        }
    }

    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " must not be null.");
        }
    }

    public static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " must be a positive number.");
        }
    }

    public static void requireValidEmail(String email) {
        requireNonBlank(email, "email");
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }
}
