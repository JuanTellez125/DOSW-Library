package edu.eci.dosw.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.tdd.controller.dto.request.LoanRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.LoanResponseDTO;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.enums.Status;
import edu.eci.dosw.tdd.core.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    private LoanRequestDTO requestDTO;
    private LoanResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(loanController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        requestDTO = LoanRequestDTO.builder()
                .userId("user1")
                .bookId("book1")
                .build();

        responseDTO = LoanResponseDTO.builder()
                .userName("Juan")
                .bookTitle("Clean Code")
                .status(Status.ACTIVE)
                .loanDate(LocalDate.now())
                .build();
    }

    @Test
    void shouldCreateLoanAndReturn201() throws Exception {
        when(loanService.createLoan(any(LoanRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("Juan"))
                .andExpect(jsonPath("$.bookTitle").value("Clean Code"));
    }

    @Test
    void shouldReturn409WhenUserAlreadyHasActiveLoan() throws Exception {
        when(loanService.createLoan(any(LoanRequestDTO.class)))
                .thenThrow(new LoanLimitExceededException("El usuario ya tiene un préstamo activo"));

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldUpdateLoanAndReturn200() throws Exception {
        when(loanService.updateLoan(eq("user1"), any(LoanRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/loans/user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Juan"));
    }

    @Test
    void shouldFindActiveLoansAndReturn200() throws Exception {
        when(loanService.findActiveLoans()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/loans/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void shouldFindLoanHistoryAndReturn200() throws Exception {
        when(loanService.findLoanHistory()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/loans/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

}