package edu.eci.dosw.tdd.core.util;

import java.time.LocalDate;

public class ValidationUtil {

    private ValidationUtil() {}

    public static void requireNonNull(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }

    public static void requireNonNull(LocalDate value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }
}
