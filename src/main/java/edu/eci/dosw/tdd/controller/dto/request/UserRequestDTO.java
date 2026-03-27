package edu.eci.dosw.tdd.controller.dto.request;

import edu.eci.dosw.tdd.core.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDTO {

    @NotNull
    private String name;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Role role;

}
