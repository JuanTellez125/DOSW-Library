package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.validator.LoanValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service that manages book loans.
 * Coordinates between UserService and BookService to process loans and returns.
 */
public class LoanService {

    private final List<Loan> loans;
    private final UserService userService;
    private final BookService bookService;
    private final LoanValidator validator;

    public LoanService(UserService userService, BookService bookService) {
        this.loans = new ArrayList<>();
        this.userService = userService;
        this.bookService = bookService;
        this.validator = new LoanValidator();
    }

    /**
     * Creates a loan for a user and a book.
     * Validates: user exists, book exists, book has copies, user has not exceeded limit.
     *
     * @param userId the ID of the user requesting the loan
     * @param bookId the ID of the book to borrow
     * @return the created Loan
     */
    public Loan borrowBook(String userId, String bookId) {
        validator.validateLoanRequest(userId, bookId);

        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId);

        if (!user.canBorrow()) {
            throw new LoanLimitExceededException(userId);
        }

        if (!book.isAvailable() || bookService.getAvailableCopies(bookId) == 0) {
            throw new BookNotAvailableException(bookId);
        }

        bookService.decrementCopy(bookId);
        user.incrementLoanCount();

        Loan loan = new Loan(IdGeneratorUtil.generateId("LOAN"), userId, bookId);
        loans.add(loan);
        return loan;
    }

    /**
     * Returns a book, closing the associated loan.
     *
     * @param loanId the ID of the loan to close
     * @return the updated Loan
     */
    public Loan returnBook(String loanId) {
        validator.validateReturnRequest(loanId);

        Loan loan = findActiveLoanById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Active loan with id '" + loanId + "' not found."));

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        bookService.incrementCopy(loan.getBookId());

        User user = userService.getUserById(loan.getUserId());
        user.decrementLoanCount();

        return loan;
    }

    /**
     * Returns all loans (active and returned).
     */
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    /**
     * Returns all active (not yet returned) loans.
     */
    public List<Loan> getActiveLoans() {
        return loans.stream()
                .filter(l -> !l.isReturned())
                .toList();
    }

    /**
     * Returns all loans for a specific user.
     */
    public List<Loan> getLoansByUser(String userId) {
        validator.validateLoanRequest(userId, "dummy");
        userService.getUserById(userId); // validate user exists
        return loans.stream()
                .filter(l -> l.getUserId().equals(userId))
                .toList();
    }

    private Optional<Loan> findActiveLoanById(String loanId) {
        return loans.stream()
                .filter(l -> l.getId().equals(loanId) && !l.isReturned())
                .findFirst();
    }
}
