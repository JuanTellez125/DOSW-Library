package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.service.LoanService;

import java.util.List;

/**
 * Controller layer for Loan operations.
 */
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
        this.loanMapper = new LoanMapper();
    }

    public LoanDTO borrowBook(String userId, String bookId) {
        Loan loan = loanService.borrowBook(userId, bookId);
        return loanMapper.toDTO(loan);
    }

    public LoanDTO returnBook(String loanId) {
        Loan loan = loanService.returnBook(loanId);
        return loanMapper.toDTO(loan);
    }

    public List<LoanDTO> getAllLoans() {
        return loanService.getAllLoans().stream()
                .map(loanMapper::toDTO)
                .toList();
    }

    public List<LoanDTO> getActiveLoans() {
        return loanService.getActiveLoans().stream()
                .map(loanMapper::toDTO)
                .toList();
    }

    public List<LoanDTO> getLoansByUser(String userId) {
        return loanService.getLoansByUser(userId).stream()
                .map(loanMapper::toDTO)
                .toList();
    }
}
