package edu.eci.dosw.tdd.core.model;

import edu.eci.dosw.tdd.core.model.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Loan {

    private Book book;
    private User user;
    private LocalDate loanDate;
    private Status status;
    private LocalDate returnDate;



}
