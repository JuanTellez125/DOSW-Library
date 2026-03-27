package edu.eci.dosw.tdd.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {

    private String id;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

}