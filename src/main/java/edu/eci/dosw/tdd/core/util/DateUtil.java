package edu.eci.dosw.tdd.core.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DateUtil() {}

    public static String format(LocalDate date) {
        ValidationUtil.requireNonNull(date, "La fecha");
        return date.format(FORMATTER);
    }

    public static LocalDate parse(String date) {
        ValidationUtil.requireNonNull(date, "La fecha");
        return LocalDate.parse(date, FORMATTER);
    }

    public static boolean isValidLoanDate(LocalDate date) {
        ValidationUtil.requireNonNull(date, "La fecha");
        return !date.isAfter(LocalDate.now());
    }

}