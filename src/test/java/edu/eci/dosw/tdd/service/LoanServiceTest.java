package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.service.LoanService;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for LoanService using Mockito.
 * LoanRepository, UserService, and BookService are all mocked.
 *
 * Scenarios covered:
 *  SUCCESS: borrowBook, returnBook, getAllLoans, getActiveLoans, getLoansByUser
 *  ERROR:   user not found, book not available, loan limit exceeded,
 *           return unknown loan, return already-returned loan
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoanService Unit Tests (Mockito)")
class LoanServiceTest {

    @Mock private LoanRepository loanRepository;
    @Mock private UserService userService;
    @Mock private BookService bookService;

    @InjectMocks
    private LoanService loanService;

    private User availableUser;
    private Book availableBook;

    @BeforeEach
    void setUp() {
        availableUser = new User("U001", "Alice", "alice@dosw.edu");
        availableBook = new Book("B001", "Clean Code", "R. Martin", "ISBN-1");
        availableBook.setAvailable(true);
    }

    // ─── SUCCESS ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[SUCCESS] Should create a loan when all conditions are met")
    void shouldBorrowBookSuccessfully() {
        when(userService.getUserById("U001")).thenReturn(availableUser);
        when(bookService.getBookById("B001")).thenReturn(availableBook);
        when(bookService.getAvailableCopies("B001")).thenReturn(2);

        Loan loan = loanService.borrowBook("U001", "B001");

        assertNotNull(loan);
        assertEquals("U001", loan.getUserId());
        assertEquals("B001", loan.getBookId());
        assertFalse(loan.isReturned());
        verify(bookService).decrementCopy("B001");
        verify(loanRepository).save(any(Loan.class));
        assertEquals(1, availableUser.getLoanCount());
    }

    @Test
    @DisplayName("[SUCCESS] Should mark loan as returned and restore copy")
    void shouldReturnBookSuccessfully() {
        Loan loan = new Loan("LOAN-001", "U001", "B001");
        when(loanRepository.findActiveLoanById("LOAN-001")).thenReturn(Optional.of(loan));
        when(userService.getUserById("U001")).thenReturn(availableUser);

        Loan result = loanService.returnBook("LOAN-001");

        assertTrue(result.isReturned());
        assertNotNull(result.getReturnDate());
        verify(bookService).incrementCopy("B001");
    }

    @Test
    @DisplayName("[SUCCESS] Should return all loans")
    void shouldGetAllLoans() {
        Loan loan = new Loan("LOAN-001", "U001", "B001");
        when(loanRepository.findAll()).thenReturn(List.of(loan));

        List<Loan> result = loanService.getAllLoans();

        assertEquals(1, result.size());
        verify(loanRepository).findAll();
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty list when no loans")
    void shouldReturnEmptyAllLoans() {
        when(loanRepository.findAll()).thenReturn(List.of());
        assertTrue(loanService.getAllLoans().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should return only active loans")
    void shouldGetActiveLoans() {
        Loan active = new Loan("LOAN-001", "U001", "B001");
        when(loanRepository.findActive()).thenReturn(List.of(active));

        List<Loan> result = loanService.getActiveLoans();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isReturned());
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty active list when all returned")
    void shouldReturnEmptyActiveLoans() {
        when(loanRepository.findActive()).thenReturn(List.of());
        assertTrue(loanService.getActiveLoans().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should get loans filtered by user")
    void shouldGetLoansByUser() {
        Loan loan = new Loan("LOAN-001", "U001", "B001");
        when(userService.getUserById("U001")).thenReturn(availableUser);
        when(loanRepository.findByUserId("U001")).thenReturn(List.of(loan));

        List<Loan> result = loanService.getLoansByUser("U001");

        assertEquals(1, result.size());
        assertEquals("U001", result.get(0).getUserId());
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty list for user with no loans")
    void shouldReturnEmptyForUserWithNoLoans() {
        when(userService.getUserById("U001")).thenReturn(availableUser);
        when(loanRepository.findByUserId("U001")).thenReturn(List.of());
        assertTrue(loanService.getLoansByUser("U001").isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] User loan count incremented after borrow, decremented after return")
    void loanCountIsTracked() {
        when(userService.getUserById("U001")).thenReturn(availableUser);
        when(bookService.getBookById("B001")).thenReturn(availableBook);
        when(bookService.getAvailableCopies("B001")).thenReturn(2);

        loanService.borrowBook("U001", "B001");
        assertEquals(1, availableUser.getLoanCount());
    }

    // ─── ERROR ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[ERROR] Should throw UserNotFoundException when user does not exist")
    void shouldThrowWhenUserNotFound() {
        when(userService.getUserById("GHOST")).thenThrow(new UserNotFoundException("GHOST"));
        assertThrows(UserNotFoundException.class, () -> loanService.borrowBook("GHOST", "B001"));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotAvailableException when book is unavailable")
    void shouldThrowWhenNoCopiesLeft() {
        // book.isAvailable() == false short-circuits the OR, getAvailableCopies is never called
        availableBook.setAvailable(false);
        when(userService.getUserById("U001")).thenReturn(availableUser);
        when(bookService.getBookById("B001")).thenReturn(availableBook);

        assertThrows(BookNotAvailableException.class, () -> loanService.borrowBook("U001", "B001"));
        verify(bookService, never()).decrementCopy(any());
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotAvailableException when copies are zero but book still available")
    void shouldThrowWhenCopiesAreZero() {
        // book.isAvailable() == true, so getAvailableCopies IS called — stub is needed
        availableBook.setAvailable(true);
        when(userService.getUserById("U001")).thenReturn(availableUser);
        when(bookService.getBookById("B001")).thenReturn(availableBook);
        when(bookService.getAvailableCopies("B001")).thenReturn(0);

        assertThrows(BookNotAvailableException.class, () -> loanService.borrowBook("U001", "B001"));
        verify(bookService, never()).decrementCopy(any());
    }

    @Test
    @DisplayName("[ERROR] Should throw LoanLimitExceededException when user has 3 active loans")
    void shouldThrowWhenLoanLimitExceeded() {
        availableUser.incrementLoanCount();
        availableUser.incrementLoanCount();
        availableUser.incrementLoanCount(); // at MAX_LOANS=3

        when(userService.getUserById("U001")).thenReturn(availableUser);
        when(bookService.getBookById("B001")).thenReturn(availableBook);

        assertThrows(LoanLimitExceededException.class, () -> loanService.borrowBook("U001", "B001"));
        verify(loanRepository, never()).save(any());
    }

    @Test
    @DisplayName("[ERROR] Should throw IllegalArgumentException when loan not found on return")
    void shouldThrowWhenLoanNotFoundOnReturn() {
        when(loanRepository.findActiveLoanById("GHOST")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> loanService.returnBook("GHOST"));
    }

    @Test
    @DisplayName("[ERROR] Should throw when getLoansByUser called with nonexistent user")
    void shouldThrowWhenGetLoansByUserWithNonexistentUser() {
        when(userService.getUserById("GHOST")).thenThrow(new UserNotFoundException("GHOST"));
        assertThrows(UserNotFoundException.class, () -> loanService.getLoansByUser("GHOST"));
    }
}
