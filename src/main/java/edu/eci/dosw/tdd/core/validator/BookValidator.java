package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.ValidationUtil;

/**
 * Validator for Book entities.
 */
public class BookValidator {

    public void validate(Book book) {
        ValidationUtil.requireNonNull(book, "book");
        ValidationUtil.requireNonBlank(book.getId(), "book.id");
        ValidationUtil.requireNonBlank(book.getTitle(), "book.title");
        ValidationUtil.requireNonBlank(book.getAuthor(), "book.author");
        ValidationUtil.requireNonBlank(book.getIsbn(), "book.isbn");
    }

    public void validateId(String id) {
        ValidationUtil.requireNonBlank(id, "bookId");
    }
}
