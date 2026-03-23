package edu.eci.dosw.tdd.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public class UserRequestDTO {

    @NotNull(message = "Name cannot be blank")
    public String name;

    @NotNull(message = "ID cannot be blank")
    public String id;
}
