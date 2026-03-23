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

}
