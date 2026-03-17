package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Book operations.
 * Managed by Spring — injected via @Autowired constructor.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Adds a new book with the given number of copies.
     * If the book already exists, the copies are accumulated.
     */
    public Book addBook(Book book, int copies) {
        if (copies <= 0) {
            throw new IllegalArgumentException("Number of copies must be positive.");
        }
        bookRepository.save(book, copies);
        return book;
    }

    /**
     * Returns all books in the catalog.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Finds a book by ID or throws BookNotFoundException.
     */
    public Book getBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    /**
     * Returns the number of available copies for a book.
     */
    public int getAvailableCopies(String id) {
        getBookById(id); // validates existence
        return bookRepository.getCopies(id);
    }

    /**
     * Updates the availability flag of a book.
     */
    public Book updateAvailability(String id, boolean available) {
        Book book = getBookById(id);
        book.setAvailable(available);
        return book;
    }

    /**
     * Decrements copies when a loan is created.
     * Sets available=false when the last copy is taken.
     */
    public void decrementCopy(String id) {
        Book book = getBookById(id);
        if (bookRepository.getCopies(id) <= 0) {
            throw new BookNotAvailableException(id);
        }
        bookRepository.decrementCopies(id);
        if (bookRepository.getCopies(id) == 0) {
            book.setAvailable(false);
        }
    }

    /**
     * Increments copies when a book is returned.
     * Restores available=true.
     */
    public void incrementCopy(String id) {
        Book book = getBookById(id);
        bookRepository.incrementCopies(id);
        book.setAvailable(true);
    }
}
