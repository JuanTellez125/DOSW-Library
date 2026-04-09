package edu.eci.dosw.tdd.persistence.relational.entity;


import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loans")
public class LoanRelationalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookRelationalEntity book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserRelationalEntity user;

    private LocalDate loanDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate returnDate;


}
