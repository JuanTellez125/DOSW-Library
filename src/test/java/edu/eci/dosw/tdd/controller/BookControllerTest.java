package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.ValidationException;
import edu.eci.dosw.tdd.core.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BookController.
 *
 * Scenarios covered:
 *  SUCCESS:
 *   - Add a book through controller
 *   - Get all books returns DTOs
 *   - Get book by ID returns correct DTO with copies
 *   - Update availability to false and to true
 *
 *  ERROR:
 *   - Add book with null/blank fields throws ValidationException
 *   - Get book by nonexistent ID throws BookNotFoundException
 *   - Update availability on nonexistent book throws BookNotFoundException
 */
@DisplayName("BookController Tests")
class BookControllerTest {

    private BookController bookController;

    @BeforeEach
    void setUp() {
        bookController = new BookController(new BookService());
    }

    // ─────────────────────────────── SUCCESS SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[SUCCESS] Should add a book through controller")
    void shouldAddBookThroughController() {
        BookDTO dto = new BookDTO("B001", "Clean Code", "R. Martin", "ISBN-1", true, 3);
        assertDoesNotThrow(() -> bookController.addBook(dto));
    }

    @Test
    @DisplayName("[SUCCESS] Should return all books as DTOs")
    void shouldReturnAllBooksAsDTOs() {
        bookController.addBook(new BookDTO("B001", "Clean Code", "R. Martin", "ISBN-1", true, 2));
        bookController.addBook(new BookDTO("B002", "Pragmatic Programmer", "Hunt", "ISBN-2", true, 1));

        List<BookDTO> books = bookController.getAllBooks();

        assertEquals(2, books.size());
    }

    @Test
    @DisplayName("[SUCCESS] Should return correct DTO fields for a book")
    void shouldReturnCorrectDTOFields() {
        bookController.addBook(new BookDTO("B001", "Clean Code", "R. Martin", "ISBN-1", true, 3));

        BookDTO result = bookController.getBookById("B001");

        assertEquals("B001", result.getId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("R. Martin", result.getAuthor());
        assertEquals("ISBN-1", result.getIsbn());
        assertEquals(3, result.getCopies());
        assertTrue(result.isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty list when no books added")
    void shouldReturnEmptyListWhenNoBooksAdded() {
        assertTrue(bookController.getAllBooks().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should update availability to false")
    void shouldUpdateAvailabilityToFalse() {
        bookController.addBook(new BookDTO("B001", "Clean Code", "R. Martin", "ISBN-1", true, 2));
        assertDoesNotThrow(() -> bookController.updateAvailability("B001", false));

        BookDTO result = bookController.getBookById("B001");
        assertFalse(result.isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] Should update availability to true after setting false")
    void shouldUpdateAvailabilityToTrue() {
        bookController.addBook(new BookDTO("B001", "Clean Code", "R. Martin", "ISBN-1", true, 2));
        bookController.updateAvailability("B001", false);
        bookController.updateAvailability("B001", true);

        BookDTO result = bookController.getBookById("B001");
        assertTrue(result.isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] Copies are reflected correctly in DTO after adding book")
    void shouldReflectCopiesInDTO() {
        bookController.addBook(new BookDTO("B001", "Clean Code", "R. Martin", "ISBN-1", true, 5));
        BookDTO result = bookController.getBookById("B001");
        assertEquals(5, result.getCopies());
    }

    // ─────────────────────────────── ERROR SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[ERROR] Should throw ValidationException when book title is blank")
    void shouldThrowWhenTitleBlank() {
        BookDTO dto = new BookDTO("B001", "", "Author", "ISBN", true, 1);
        assertThrows(ValidationException.class, () -> bookController.addBook(dto));
    }

    @Test
    @DisplayName("[ERROR] Should throw ValidationException when book id is blank")
    void shouldThrowWhenIdBlank() {
        BookDTO dto = new BookDTO("", "Title", "Author", "ISBN", true, 1);
        assertThrows(ValidationException.class, () -> bookController.addBook(dto));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotFoundException when getting nonexistent book")
    void shouldThrowWhenBookNotFound() {
        assertThrows(BookNotFoundException.class, () -> bookController.getBookById("GHOST"));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotFoundException when updating availability on nonexistent book")
    void shouldThrowWhenUpdatingNonexistentBook() {
        assertThrows(BookNotFoundException.class, () -> bookController.updateAvailability("GHOST", true));
    }

    @Test
    @DisplayName("[ERROR] Should throw when adding book with zero copies")
    void shouldThrowWhenZeroCopies() {
        BookDTO dto = new BookDTO("B001", "Title", "Author", "ISBN", true, 0);
        assertThrows(IllegalArgumentException.class, () -> bookController.addBook(dto));
    }
}
