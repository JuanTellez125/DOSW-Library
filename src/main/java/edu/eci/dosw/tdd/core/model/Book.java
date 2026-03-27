package edu.eci.dosw.tdd.core.model;

import com.sun.source.tree.LambdaExpressionTree;
import edu.eci.dosw.tdd.core.model.enums.BookType;
import edu.eci.dosw.tdd.core.model.enums.MemberShip;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "books")
@Data
@Builder
public class Book {

    private String title;
    private String author;

    @Id
    private String id;

    private String email;

    private String category;

    private BookType type;

    private LocalDate releaseDate;

    private String isbn;

    private MemberShip memberShip;

    private LocalDate registerDate;

}
