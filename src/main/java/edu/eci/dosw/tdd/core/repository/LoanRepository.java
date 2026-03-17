package edu.eci.dosw.tdd.core.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory repository for loans.
 */
@Repository
public class LoanRepository {

    private final List<Loan> loans = new ArrayList<>();

    public void save(Loan loan) {
        loans.add(loan);
    }

    public List<Loan> findAll() {
        return new ArrayList<>(loans);
    }

    public List<Loan> findActive() {
        return loans.stream().filter(l -> !l.isReturned()).toList();
    }

    public List<Loan> findByUserId(String userId) {
        return loans.stream().filter(l -> l.getUserId().equals(userId)).toList();
    }

    public Optional<Loan> findActiveLoanById(String loanId) {
        return loans.stream()
                .filter(l -> l.getId().equals(loanId) && !l.isReturned())
                .findFirst();
    }

    public void clear() {
        loans.clear();
    }
}
