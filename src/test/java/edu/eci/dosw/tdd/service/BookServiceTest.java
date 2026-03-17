package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.ValidationException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BookService.
 *
 * Scenarios covered:
 *  SUCCESS:
 *   - Add a book with valid data
 *   - Add multiple copies to existing book
 *   - Get all books
 *   - Get book by valid ID
 *   - Update availability (true / false)
 *   - Decrement copy reduces count
 *   - Increment copy restores count and availability
 *
 *  ERROR:
 *   - Add book with null book object
 *   - Add book with blank fields
 *   - Add book with zero or negative copies
 *   - Get book by null/blank/nonexistent ID
 *   - Decrement copy below zero throws BookNotAvailableException
 *   - Update availability for nonexistent book
 */
@DisplayName("BookService Tests")
class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
    }

    // ─────────────────────────────── SUCCESS SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[SUCCESS] Should add a book successfully")
    void shouldAddBookSuccessfully() {
        Book book = new Book("B001", "Clean Code", "Robert C. Martin", "978-0132350884");
        bookService.addBook(book, 3);

        Book found = bookService.getBookById("B001");
        assertNotNull(found);
        assertEquals("Clean Code", found.getTitle());
        assertEquals(3, bookService.getAvailableCopies("B001"));
    }

    @Test
    @DisplayName("[SUCCESS] Should accumulate copies when same book is added twice")
    void shouldAccumulateCopiesForExistingBook() {
        Book book = new Book("B001", "Clean Code", "Robert C. Martin", "978-0132350884");
        bookService.addBook(book, 2);
        bookService.addBook(book, 3);

        assertEquals(5, bookService.getAvailableCopies("B001"));
    }

    @Test
    @DisplayName("[SUCCESS] Should return all books")
    void shouldReturnAllBooks() {
        bookService.addBook(new Book("B001", "Book One", "Author A", "ISBN-1"), 1);
        bookService.addBook(new Book("B002", "Book Two", "Author B", "ISBN-2"), 2);

        List<Book> books = bookService.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty list when catalog is empty")
    void shouldReturnEmptyListWhenNoBooksAdded() {
        assertTrue(bookService.getAllBooks().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should find book by ID")
    void shouldFindBookById() {
        bookService.addBook(new Book("B002", "The Pragmatic Programmer", "Hunt & Thomas", "ISBN-2"), 1);
        Book found = bookService.getBookById("B002");
        assertEquals("B002", found.getId());
    }

    @Test
    @DisplayName("[SUCCESS] Should update book availability to false")
    void shouldUpdateAvailabilityToFalse() {
        bookService.addBook(new Book("B001", "Clean Code", "R. Martin", "ISBN"), 2);
        bookService.updateAvailability("B001", false);
        assertFalse(bookService.getBookById("B001").isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] Should update book availability to true")
    void shouldUpdateAvailabilityToTrue() {
        Book book = new Book("B001", "Clean Code", "R. Martin", "ISBN");
        bookService.addBook(book, 2);
        bookService.updateAvailability("B001", false);
        bookService.updateAvailability("B001", true);
        assertTrue(bookService.getBookById("B001").isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] Should decrement available copies on borrow")
    void shouldDecrementCopiesOnBorrow() {
        bookService.addBook(new Book("B001", "Clean Code", "R. Martin", "ISBN"), 2);
        bookService.decrementCopy("B001");
        assertEquals(1, bookService.getAvailableCopies("B001"));
    }

    @Test
    @DisplayName("[SUCCESS] Should set unavailable when last copy is borrowed")
    void shouldSetUnavailableWhenLastCopyBorrowed() {
        bookService.addBook(new Book("B001", "Clean Code", "R. Martin", "ISBN"), 1);
        bookService.decrementCopy("B001");
        assertFalse(bookService.getBookById("B001").isAvailable());
        assertEquals(0, bookService.getAvailableCopies("B001"));
    }

    @Test
    @DisplayName("[SUCCESS] Should increment copies and restore availability on return")
    void shouldIncrementCopiesOnReturn() {
        bookService.addBook(new Book("B001", "Clean Code", "R. Martin", "ISBN"), 1);
        bookService.decrementCopy("B001");
        bookService.incrementCopy("B001");
        assertEquals(1, bookService.getAvailableCopies("B001"));
        assertTrue(bookService.getBookById("B001").isAvailable());
    }

    // ─────────────────────────────── ERROR SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[ERROR] Should throw ValidationException when adding null book")
    void shouldThrowWhenBookIsNull() {
        assertThrows(ValidationException.class, () -> bookService.addBook(null, 1));
    }

    @Test
    @DisplayName("[ERROR] Should throw when adding book with zero copies")
    void shouldThrowWhenCopiesIsZero() {
        Book book = new Book("B001", "Clean Code", "R. Martin", "ISBN");
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book, 0));
    }

    @Test
    @DisplayName("[ERROR] Should throw when adding book with negative copies")
    void shouldThrowWhenCopiesIsNegative() {
        Book book = new Book("B001", "Clean Code", "R. Martin", "ISBN");
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book, -5));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("[ERROR] Should throw ValidationException for blank/null book ID on getById")
    void shouldThrowForBlankBookId(String id) {
        assertThrows(ValidationException.class, () -> bookService.getBookById(id));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotFoundException when book ID does not exist")
    void shouldThrowBookNotFoundForUnknownId() {
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById("UNKNOWN"));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotAvailableException when decrementing with 0 copies")
    void shouldThrowWhenDecrementingWithNoCopies() {
        bookService.addBook(new Book("B001", "Clean Code", "R. Martin", "ISBN"), 1);
        bookService.decrementCopy("B001"); // takes last copy
        assertThrows(BookNotAvailableException.class, () -> bookService.decrementCopy("B001"));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotFoundException when updating availability on nonexistent book")
    void shouldThrowWhenUpdatingAvailabilityForNonexistentBook() {
        assertThrows(BookNotFoundException.class, () -> bookService.updateAvailability("GHOST", true));
    }

    @Test
    @DisplayName("[ERROR] Should throw ValidationException when book has blank title")
    void shouldThrowWhenBookHasBlankTitle() {
        Book book = new Book("B001", "  ", "Author", "ISBN");
        assertThrows(ValidationException.class, () -> bookService.addBook(book, 1));
    }

    @Test
    @DisplayName("[ERROR] Should throw ValidationException when book has blank author")
    void shouldThrowWhenBookHasBlankAuthor() {
        Book book = new Book("B001", "Title", "", "ISBN");
        assertThrows(ValidationException.class, () -> bookService.addBook(book, 1));
    }
}
