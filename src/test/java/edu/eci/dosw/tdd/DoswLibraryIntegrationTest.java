package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full integration tests that boot the complete Spring context on a random port.
 * Uses TestRestTemplate to make real HTTP calls through every layer.
 *
 * The in-memory repositories are cleared before each test via @BeforeEach
 * to guarantee test isolation — each test starts with a clean state.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integration Tests — Full Spring Context")
class DoswLibraryIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private BookRepository bookRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private LoanRepository loanRepository;

    @BeforeEach
    void clearRepositories() {
        loanRepository.clear();
        bookRepository.clear();
        userRepository.clear();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    // ─── FULL LIFECYCLE ────────────────────────────────────────────────────────

    @Test
    @DisplayName("[INTEGRATION] Full lifecycle: add book → register user → borrow → return")
    void fullLoanLifecycle() {
        BookDTO book = new BookDTO("B001", "Refactoring", "Fowler", "ISBN-RF", true, 1);
        ResponseEntity<BookDTO> bookResp = restTemplate.postForEntity(url("/api/books"), book, BookDTO.class);
        assertEquals(HttpStatus.CREATED, bookResp.getStatusCode());
        assertEquals("B001", bookResp.getBody().getId());

        UserDTO user = new UserDTO("U001", "Carlos Pérez", "carlos@dosw.edu", 0);
        assertEquals(HttpStatus.CREATED, restTemplate.postForEntity(url("/api/users"), user, UserDTO.class).getStatusCode());

        ResponseEntity<LoanDTO> loanResp = restTemplate.postForEntity(
                url("/api/loans?userId=U001&bookId=B001"), null, LoanDTO.class);
        assertEquals(HttpStatus.CREATED, loanResp.getStatusCode());
        String loanId = loanResp.getBody().getId();
        assertFalse(loanResp.getBody().isReturned());

        LoanDTO[] active = restTemplate.getForEntity(url("/api/loans/active"), LoanDTO[].class).getBody();
        assertEquals(1, active.length);

        restTemplate.put(url("/api/loans/" + loanId + "/return"), null);

        LoanDTO[] activeAfter = restTemplate.getForEntity(url("/api/loans/active"), LoanDTO[].class).getBody();
        assertEquals(0, activeAfter.length);
    }

    // ─── 404 SCENARIOS ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("[INTEGRATION] GET /api/books/{id} unknown → 404")
    void shouldReturn404ForUnknownBook() {
        assertEquals(HttpStatus.NOT_FOUND,
                restTemplate.getForEntity(url("/api/books/UNKNOWN"), String.class).getStatusCode());
    }

    @Test
    @DisplayName("[INTEGRATION] GET /api/users/{id} unknown → 404")
    void shouldReturn404ForUnknownUser() {
        assertEquals(HttpStatus.NOT_FOUND,
                restTemplate.getForEntity(url("/api/users/UNKNOWN"), String.class).getStatusCode());
    }

    @Test
    @DisplayName("[INTEGRATION] Borrow with nonexistent user → 404")
    void shouldReturn404WhenBorrowingWithNonexistentUser() {
        restTemplate.postForEntity(url("/api/books"),
                new BookDTO("B002", "DDD", "Evans", "ISBN-DDD", true, 1), BookDTO.class);
        assertEquals(HttpStatus.NOT_FOUND,
                restTemplate.postForEntity(url("/api/loans?userId=NO-USER&bookId=B002"), null, String.class).getStatusCode());
    }

    // ─── CONFLICT / LIMIT SCENARIOS ────────────────────────────────────────────

    @Test
    @DisplayName("[INTEGRATION] Borrow when no copies left → 409")
    void shouldReturn409WhenNoCopiesLeft() {
        restTemplate.postForEntity(url("/api/books"),
                new BookDTO("B003", "SICP", "Abelson", "ISBN-SICP", true, 1), BookDTO.class);
        restTemplate.postForEntity(url("/api/users"),
                new UserDTO("U002", "Diana", "diana@dosw.edu", 0), UserDTO.class);
        restTemplate.postForEntity(url("/api/users"),
                new UserDTO("U003", "Ernesto", "ernesto@dosw.edu", 0), UserDTO.class);

        restTemplate.postForEntity(url("/api/loans?userId=U002&bookId=B003"), null, String.class);

        assertEquals(HttpStatus.CONFLICT,
                restTemplate.postForEntity(url("/api/loans?userId=U003&bookId=B003"), null, String.class).getStatusCode());
    }

    @Test
    @DisplayName("[INTEGRATION] 4th loan when user has 3 → 422")
    void shouldReturn422WhenLoanLimitExceeded() {
        for (int i = 1; i <= 4; i++) {
            restTemplate.postForEntity(url("/api/books"),
                    new BookDTO("LB00" + i, "Book" + i, "Author", "ISBN-" + i, true, 1), BookDTO.class);
        }
        restTemplate.postForEntity(url("/api/users"),
                new UserDTO("LU001", "Fernando", "fernando@dosw.edu", 0), UserDTO.class);
        for (int i = 1; i <= 3; i++) {
            restTemplate.postForEntity(url("/api/loans?userId=LU001&bookId=LB00" + i), null, String.class);
        }
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,
                restTemplate.postForEntity(url("/api/loans?userId=LU001&bookId=LB004"), null, String.class).getStatusCode());
    }

    // ─── VALIDATION ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[INTEGRATION] POST /api/books invalid data → 400")
    void shouldReturn400ForInvalidBook() {
        assertEquals(HttpStatus.BAD_REQUEST,
                restTemplate.postForEntity(url("/api/books"),
                        new BookDTO("", "", "", "", true, 0), String.class).getStatusCode());
    }

    @Test
    @DisplayName("[INTEGRATION] POST /api/users invalid email → 400")
    void shouldReturn400ForInvalidUserEmail() {
        assertEquals(HttpStatus.BAD_REQUEST,
                restTemplate.postForEntity(url("/api/users"),
                        new UserDTO("U999", "Test", "bad-email", 0), String.class).getStatusCode());
    }
}
