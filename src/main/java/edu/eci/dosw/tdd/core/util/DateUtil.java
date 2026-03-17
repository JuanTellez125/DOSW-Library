package edu.eci.dosw.tdd.core.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date operations.
 */
public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtil() {}

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static String format(LocalDate date) {
        if (date == null) return null;
        return date.format(FORMATTER);
    }

    public static LocalDate parse(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        return LocalDate.parse(dateStr, FORMATTER);
    }

    public static boolean isOverdue(LocalDate loanDate, int maxDays) {
        return LocalDate.now().isAfter(loanDate.plusDays(maxDays));
    }
}
