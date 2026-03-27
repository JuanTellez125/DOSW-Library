package edu.eci.dosw.tdd.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookRequestDTO {

    @NotNull
    private String id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private int totalCopies;

    @NotNull
    private int availableCopies;
}
