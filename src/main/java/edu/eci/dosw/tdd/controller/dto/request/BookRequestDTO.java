package edu.eci.dosw.tdd.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {

    @NotNull(message = "Title no puede estar vacio")
    private String title;

    @NotNull(message = "Author no puede estar vacio")
    private String author;

    @NotNull(message = "Total copies no puede estar vacio")
    private int totalCopies;

    private List<String> categories;
    private String publicationType;
    private LocalDate releaseDate;
    private String isbn;
    private MetadataRequestDTO metadata;

}
