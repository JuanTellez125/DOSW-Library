package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.service.BookService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookService using Mockito to mock BookRepository.
 * Spring context is NOT loaded — pure unit tests.
 *
 * Scenarios covered:
 *  SUCCESS: addBook, getAllBooks, getBookById, getAvailableCopies,
 *           updateAvailability, decrementCopy (with last-copy case),
 *           incrementCopy
 *  ERROR:   addBook with zero/negative copies, getBookById nonexistent,
 *           decrementCopy with no copies left
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Unit Tests (Mockito)")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book("B001", "Clean Code", "R. Martin", "ISBN-1");
    }

    // ─── SUCCESS ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[SUCCESS] Should add book and save to repository")
    void shouldAddBook() {
        Book result = bookService.addBook(sampleBook, 3);

        verify(bookRepository).save(sampleBook, 3);
        assertEquals("B001", result.getId());
    }

    @Test
    @DisplayName("[SUCCESS] Should return all books from repository")
    void shouldGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook));

        List<Book> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty list when no books exist")
    void shouldReturnEmptyListWhenNoBooksExist() {
        when(bookRepository.findAll()).thenReturn(List.of());
        assertTrue(bookService.getAllBooks().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should find book by ID")
    void shouldFindBookById() {
        when(bookRepository.findById("B001")).thenReturn(Optional.of(sampleBook));

        Book result = bookService.getBookById("B001");

        assertEquals("B001", result.getId());
        verify(bookRepository).findById("B001");
    }

    @Test
    @DisplayName("[SUCCESS] Should return available copies")
    void shouldReturnAvailableCopies() {
        when(bookRepository.findById("B001")).thenReturn(Optional.of(sampleBook));
        when(bookRepository.getCopies("B001")).thenReturn(2);

        int copies = bookService.getAvailableCopies("B001");

        assertEquals(2, copies);
    }

    @Test
    @DisplayName("[SUCCESS] Should update availability to false")
    void shouldUpdateAvailabilityToFalse() {
        when(bookRepository.findById("B001")).thenReturn(Optional.of(sampleBook));

        Book result = bookService.updateAvailability("B001", false);

        assertFalse(result.isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] Should update availability to true")
    void shouldUpdateAvailabilityToTrue() {
        sampleBook.setAvailable(false);
        when(bookRepository.findById("B001")).thenReturn(Optional.of(sampleBook));

        Book result = bookService.updateAvailability("B001", true);

        assertTrue(result.isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] Should decrement copies and set unavailable on last copy")
    void shouldDecrementAndSetUnavailableOnLastCopy() {
        when(bookRepository.findById("B001")).thenReturn(Optional.of(sampleBook));
        when(bookRepository.getCopies("B001")).thenReturn(1).thenReturn(0);

        bookService.decrementCopy("B001");

        verify(bookRepository).decrementCopies("B001");
        assertFalse(sampleBook.isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] Should decrement copies without marking unavailable when copies remain")
    void shouldDecrementCopiesWithoutMarkingUnavailable() {
        when(bookRepository.findById("B001")).thenReturn(Optional.of(sampleBook));
        when(bookRepository.getCopies("B001")).thenReturn(3).thenReturn(2);

        bookService.decrementCopy("B001");

        verify(bookRepository).decrementCopies("B001");
        assertTrue(sampleBook.isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] Should increment copies and restore availability")
    void shouldIncrementCopiesAndRestoreAvailability() {
        sampleBook.setAvailable(false);
        when(bookRepository.findById("B001")).thenReturn(Optional.of(sampleBook));

        bookService.incrementCopy("B001");

        verify(bookRepository).incrementCopies("B001");
        assertTrue(sampleBook.isAvailable());
    }

    // ─── ERROR ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[ERROR] Should throw IllegalArgumentException when copies is zero")
    void shouldThrowWhenCopiesIsZero() {
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(sampleBook, 0));
        verify(bookRepository, never()).save(any(), anyInt());
    }

    @Test
    @DisplayName("[ERROR] Should throw IllegalArgumentException when copies is negative")
    void shouldThrowWhenCopiesIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(sampleBook, -2));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotFoundException for unknown ID")
    void shouldThrowBookNotFoundForUnknownId() {
        when(bookRepository.findById("GHOST")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById("GHOST"));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotAvailableException when no copies left")
    void shouldThrowWhenNoCopiesLeft() {
        when(bookRepository.findById("B001")).thenReturn(Optional.of(sampleBook));
        when(bookRepository.getCopies("B001")).thenReturn(0);

        assertThrows(BookNotAvailableException.class, () -> bookService.decrementCopy("B001"));
        verify(bookRepository, never()).decrementCopies(any());
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotFoundException when updating nonexistent book")
    void shouldThrowWhenUpdatingNonexistentBook() {
        when(bookRepository.findById("GHOST")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.updateAvailability("GHOST", true));
    }
}
