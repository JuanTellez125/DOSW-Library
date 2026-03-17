package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;

/**
 * Maps between Book domain model and BookDTO.
 */
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
        return new Book(dto.getId(), dto.getTitle(), dto.getAuthor(), dto.getIsbn());
    }
}
