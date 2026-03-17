package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.validator.BookValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookService {

    // Map: Book -> number of available copies
    private final Map<Book, Integer> bookCatalog;
    private final Map<String, Book> bookById;
    private final BookValidator validator;

    public BookService() {
        this.bookCatalog = new HashMap<>();
        this.bookById = new HashMap<>();
        this.validator = new BookValidator();
    }

    /**
     * Adds a book to the catalog with a given number of copies.
     */
    public void addBook(Book book, int copies) {
        validator.validate(book);
        if (copies <= 0) {
            throw new IllegalArgumentException("Number of copies must be positive.");
        }
        if (bookById.containsKey(book.getId())) {
            // Increment copies if already exists
            Book existing = bookById.get(book.getId());
            bookCatalog.put(existing, bookCatalog.get(existing) + copies);
        } else {
            bookCatalog.put(book, copies);
            bookById.put(book.getId(), book);
        }
    }

    /**
     * Returns all books in the catalog.
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(bookCatalog.keySet());
    }

    /**
     * Finds a book by its ID.
     */
    public Book getBookById(String id) {
        validator.validateId(id);
        Book book = bookById.get(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }

    /**
     * Updates the availability of a book.
     */
    public void updateAvailability(String bookId, boolean available) {
        Book book = getBookById(bookId);
        book.setAvailable(available);
    }

    /**
     * Returns the number of available copies for a book.
     */
    public int getAvailableCopies(String bookId) {
        Book book = getBookById(bookId);
        return bookCatalog.getOrDefault(book, 0);
    }

    /**
     * Decrements available copies when a loan is made.
     */
    public void decrementCopy(String bookId) {
        Book book = getBookById(bookId);
        int copies = bookCatalog.getOrDefault(book, 0);
        if (copies <= 0) {
            throw new edu.eci.dosw.tdd.core.exception.BookNotAvailableException(bookId);
        }
        bookCatalog.put(book, copies - 1);
        if (copies - 1 == 0) {
            book.setAvailable(false);
        }
    }

    /**
     * Increments available copies when a book is returned.
     */
    public void incrementCopy(String bookId) {
        Book book = getBookById(bookId);
        int copies = bookCatalog.getOrDefault(book, 0);
        bookCatalog.put(book, copies + 1);
        book.setAvailable(true);
    }
}
