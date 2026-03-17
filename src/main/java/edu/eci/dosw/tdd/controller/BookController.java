package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Book operations.
 *
 * Endpoints:
 *   POST   /api/books                      → add book
 *   GET    /api/books                      → get all books
 *   GET    /api/books/{id}                 → get book by ID
 *   PUT    /api/books/{id}/availability    → update availability
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @Autowired
    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO dto) {
        Book book = bookMapper.toModel(dto);
        bookService.addBook(book, dto.getCopies());
        BookDTO response = bookMapper.toDTO(book, bookService.getAvailableCopies(book.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks().stream()
                .map(b -> bookMapper.toDTO(b, bookService.getAvailableCopies(b.getId())))
                .toList();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable String id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(bookMapper.toDTO(book, bookService.getAvailableCopies(id)));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<BookDTO> updateAvailability(
            @PathVariable String id,
            @RequestParam boolean available) {
        Book book = bookService.updateAvailability(id, available);
        return ResponseEntity.ok(bookMapper.toDTO(book, bookService.getAvailableCopies(id)));
    }
}
