package edu.eci.dosw.tdd.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDTO {

    @NotNull(message = "Name cannot be blank")
    public String name;

}
