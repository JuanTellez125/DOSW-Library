package edu.eci.dosw.tdd.core.model;

import edu.eci.dosw.tdd.core.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanHistory {
    private Status status;
    private LocalDate excecutionDate;
}
