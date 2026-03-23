package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.controller.dto.request.BookRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.BookResponseDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    public BookResponseDTO createBook(BookRequestDTO dto) {
        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Transactional
    public BookResponseDTO updateBook(String title, BookRequestDTO dto) {
        Book book = bookRepository.findByTitle(title)
                .orElseThrow(() -> new BookNotAvailableException("Libro no encontrado: " + title));

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    @Transactional
    public void deleteBook(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotAvailableException("Libro no encontrado con id: " + id));

        bookRepository.delete(book);
    }

    public List<BookResponseDTO> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
