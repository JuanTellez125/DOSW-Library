package edu.eci.dosw.tdd.core.repository;

import edu.eci.dosw.tdd.core.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book,String> {

    Optional<Book> findByTitle(String title);
    Optional<Book> findByAuthor(String author);

}
