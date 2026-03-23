package edu.eci.dosw.tdd.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookRequestDTO {

    @NotNull(message = "El titulo es obligatorio")
    private String title;

    @NotNull(message = "El autor es obligatorio")
    private String author;

}
