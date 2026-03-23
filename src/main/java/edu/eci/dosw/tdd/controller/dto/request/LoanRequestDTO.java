package edu.eci.dosw.tdd.controller.dto.request;


import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanRequestDTO {

    @NotNull(message = "User cannot be blank")
    private User user;

    @NotNull(message = "Book cannot be blank")
    private Book book;

    @NotNull(message = "LoanDate cannot be blank")
    private LocalDate loanDate;

    @NotNull(message = "Status cannot be blank")
    private Status status;


    private LocalDate returnDate;
}
