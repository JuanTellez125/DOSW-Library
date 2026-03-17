package edu.eci.dosw.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.GlobalExceptionHandler;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller-layer tests for BookController using @WebMvcTest.
 * Only the web slice is loaded — BookService is mocked with @MockBean.
 *
 * Scenarios covered:
 *  SUCCESS: POST /api/books (201), GET /api/books (200),
 *           GET /api/books/{id} (200), PUT /api/books/{id}/availability (200)
 *  ERROR:   POST with blank fields (400), GET nonexistent book (404),
 *           PUT nonexistent book (404), POST with missing copies (400)
 */
@WebMvcTest(BookController.class)
@Import({BookMapper.class, GlobalExceptionHandler.class})
@DisplayName("BookController Web Tests (MockMvc)")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    // ─── SUCCESS ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[SUCCESS] POST /api/books → 201 Created")
    void shouldCreateBook() throws Exception {
        BookDTO dto = new BookDTO("B001", "Clean Code", "R. Martin", "ISBN-1", true, 3);
        Book book = new Book("B001", "Clean Code", "R. Martin", "ISBN-1");

        when(bookService.addBook(any(), eq(3))).thenReturn(book);
        when(bookService.getAvailableCopies("B001")).thenReturn(3);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("B001"))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.copies").value(3));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/books → 200 with list")
    void shouldGetAllBooks() throws Exception {
        Book book = new Book("B001", "Clean Code", "R. Martin", "ISBN-1");

        when(bookService.getAllBooks()).thenReturn(List.of(book));
        when(bookService.getAvailableCopies("B001")).thenReturn(2);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("B001"));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/books → 200 with empty list")
    void shouldReturnEmptyList() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/books/{id} → 200 with book")
    void shouldGetBookById() throws Exception {
        Book book = new Book("B001", "Clean Code", "R. Martin", "ISBN-1");

        when(bookService.getBookById("B001")).thenReturn(book);
        when(bookService.getAvailableCopies("B001")).thenReturn(2);

        mockMvc.perform(get("/api/books/B001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("B001"))
                .andExpect(jsonPath("$.author").value("R. Martin"))
                .andExpect(jsonPath("$.copies").value(2));
    }

    @Test
    @DisplayName("[SUCCESS] PUT /api/books/{id}/availability → 200")
    void shouldUpdateAvailability() throws Exception {
        Book book = new Book("B001", "Clean Code", "R. Martin", "ISBN-1");
        book.setAvailable(false);

        when(bookService.updateAvailability("B001", false)).thenReturn(book);
        when(bookService.getAvailableCopies("B001")).thenReturn(0);

        mockMvc.perform(put("/api/books/B001/availability")
                        .param("available", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    // ─── ERROR ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[ERROR] POST /api/books with blank title → 400")
    void shouldReturn400ForBlankTitle() throws Exception {
        BookDTO dto = new BookDTO("B001", "", "R. Martin", "ISBN-1", true, 3);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("[ERROR] POST /api/books with null copies → 400")
    void shouldReturn400ForNullCopies() throws Exception {
        BookDTO dto = new BookDTO("B001", "Title", "Author", "ISBN", true, null);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("[ERROR] POST /api/books with copies=0 → 400")
    void shouldReturn400ForZeroCopies() throws Exception {
        BookDTO dto = new BookDTO("B001", "Title", "Author", "ISBN", true, 0);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("[ERROR] GET /api/books/{id} nonexistent → 404")
    void shouldReturn404ForUnknownBook() throws Exception {
        when(bookService.getBookById("GHOST")).thenThrow(new BookNotFoundException("GHOST"));

        mockMvc.perform(get("/api/books/GHOST"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("BOOK_NOT_FOUND"));
    }

    @Test
    @DisplayName("[ERROR] PUT /api/books/{id}/availability nonexistent → 404")
    void shouldReturn404WhenUpdatingNonexistentBook() throws Exception {
        when(bookService.updateAvailability("GHOST", true)).thenThrow(new BookNotFoundException("GHOST"));

        mockMvc.perform(put("/api/books/GHOST/availability")
                        .param("available", "true"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("BOOK_NOT_FOUND"));
    }
}
