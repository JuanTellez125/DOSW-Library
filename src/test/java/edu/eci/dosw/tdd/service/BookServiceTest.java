package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.controller.dto.request.BookRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.BookResponseDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.relational.repository.BookRepository;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookValidator bookValidator;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookRequestDTO requestDTO;
    private BookResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id("1")
                .title("Clean Code")
                .author("Robert Martin")
                .build();

        requestDTO = BookRequestDTO.builder()
                .title("Clean Code")
                .author("Robert Martin")
                .build();

        responseDTO = BookResponseDTO.builder()
                .id("1")
                .title("Clean Code")
                .author("Robert Martin")
                .build();
    }

    @Test
    void shouldCreateBookSuccessfully() {
        when(bookMapper.toEntity(requestDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(responseDTO);

        BookResponseDTO result = bookService.createBook(requestDTO);

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert Martin", result.getAuthor());
        verify(bookValidator).validate(requestDTO.getTitle(), requestDTO.getAuthor());
        verify(bookRepository).save(book);
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        when(bookRepository.findByTitle("Clean Code")).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(responseDTO);

        BookResponseDTO result = bookService.updateBook("Clean Code", requestDTO);

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        verify(bookRepository).save(book);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundOnUpdate() {
        when(bookRepository.findByTitle("Clean Code")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.updateBook("Clean Code", requestDTO));

        verify(bookRepository, never()).save(any());
    }

    @Test
    void shouldDeleteBookSuccessfully() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(book));

        bookService.deleteBook("1");

        verify(bookRepository).delete(book);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundOnDelete() {
        when(bookRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.deleteBook("1"));

        verify(bookRepository, never()).delete(any());
    }

    @Test
    void shouldFindAllBooksSuccessfully() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(responseDTO);

        List<BookResponseDTO> result = bookService.findAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
    }

}