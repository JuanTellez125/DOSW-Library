package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.service.BookService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookService using Mockito to mock BookRepository.
 * Spring context is NOT loaded — pure unit tests.
 *
 * Scenarios covered:
 *  SUCCESS: addBook, getAllBooks, getBookById, getAvailableCopies,
 *           updateAvailability, decrementCopy (with last-copy case),
 *           incrementCopy
 *  ERROR:   addBook with zero/negative copies, getBookById nonexistent,
 *           decrementCopy with no copies left
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Unit Tests (Mockito)")
class BookServiceTest {

}
