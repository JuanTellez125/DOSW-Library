package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void shouldFormatDateCorrectly() {
        LocalDate date = LocalDate.of(2026, 3, 23);
        assertEquals("23/03/2026", DateUtil.format(date));
    }

    @Test
    void shouldThrowWhenFormatReceivesNull() {
        assertThrows(IllegalArgumentException.class,
                () -> DateUtil.format(null));
    }

    @Test
    void shouldParseDateCorrectly() {
        LocalDate result = DateUtil.parse("23/03/2026");
        assertEquals(LocalDate.of(2026, 3, 23), result);
    }

    @Test
    void shouldThrowWhenParseReceivesBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> DateUtil.parse(""));
    }

    @Test
    void shouldReturnTrueForTodayDate() {
        assertTrue(DateUtil.isValidLoanDate(LocalDate.now()));
    }

    @Test
    void shouldReturnTrueForPastDate() {
        assertTrue(DateUtil.isValidLoanDate(LocalDate.now().minusDays(1)));
    }

    @Test
    void shouldReturnFalseForFutureDate() {
        assertFalse(DateUtil.isValidLoanDate(LocalDate.now().plusDays(1)));
    }

    @Test
    void shouldThrowWhenIsValidLoanDateReceivesNull() {
        assertThrows(IllegalArgumentException.class,
                () -> DateUtil.isValidLoanDate(null));
    }

}