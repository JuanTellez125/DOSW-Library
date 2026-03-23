package edu.eci.dosw.tdd.core.model;

import com.sun.source.tree.LambdaExpressionTree;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
@Data
@Builder
public class Book {

    private String title;
    private String author;

    @Id
    private String ID;

}
