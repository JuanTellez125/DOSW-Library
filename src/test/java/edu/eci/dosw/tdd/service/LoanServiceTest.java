package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.exception.*;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.service.LoanService;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoanService.
 *
 * Scenarios covered:
 *  SUCCESS:
 *   - Borrow a book successfully
 *   - Loan updates user loan count
 *   - Loan decrements book copies
 *   - Return a book successfully
 *   - Return updates user loan count
 *   - Return increments book copies and availability
 *   - Borrow different books by same user
 *   - Get all loans
 *   - Get active loans filters returned ones
 *   - Get loans by user
 *   - Borrow up to the limit (3 books)
 *   - Book with multiple copies can be borrowed by multiple users
 *
 *  ERROR:
 *   - Borrow with nonexistent user
 *   - Borrow with nonexistent book
 *   - Borrow book with no copies left
 *   - Borrow when user has reached loan limit
 *   - Return with invalid loan ID
 *   - Return already-returned loan
 *   - Borrow with blank userId or bookId
 */
@DisplayName("LoanService Tests")
class LoanServiceTest {

    private BookService bookService;
    private UserService userService;
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        userService = new UserService();
        loanService = new LoanService(userService, bookService);

        // Default fixtures
        bookService.addBook(new Book("B001", "Clean Code", "R. Martin", "ISBN-1"), 2);
        bookService.addBook(new Book("B002", "Pragmatic Programmer", "Hunt", "ISBN-2"), 1);
        bookService.addBook(new Book("B003", "Design Patterns", "GoF", "ISBN-3"), 3);

