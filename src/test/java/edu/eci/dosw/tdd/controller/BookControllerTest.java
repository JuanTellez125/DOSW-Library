package edu.eci.dosw.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.tdd.controller.dto.request.BookRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.BookResponseDTO;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookRequestDTO requestDTO;
    private BookResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookController)
                .setControllerAdvice(new edu.eci.dosw.tdd.core.exception.GlobalExceptionHandler())
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
    void shouldCreateBookAndReturn201() throws Exception {
        when(bookService.createBook(any(BookRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert Martin"));
    }

    @Test
    void shouldUpdateBookAndReturn200() throws Exception {
        when(bookService.updateBook(eq("Clean Code"), any(BookRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/books/Clean Code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void shouldReturn404WhenBookNotFoundOnUpdate() throws Exception {
        when(bookService.updateBook(anyString(), any(BookRequestDTO.class)))
                .thenThrow(new BookNotFoundException("Libro no encontrado"));

        mockMvc.perform(put("/api/books/NoExiste")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteBookAndReturn204() throws Exception {
        doNothing().when(bookService).deleteBook("1");

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenBookNotFoundOnDelete() throws Exception {
        doThrow(new BookNotFoundException("Libro no encontrado"))
                .when(bookService).deleteBook("1");

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindAllBooksAndReturn200() throws Exception {
        when(bookService.findAllBooks()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

}