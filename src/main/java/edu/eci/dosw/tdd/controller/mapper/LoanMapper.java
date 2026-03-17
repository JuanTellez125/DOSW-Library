package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.util.DateUtil;

/**
 * Maps between Loan domain model and LoanDTO.
 */
public class LoanMapper {

    public LoanDTO toDTO(Loan loan) {
        if (loan == null) return null;
        return new LoanDTO(
                loan.getId(),
                loan.getUserId(),
                loan.getBookId(),
                DateUtil.format(loan.getLoanDate()),
                DateUtil.format(loan.getReturnDate()),
                loan.isReturned()
        );
    }
}
