package edu.eci.dosw.tdd.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class LoanRequestDTO {

    @NotNull(message = "The user ID cannot be blank")
    private String userId;

    @NotNull(message = "The book id cannot be blank")
    private String bookId;
}
