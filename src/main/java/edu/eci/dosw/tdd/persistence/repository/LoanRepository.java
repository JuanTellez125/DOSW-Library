package edu.eci.dosw.tdd.persistence.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.enums.Status;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<LoanEntity, String> {

    Optional<Loan> findByUserId(String userId);

    List<Loan> findByStatus(Status status);

    boolean existsByUserIdAndStatus(String userId, Status status);

}