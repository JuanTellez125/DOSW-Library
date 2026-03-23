package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.request.LoanRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.LoanResponseDTO;
import edu.eci.dosw.tdd.core.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody @Valid LoanRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.createLoan(dto));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<LoanResponseDTO> updateLoan(@PathVariable String userId,
                                                      @RequestBody @Valid LoanRequestDTO dto) {
        return ResponseEntity.ok(loanService.updateLoan(userId, dto));
    }

    @GetMapping("/active")
    public ResponseEntity<List<LoanResponseDTO>> findActiveLoans() {
        return ResponseEntity.ok(loanService.findActiveLoans());
    }

    @GetMapping("/history")
    public ResponseEntity<List<LoanResponseDTO>> findLoanHistory() {
        return ResponseEntity.ok(loanService.findLoanHistory());
    }

}