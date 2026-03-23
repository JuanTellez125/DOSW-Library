package edu.eci.dosw.tdd.core.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class User {

    private String name;

    @Id
    private String ID;

}
