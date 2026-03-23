package edu.eci.dosw.tdd.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookRequestDTO {

    @NotNull(message = "El titulo es obligatorio")
    private String title;

    @NotNull(message = "El autor es obligatorio")
    private String author;

    @NotNull(message = "ID cannot be blank")
    public String id;
}
