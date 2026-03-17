package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Loan operations.
 *
 * Endpoints:
 *   POST  /api/loans                      → borrow a book
 *   PUT   /api/loans/{loanId}/return      → return a book
 *   GET   /api/loans                      → get all loans
 *   GET   /api/loans/active               → get active loans
 *   GET   /api/loans/user/{userId}        → get loans by user
 */
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    @Autowired
    public LoanController(LoanService loanService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }

    @PostMapping
    public ResponseEntity<LoanDTO> borrowBook(
            @RequestParam String userId,
            @RequestParam String bookId) {
        Loan loan = loanService.borrowBook(userId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(loanMapper.toDTO(loan));
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable String loanId) {
        Loan loan = loanService.returnBook(loanId);
        return ResponseEntity.ok(loanMapper.toDTO(loan));
    }

    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loans = loanService.getAllLoans().stream()
                .map(loanMapper::toDTO)
                .toList();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/active")
    public ResponseEntity<List<LoanDTO>> getActiveLoans() {
        List<LoanDTO> loans = loanService.getActiveLoans().stream()
                .map(loanMapper::toDTO)
                .toList();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanDTO>> getLoansByUser(@PathVariable String userId) {
        List<LoanDTO> loans = loanService.getLoansByUser(userId).stream()
                .map(loanMapper::toDTO)
                .toList();
        return ResponseEntity.ok(loans);
    }
}
