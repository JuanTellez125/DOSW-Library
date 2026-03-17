package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.controller.BookController;
import edu.eci.dosw.tdd.controller.LoanController;
import edu.eci.dosw.tdd.controller.UserController;
import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.service.LoanService;
import edu.eci.dosw.tdd.core.service.UserService;

/**
 * Entry point for the DOSW Library Management System.
 */
public class DoswLibraryApplication {

    public static void main(String[] args) {
        // Compose services
        BookService bookService = new BookService();
        UserService userService = new UserService();
        LoanService loanService = new LoanService(userService, bookService);

        // Compose controllers
        BookController bookController = new BookController(bookService);
        UserController userController = new UserController(userService);
        LoanController loanController = new LoanController(loanService);

        // Demo flow
        System.out.println("=== DOSW Library System ===\n");

        // Add books
        bookController.addBook(new BookDTO("B001", "Clean Code", "Robert C. Martin", "978-0132350884", true, 3));
        bookController.addBook(new BookDTO("B002", "The Pragmatic Programmer", "Andrew Hunt", "978-0201616224", true, 2));

        // Register users
        userController.registerUser(new UserDTO("U001", "Alice García", "alice@dosw.edu", 0));
        userController.registerUser(new UserDTO("U002", "Bob Martínez", "bob@dosw.edu", 0));

        // Borrow a book
        LoanDTO loan = loanController.borrowBook("U001", "B001");
        System.out.println("Loan created: " + loan.getId());

        // List active loans
        System.out.println("Active loans: " + loanController.getActiveLoans().size());

        // Return the book
        loanController.returnBook(loan.getId());
        System.out.println("Book returned.");
        System.out.println("Active loans after return: " + loanController.getActiveLoans().size());
    }
}
