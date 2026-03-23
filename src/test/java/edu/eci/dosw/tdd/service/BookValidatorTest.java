package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.validator.BookValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookValidatorTest {

    @InjectMocks
    private BookValidator bookValidator;

    @Test
    void shouldNotThrowWhenTitleAndAuthorAreValid() {
        assertDoesNotThrow(() -> bookValidator.validate("Clean Code", "Robert Martin"));
    }

    @Test
    void shouldThrowWhenTitleIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> bookValidator.validate("", "Robert Martin"));
    }

    @Test
    void shouldThrowWhenTitleIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> bookValidator.validate(null, "Robert Martin"));
    }

    @Test
    void shouldThrowWhenAuthorIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> bookValidator.validate("Clean Code", ""));
    }

    @Test
    void shouldThrowWhenAuthorIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> bookValidator.validate("Clean Code", null));
    }

}