        userService.registerUser(new User("U001", "Alice", "alice@dosw.edu"));
        userService.registerUser(new User("U002", "Bob", "bob@dosw.edu"));
    }

    // ─────────────────────────────── SUCCESS SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[SUCCESS] Should create a loan successfully")
    void shouldCreateLoanSuccessfully() {
        Loan loan = loanService.borrowBook("U001", "B001");

        assertNotNull(loan);
        assertNotNull(loan.getId());
        assertEquals("U001", loan.getUserId());
        assertEquals("B001", loan.getBookId());
        assertFalse(loan.isReturned());
        assertNotNull(loan.getLoanDate());
    }

    @Test
    @DisplayName("[SUCCESS] Should decrement book copies after loan")
    void shouldDecrementCopiesAfterLoan() {
        loanService.borrowBook("U001", "B001");
        assertEquals(1, bookService.getAvailableCopies("B001"));
    }

    @Test
    @DisplayName("[SUCCESS] Should increment user loan count after borrow")
    void shouldIncrementUserLoanCount() {
        loanService.borrowBook("U001", "B001");
        User user = userService.getUserById("U001");
        assertEquals(1, user.getLoanCount());
    }

    @Test
    @DisplayName("[SUCCESS] Should allow borrowing up to 3 books (the limit)")
    void shouldAllowBorrowingUpToLimit() {
        loanService.borrowBook("U001", "B001");
        loanService.borrowBook("U001", "B002");
        loanService.borrowBook("U001", "B003");

        User user = userService.getUserById("U001");
        assertEquals(3, user.getLoanCount());
        assertFalse(user.canBorrow());
    }

    @Test
    @DisplayName("[SUCCESS] Should return a book successfully")
    void shouldReturnBookSuccessfully() {
        Loan loan = loanService.borrowBook("U001", "B001");
        Loan returned = loanService.returnBook(loan.getId());

        assertTrue(returned.isReturned());
        assertNotNull(returned.getReturnDate());
    }

    @Test
    @DisplayName("[SUCCESS] Should restore book copies after return")
    void shouldRestoreCopiesAfterReturn() {
        Loan loan = loanService.borrowBook("U001", "B002"); // B002 has 1 copy
        assertEquals(0, bookService.getAvailableCopies("B002"));

        loanService.returnBook(loan.getId());
        assertEquals(1, bookService.getAvailableCopies("B002"));
        assertTrue(bookService.getBookById("B002").isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] Should decrement user loan count after return")
    void shouldDecrementUserLoanCountAfterReturn() {
        Loan loan = loanService.borrowBook("U001", "B001");
        loanService.returnBook(loan.getId());

        User user = userService.getUserById("U001");
        assertEquals(0, user.getLoanCount());
    }

    @Test
    @DisplayName("[SUCCESS] Should allow re-borrowing after returning book")
    void shouldAllowReBorrowingAfterReturn() {
        Loan loan = loanService.borrowBook("U001", "B002");
        loanService.returnBook(loan.getId());

        Loan newLoan = loanService.borrowBook("U001", "B002");
        assertNotNull(newLoan);
        assertNotEquals(loan.getId(), newLoan.getId());
    }

    @Test
    @DisplayName("[SUCCESS] Should get all loans including returned ones")
    void shouldGetAllLoans() {
        Loan loan1 = loanService.borrowBook("U001", "B001");
        loanService.borrowBook("U002", "B002");
        loanService.returnBook(loan1.getId());

        List<Loan> all = loanService.getAllLoans();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("[SUCCESS] Should get only active loans")
    void shouldGetActiveLoansOnly() {
        Loan loan1 = loanService.borrowBook("U001", "B001");
        loanService.borrowBook("U002", "B002");
        loanService.returnBook(loan1.getId());

        List<Loan> active = loanService.getActiveLoans();
        assertEquals(1, active.size());
        assertFalse(active.get(0).isReturned());
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty active list when all loans returned")
    void shouldReturnEmptyWhenAllLoansReturned() {
        Loan loan = loanService.borrowBook("U001", "B001");
        loanService.returnBook(loan.getId());

        assertTrue(loanService.getActiveLoans().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should get loans filtered by user")
    void shouldGetLoansByUser() {
        loanService.borrowBook("U001", "B001");
        loanService.borrowBook("U001", "B003");
        loanService.borrowBook("U002", "B002");

        List<Loan> aliceLoans = loanService.getLoansByUser("U001");
        assertEquals(2, aliceLoans.size());
        assertTrue(aliceLoans.stream().allMatch(l -> l.getUserId().equals("U001")));
    }

    @Test
    @DisplayName("[SUCCESS] Multiple users can borrow different copies of same book")
    void shouldAllowMultipleUsersToborrowSameBook() {
        // B001 has 2 copies
        Loan l1 = loanService.borrowBook("U001", "B001");
        Loan l2 = loanService.borrowBook("U002", "B001");

        assertNotNull(l1);
        assertNotNull(l2);
        assertEquals(0, bookService.getAvailableCopies("B001"));
    }

    // ─────────────────────────────── ERROR SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[ERROR] Should throw UserNotFoundException when user does not exist")
    void shouldThrowWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> loanService.borrowBook("GHOST", "B001"));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotFoundException when book does not exist")
    void shouldThrowWhenBookNotFound() {
        assertThrows(BookNotFoundException.class, () -> loanService.borrowBook("U001", "GHOST"));
    }

    @Test
    @DisplayName("[ERROR] Should throw BookNotAvailableException when no copies left")
    void shouldThrowWhenNoCopiesLeft() {
        // B002 has 1 copy; borrow it first
        loanService.borrowBook("U001", "B002");
        assertThrows(BookNotAvailableException.class, () -> loanService.borrowBook("U002", "B002"));
    }

    @Test
    @DisplayName("[ERROR] Should throw LoanLimitExceededException when user has 3 loans")
    void shouldThrowWhenUserExceedsLoanLimit() {
        loanService.borrowBook("U001", "B001");
        loanService.borrowBook("U001", "B002");
        loanService.borrowBook("U001", "B003");

        // Add a 4th book
        bookService.addBook(new Book("B004", "Refactoring", "Fowler", "ISBN-4"), 1);
        assertThrows(LoanLimitExceededException.class, () -> loanService.borrowBook("U001", "B004"));
    }

    @Test
    @DisplayName("[ERROR] Should throw when returning with unknown loan ID")
    void shouldThrowWhenReturnLoanNotFound() {
        assertThrows(IllegalArgumentException.class, () -> loanService.returnBook("LOAN-GHOST"));
    }

    @Test
    @DisplayName("[ERROR] Should throw when returning an already-returned loan")
    void shouldThrowWhenReturningAlreadyReturnedLoan() {
        Loan loan = loanService.borrowBook("U001", "B001");
        loanService.returnBook(loan.getId());
        // The loan is no longer active — second return should fail
        assertThrows(IllegalArgumentException.class, () -> loanService.returnBook(loan.getId()));
    }

    @Test
    @DisplayName("[ERROR] Should throw ValidationException for blank userId on borrow")
    void shouldThrowForBlankUserIdOnBorrow() {
        assertThrows(ValidationException.class, () -> loanService.borrowBook("  ", "B001"));
    }

    @Test
    @DisplayName("[ERROR] Should throw ValidationException for blank bookId on borrow")
    void shouldThrowForBlankBookIdOnBorrow() {
        assertThrows(ValidationException.class, () -> loanService.borrowBook("U001", "  "));
    }

    @Test
    @DisplayName("[ERROR] Should throw ValidationException for blank loanId on return")
    void shouldThrowForBlankLoanIdOnReturn() {
        assertThrows(ValidationException.class, () -> loanService.returnBook("  "));
    }
}
