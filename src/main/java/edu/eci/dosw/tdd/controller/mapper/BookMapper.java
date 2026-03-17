package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;
import org.springframework.stereotype.Component;

/**
 * Maps between the Book domain model and BookDTO.
 * Registered as a Spring bean via @Component so it can be @Autowired.
 */
@Component
public class BookMapper {

    public BookDTO toDTO(Book book, int copies) {
        if (book == null) return null;
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.isAvailable(),
                copies
        );
    }

    public Book toModel(BookDTO dto) {
        if (dto == null) return null;
        Book book = new Book(dto.getId(), dto.getTitle(), dto.getAuthor(), dto.getIsbn());
        book.setAvailable(dto.isAvailable());
        return book;
    }
}
