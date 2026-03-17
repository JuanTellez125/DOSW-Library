package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.model.Loan;
import org.springframework.stereotype.Component;

/**
 * Maps between the Loan domain model and LoanDTO.
 */
@Component
public class LoanMapper {

    public LoanDTO toDTO(Loan loan) {
        if (loan == null) return null;
        return new LoanDTO(
                loan.getId(),
                loan.getUserId(),
                loan.getBookId(),
                loan.getLoanDate() != null ? loan.getLoanDate().toString() : null,
                loan.getReturnDate() != null ? loan.getReturnDate().toString() : null,
                loan.isReturned()
        );
    }
}
