package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.exception.*;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.service.LoanService;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoanController.
 *
 * Scenarios covered:
 *  SUCCESS:
 *   - Borrow a book returns a LoanDTO with correct fields
 *   - Return a book marks LoanDTO as returned with returnDate
 *   - Get all loans (active + returned)
 *   - Get active loans filters returned ones
 *   - Get loans by user returns only that user's loans
 *
 *  ERROR:
 *   - Borrow with nonexistent user throws UserNotFoundException
 *   - Borrow with nonexistent book throws BookNotFoundException
 *   - Borrow when no copies left throws BookNotAvailableException
 *   - Borrow when user at loan limit throws LoanLimitExceededException
 *   - Return with invalid loanId throws IllegalArgumentException
 *   - Return already-returned loan throws IllegalArgumentException
 */
@DisplayName("LoanController Tests")
class LoanControllerTest {

    private LoanController loanController;
    private BookService bookService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        userService = new UserService();
        LoanService loanService = new LoanService(userService, bookService);
        loanController = new LoanController(loanService);

        bookService.addBook(new Book("B001", "Clean Code", "R. Martin", "ISBN-1"), 2);
        bookService.addBook(new Book("B002", "Pragmatic Programmer", "Hunt", "ISBN-2"), 1);
        bookService.addBook(new Book("B003", "Design Patterns", "GoF", "ISBN-3"), 3);

        userService.registerUser(new User("U001", "Alice", "alice@dosw.edu"));
        userService.registerUser(new User("U002", "Bob", "bob@dosw.edu"));
    }

    // ─────────────────────────────── SUCCESS SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[SUCCESS] Should return LoanDTO with correct fields on borrow")
    void shouldReturnLoanDTOOnBorrow() {
        LoanDTO dto = loanController.borrowBook("U001", "B001");

        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertEquals("U001", dto.getUserId());
        assertEquals("B001", dto.getBookId());
        assertFalse(dto.isReturned());
        assertNotNull(dto.getLoanDate());
        assertNull(dto.getReturnDate());
    }

    @Test
    @DisplayName("[SUCCESS] Should mark loan as returned with returnDate set")
    void shouldMarkLoanAsReturnedWithDate() {
        LoanDTO loan = loanController.borrowBook("U001", "B001");
        LoanDTO returned = loanController.returnBook(loan.getId());

        assertTrue(returned.isReturned());
        assertNotNull(returned.getReturnDate());
    }

    @Test
    @DisplayName("[SUCCESS] Should get all loans including returned ones")
    void shouldGetAllLoans() {
        LoanDTO loan1 = loanController.borrowBook("U001", "B001");
        loanController.borrowBook("U002", "B002");
        loanController.returnBook(loan1.getId());

        List<LoanDTO> all = loanController.getAllLoans();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("[SUCCESS] Should get only active loans")
    void shouldGetActiveLoans() {
        LoanDTO loan1 = loanController.borrowBook("U001", "B001");
        loanController.borrowBook("U002", "B002");
        loanController.returnBook(loan1.getId());

        List<LoanDTO> active = loanController.getActiveLoans();
        assertEquals(1, active.size());
        assertFalse(active.get(0).isReturned());
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty active list when no loans exist")
    void shouldReturnEmptyActiveLoansInitially() {
        assertTrue(loanController.getActiveLoans().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty all loans list initially")
    void shouldReturnEmptyAllLoansInitially() {
        assertTrue(loanController.getAllLoans().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should get loans filtered by user")
    void shouldGetLoansByUser() {
        loanController.borrowBook("U001", "B001");
        loanController.borrowBook("U001", "B003");
        loanController.borrowBook("U002", "B002");

        List<LoanDTO> aliceLoans = loanController.getLoansByUser("U001");
        assertEquals(2, aliceLoans.size());
        assertTrue(aliceLoans.stream().allMatch(l -> l.getUserId().equals("U001")));
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty list for user with no loans")
    void shouldReturnEmptyForUserWithNoLoans() {
        List<LoanDTO> loans = loanController.getLoansByUser("U002");
        assertTrue(loans.isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] All loans appear after multiple borrows")
    void shouldAccumulateLoansCorrectly() {
        loanController.borrowBook("U001", "B001");
        loanController.borrowBook("U001", "B003");
        loanController.borrowBook("U002", "B002");

        assertEquals(3, loanController.getAllLoans().size());
        assertEquals(3, loanController.getActiveLoans().size());
    }

    // ─────────────────────────────── ERROR SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[ERROR] Should throw UserNotFoundException when user does not exist")
    void shouldThrowWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> loanController.borrowBook("GHOST", "B001"));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotFoundException when book does not exist")
    void shouldThrowWhenBookNotFound() {
        assertThrows(BookNotFoundException.class, () -> loanController.borrowBook("U001", "GHOST"));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotAvailableException when no copies left")
    void shouldThrowWhenNoCopiesLeft() {
        loanController.borrowBook("U001", "B002"); // takes last copy
        assertThrows(BookNotAvailableException.class, () -> loanController.borrowBook("U002", "B002"));
    }

    @Test
    @DisplayName("[ERROR] Should throw LoanLimitExceededException when user has 3 active loans")
    void shouldThrowWhenLoanLimitExceeded() {
        loanController.borrowBook("U001", "B001");
        loanController.borrowBook("U001", "B002");
        loanController.borrowBook("U001", "B003");

        bookService.addBook(new Book("B004", "Refactoring", "Fowler", "ISBN-4"), 1);
        assertThrows(LoanLimitExceededException.class, () -> loanController.borrowBook("U001", "B004"));
    }

    @Test
    @DisplayName("[ERROR] Should throw IllegalArgumentException when returning unknown loan")
    void shouldThrowWhenReturningUnknownLoan() {
        assertThrows(IllegalArgumentException.class, () -> loanController.returnBook("LOAN-GHOST"));
    }

    @Test
    @DisplayName("[ERROR] Should throw IllegalArgumentException when returning already-returned loan")
    void shouldThrowWhenReturningAlreadyReturnedLoan() {
        LoanDTO loan = loanController.borrowBook("U001", "B001");
        loanController.returnBook(loan.getId());
        assertThrows(IllegalArgumentException.class, () -> loanController.returnBook(loan.getId()));
    }
}
