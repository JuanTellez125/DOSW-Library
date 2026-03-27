package edu.eci.dosw.tdd.controller.dto.response;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDTO {

    private String bookId;
    private String bookTitle;
    private String userId;
    private String username;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private Status status;

}