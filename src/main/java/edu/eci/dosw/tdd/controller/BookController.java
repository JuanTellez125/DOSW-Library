package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;

import java.util.List;

/**
 * Controller layer for Book operations.
 */
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService) {
        this.bookService = bookService;
        this.bookMapper = new BookMapper();
    }

    public void addBook(BookDTO dto) {
        Book book = bookMapper.toModel(dto);
        bookService.addBook(book, dto.getCopies());
    }

    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(b -> bookMapper.toDTO(b, bookService.getAvailableCopies(b.getId())))
                .toList();
    }

    public BookDTO getBookById(String id) {
        Book book = bookService.getBookById(id);
        return bookMapper.toDTO(book, bookService.getAvailableCopies(id));
    }

    public void updateAvailability(String bookId, boolean available) {
        bookService.updateAvailability(bookId, available);
    }
}
