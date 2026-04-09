package edu.eci.dosw.tdd.controller.dto.request;

import edu.eci.dosw.tdd.core.model.enums.BookType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {

    @NotNull(message = "Book's title cannot be blank")
    private String title;
    private String author;
    private String email;
    private String category;
    private BookType type;
    private LocalDate releaseDate;
    private String isbn;
    private LocalDate registerDate;

}
