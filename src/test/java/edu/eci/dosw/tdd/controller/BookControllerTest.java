package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

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
