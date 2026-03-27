package edu.eci.dosw.tdd.controller.dto.request;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.model.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class LoanRequestDTO {

    private String bookId;
    private String bookTitle;
    private String userId;
    private String username;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private Status status;
}
