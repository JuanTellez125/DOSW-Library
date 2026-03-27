package edu.eci.dosw.tdd.persistence.repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, String> {

    Optional<Book> findByTitle(String title);
    Optional<Book> findByAuthor(String author);

}
