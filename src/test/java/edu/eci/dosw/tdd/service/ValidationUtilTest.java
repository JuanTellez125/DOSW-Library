package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void shouldThrowWhenValueIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requireNonNull("   ", "campo"));
    }

    @Test
    void shouldThrowWhenValueIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requireNonNull("   ", "campo"));
    }

    @Test
    void shouldNotThrowWhenValueIsValid() {
        assertDoesNotThrow(() -> ValidationUtil.requireNonNull("valor", "campo"));
    }

    @Test
    void shouldThrowWhenObjectIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requireNonNull("   ", "campo"));
    }


}