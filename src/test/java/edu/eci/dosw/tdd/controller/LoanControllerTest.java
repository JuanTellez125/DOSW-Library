package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.exception.*;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.service.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller-layer tests for LoanController using @WebMvcTest.
 *
 * Scenarios covered:
 *  SUCCESS: POST /api/loans (201), PUT /api/loans/{id}/return (200),
 *           GET /api/loans (200), GET /api/loans/active (200),
 *           GET /api/loans/user/{userId} (200), empty list cases
 *  ERROR:   user not found (404), book not available (409),
 *           loan limit exceeded (422), loan not found (400),
 *           already returned (400)
 */
@WebMvcTest(LoanController.class)
@Import({LoanMapper.class, GlobalExceptionHandler.class})
@DisplayName("LoanController Web Tests (MockMvc)")
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    // ─── SUCCESS ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[SUCCESS] POST /api/loans → 201 Created")
    void shouldBorrowBook() throws Exception {
        Loan loan = new Loan("LOAN-001", "U001", "B001");
        when(loanService.borrowBook("U001", "B001")).thenReturn(loan);

        mockMvc.perform(post("/api/loans")
                        .param("userId", "U001")
                        .param("bookId", "B001"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("LOAN-001"))
                .andExpect(jsonPath("$.userId").value("U001"))
                .andExpect(jsonPath("$.bookId").value("B001"))
                .andExpect(jsonPath("$.returned").value(false));
    }

    @Test
    @DisplayName("[SUCCESS] PUT /api/loans/{id}/return → 200")
    void shouldReturnBook() throws Exception {
        Loan loan = new Loan("LOAN-001", "U001", "B001");
        loan.setReturned(true);
        when(loanService.returnBook("LOAN-001")).thenReturn(loan);

        mockMvc.perform(put("/api/loans/LOAN-001/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returned").value(true));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/loans → 200 with all loans")
    void shouldGetAllLoans() throws Exception {
        Loan loan = new Loan("LOAN-001", "U001", "B001");
        when(loanService.getAllLoans()).thenReturn(List.of(loan));

        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("LOAN-001"));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/loans → 200 empty list")
    void shouldReturnEmptyAllLoans() throws Exception {
        when(loanService.getAllLoans()).thenReturn(List.of());

        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/loans/active → 200 with active loans")
    void shouldGetActiveLoans() throws Exception {
        Loan loan = new Loan("LOAN-001", "U001", "B001");
        when(loanService.getActiveLoans()).thenReturn(List.of(loan));

        mockMvc.perform(get("/api/loans/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/loans/active → 200 empty list")
    void shouldReturnEmptyActiveLoans() throws Exception {
        when(loanService.getActiveLoans()).thenReturn(List.of());

        mockMvc.perform(get("/api/loans/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/loans/user/{userId} → 200 with user loans")
    void shouldGetLoansByUser() throws Exception {
        Loan loan = new Loan("LOAN-001", "U001", "B001");
        when(loanService.getLoansByUser("U001")).thenReturn(List.of(loan));

        mockMvc.perform(get("/api/loans/user/U001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value("U001"));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/loans/user/{userId} → 200 empty list")
    void shouldReturnEmptyForUserWithNoLoans() throws Exception {
        when(loanService.getLoansByUser("U002")).thenReturn(List.of());

        mockMvc.perform(get("/api/loans/user/U002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ─── ERROR ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[ERROR] POST /api/loans with nonexistent user → 404")
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(loanService.borrowBook("GHOST", "B001"))
                .thenThrow(new UserNotFoundException("GHOST"));

        mockMvc.perform(post("/api/loans")
                        .param("userId", "GHOST")
                        .param("bookId", "B001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
    }

    @Test
    @DisplayName("[ERROR] POST /api/loans with nonexistent book → 404")
    void shouldReturn404WhenBookNotFound() throws Exception {
        when(loanService.borrowBook("U001", "GHOST"))
                .thenThrow(new BookNotFoundException("GHOST"));

        mockMvc.perform(post("/api/loans")
                        .param("userId", "U001")
                        .param("bookId", "GHOST"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("BOOK_NOT_FOUND"));
    }

    @Test
    @DisplayName("[ERROR] POST /api/loans with no copies available → 409")
    void shouldReturn409WhenBookNotAvailable() throws Exception {
        when(loanService.borrowBook("U001", "B001"))
                .thenThrow(new BookNotAvailableException("B001"));

        mockMvc.perform(post("/api/loans")
                        .param("userId", "U001")
                        .param("bookId", "B001"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("BOOK_NOT_AVAILABLE"));
    }

    @Test
    @DisplayName("[ERROR] POST /api/loans when user at loan limit → 422")
    void shouldReturn422WhenLoanLimitExceeded() throws Exception {
        when(loanService.borrowBook("U001", "B001"))
                .thenThrow(new LoanLimitExceededException("U001"));

        mockMvc.perform(post("/api/loans")
                        .param("userId", "U001")
                        .param("bookId", "B001"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value("LOAN_LIMIT_EXCEEDED"));
    }

    @Test
    @DisplayName("[ERROR] PUT /api/loans/{id}/return with unknown loan → 400")
    void shouldReturn400WhenLoanNotFound() throws Exception {
        when(loanService.returnBook("GHOST"))
                .thenThrow(new IllegalArgumentException("Active loan with id 'GHOST' not found."));

        mockMvc.perform(put("/api/loans/GHOST/return"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("[ERROR] GET /api/loans/user/{userId} with nonexistent user → 404")
    void shouldReturn404WhenGetLoansByNonexistentUser() throws Exception {
        when(loanService.getLoansByUser("GHOST"))
                .thenThrow(new UserNotFoundException("GHOST"));

        mockMvc.perform(get("/api/loans/user/GHOST"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
    }
}
