package edu.eci.dosw.tdd.controller.dto.request;

import edu.eci.dosw.tdd.core.model.enums.MemberShip;
import edu.eci.dosw.tdd.core.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @NotNull(message = "El nombre no puede estar vacio")
    private String name;

    @NotNull(message = "Username no puede estar vacio")
    private String userName;

    @NotNull(message = "Email no puede estar vacio")
    @Email(message = "Email debe ser valido")
    private String email;

    @NotNull(message = "Password no puede estar vacio")
    private String password;

    private Role role;
    private MemberShip memberShip;

}
