package edu.eci.dosw.tdd.controller.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponseDTO {

    private String id;
    private String title;
    private String author;
}
