package edu.eci.dosw.tdd.core.model;


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
public class Book {

    private Long bookId;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;
    private List<String> categories;
    private String publicationType;
    private LocalDate releaseDate;
    private String isbn;

    private Metadata metadata;

    private String availabilityStatus;
    private int borrowedCopies;
    private LocalDate registerDate;

}
