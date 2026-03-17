package edu.eci.dosw.tdd.controller.dto;

/**
 * DTO for Loan responses.
 * No validation annotations — this is a response-only DTO.
 */
public class LoanDTO {

    private String id;
    private String userId;
    private String bookId;
    private String loanDate;
    private String returnDate;
    private boolean returned;

    public LoanDTO() {}

    public LoanDTO(String id, String userId, String bookId,
                   String loanDate, String returnDate, boolean returned) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getLoanDate() { return loanDate; }
    public void setLoanDate(String loanDate) { this.loanDate = loanDate; }

    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }

    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
}
