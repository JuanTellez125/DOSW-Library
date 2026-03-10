package edu.eci.dosw.tdd.model;

import lombok.Data;

import java.util.Date;

@Data
public class Loan {
    private Book book;
    private User user;
    private Date loanDate;
    private Status status;
    private Date returnDate;
}
