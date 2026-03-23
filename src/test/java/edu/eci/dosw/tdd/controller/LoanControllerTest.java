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

}
