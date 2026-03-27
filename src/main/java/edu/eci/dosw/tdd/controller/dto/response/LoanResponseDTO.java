package edu.eci.dosw.tdd.controller.dto.response;

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

    private String userName;
    private String bookTitle;
    private String bookAuthor;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private Status status;

}