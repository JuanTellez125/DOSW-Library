package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private final BookRepository bookRepository;

    public void validate(String title, String author) {
        ValidationUtil.requireNonNull(title, "El título");
        ValidationUtil.requireNonNull(author, "El autor");
    }

}
