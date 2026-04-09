package edu.eci.dosw.tdd.persistence.relational.entity;

import edu.eci.dosw.tdd.core.model.enums.BookType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class BookRelationalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;
    private String author;
    private String category;

    @Enumerated(EnumType.STRING)
    private BookType type;

    private LocalDate releaseDate;
    private String isbn;
    private LocalDate registerDate;

    private Integer totalCopies;
    private Integer availableCopies;
}
