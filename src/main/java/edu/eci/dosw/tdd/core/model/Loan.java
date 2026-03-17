package edu.eci.dosw.tdd.core.model;

import java.time.LocalDate;

public class Loan {

    private String id;
    private String userId;
    private String bookId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private boolean returned;

    public Loan() {}

    public Loan(String id, String userId, String bookId) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = LocalDate.now();
        this.returned = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
}
