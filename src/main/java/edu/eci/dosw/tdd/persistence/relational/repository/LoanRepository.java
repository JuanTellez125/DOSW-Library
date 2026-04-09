package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.enums.Status;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanRelationalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<LoanRelationalEntity, Long> {

    Optional<Loan> findByUserId(Long userId);

    List<LoanRelationalEntity> findByStatus(Status status);

    boolean existsByUserIdAndStatus(Long userId, Status status);

}