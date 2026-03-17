package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Loan operations.
 * Coordinates BookService and UserService to manage the full loan lifecycle.
 */
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public LoanService(LoanRepository loanRepository,
                       UserService userService,
                       BookService bookService) {
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    /**
     * Creates a loan after validating:
     *  - User exists
     *  - Book exists and has available copies
     *  - User has not exceeded MAX_LOANS
     */
    public Loan borrowBook(String userId, String bookId) {
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
        loanRepository.save(loan);
        return loan;
    }

    /**
     * Returns a book, marks the loan as returned, and restores availability.
     */
    public Loan returnBook(String loanId) {
        Loan loan = loanRepository.findActiveLoanById(loanId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Active loan with id '" + loanId + "' not found."));

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        bookService.incrementCopy(loan.getBookId());
        userService.getUserById(loan.getUserId()).decrementLoanCount();

        return loan;
    }

    /**
     * Returns all loans (active + returned).
     */
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    /**
     * Returns only active (not yet returned) loans.
     */
    public List<Loan> getActiveLoans() {
        return loanRepository.findActive();
    }

    /**
     * Returns all loans for a specific user.
     */
    public List<Loan> getLoansByUser(String userId) {
        userService.getUserById(userId); // validate user exists
        return loanRepository.findByUserId(userId);
    }
}
