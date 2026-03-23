package edu.eci.dosw.tdd.core.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends MongoRepository<Loan, String> {

    Optional<Loan> findByUserId(String userId);

    List<Loan> findByStatus(Status status);

    boolean existsByUserIdAndStatus(String userId, Status status);

}