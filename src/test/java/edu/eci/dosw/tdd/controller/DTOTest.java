package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.dto.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DTOs — ensures all getters, setters and constructors are covered,
 * which eliminates the 58% coverage gap on edu.eci.dosw.tdd.controller.dto.
 */
@DisplayName("DTO Tests")
class DTOTest {

    // ─────────────────────────────── BookDTO ───────────────────────────────

    @Test
    @DisplayName("[SUCCESS] BookDTO all-args constructor and getters")
    void bookDTOAllArgsConstructor() {
        BookDTO dto = new BookDTO("B001", "Clean Code", "R. Martin", "ISBN-1", true, 3);

        assertEquals("B001", dto.getId());
        assertEquals("Clean Code", dto.getTitle());
        assertEquals("R. Martin", dto.getAuthor());
        assertEquals("ISBN-1", dto.getIsbn());
        assertTrue(dto.isAvailable());
        assertEquals(3, dto.getCopies());
    }

    @Test
    @DisplayName("[SUCCESS] BookDTO no-args constructor and setters")
    void bookDTONoArgsAndSetters() {
        BookDTO dto = new BookDTO();
        dto.setId("B002");
        dto.setTitle("Pragmatic Programmer");
        dto.setAuthor("Hunt");
        dto.setIsbn("ISBN-2");
        dto.setAvailable(false);
        dto.setCopies(5);

        assertEquals("B002", dto.getId());
        assertEquals("Pragmatic Programmer", dto.getTitle());
        assertEquals("Hunt", dto.getAuthor());
        assertEquals("ISBN-2", dto.getIsbn());
        assertFalse(dto.isAvailable());
        assertEquals(5, dto.getCopies());
    }

    // ─────────────────────────────── UserDTO ───────────────────────────────

    @Test
    @DisplayName("[SUCCESS] UserDTO all-args constructor and getters")
    void userDTOAllArgsConstructor() {
        UserDTO dto = new UserDTO("U001", "Alice García", "alice@dosw.edu", 2);

        assertEquals("U001", dto.getId());
        assertEquals("Alice García", dto.getName());
        assertEquals("alice@dosw.edu", dto.getEmail());
        assertEquals(2, dto.getLoanCount());
    }

    @Test
    @DisplayName("[SUCCESS] UserDTO no-args constructor and setters")
    void userDTONoArgsAndSetters() {
        UserDTO dto = new UserDTO();
        dto.setId("U002");
        dto.setName("Bob Martínez");
        dto.setEmail("bob@dosw.edu");
        dto.setLoanCount(1);

        assertEquals("U002", dto.getId());
        assertEquals("Bob Martínez", dto.getName());
        assertEquals("bob@dosw.edu", dto.getEmail());
        assertEquals(1, dto.getLoanCount());
    }

    // ─────────────────────────────── LoanDTO ───────────────────────────────

    @Test
    @DisplayName("[SUCCESS] LoanDTO all-args constructor and getters")
    void loanDTOAllArgsConstructor() {
        LoanDTO dto = new LoanDTO("L001", "U001", "B001", "2026-03-16", null, false);

        assertEquals("L001", dto.getId());
        assertEquals("U001", dto.getUserId());
        assertEquals("B001", dto.getBookId());
        assertEquals("2026-03-16", dto.getLoanDate());
        assertNull(dto.getReturnDate());
        assertFalse(dto.isReturned());
    }

    @Test
    @DisplayName("[SUCCESS] LoanDTO no-args constructor and setters")
    void loanDTONoArgsAndSetters() {
        LoanDTO dto = new LoanDTO();
        dto.setId("L002");
        dto.setUserId("U002");
        dto.setBookId("B002");
        dto.setLoanDate("2026-03-15");
        dto.setReturnDate("2026-03-16");
        dto.setReturned(true);

        assertEquals("L002", dto.getId());
        assertEquals("U002", dto.getUserId());
        assertEquals("B002", dto.getBookId());
        assertEquals("2026-03-15", dto.getLoanDate());
        assertEquals("2026-03-16", dto.getReturnDate());
        assertTrue(dto.isReturned());
    }
}
