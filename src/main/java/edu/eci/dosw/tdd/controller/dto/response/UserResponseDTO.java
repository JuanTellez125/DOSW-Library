package edu.eci.dosw.tdd.controller.dto.response;

import edu.eci.dosw.tdd.core.model.enums.MemberShip;
import edu.eci.dosw.tdd.core.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String userId;
    private String name;
    private String userName;
    private String email;
    private Role role;
    private MemberShip memberShip;
    private LocalDate addedDate;

}