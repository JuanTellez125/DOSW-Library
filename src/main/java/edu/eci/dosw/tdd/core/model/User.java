package edu.eci.dosw.tdd.core.model;

import edu.eci.dosw.tdd.core.model.enums.MemberShip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import edu.eci.dosw.tdd.core.model.enums.Role;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userId;
    private String name;
    private String userName;
    private String email;
    private String password;
    private Role role;
    private MemberShip memberShip;
    private LocalDate addedDate;

}
