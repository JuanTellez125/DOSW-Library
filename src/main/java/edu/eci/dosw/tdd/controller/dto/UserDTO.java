package edu.eci.dosw.tdd.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for User — @Email ensures email format is validated by Spring
 * automatically when paired with @Valid on the controller method.
 */
public class UserDTO {

    @NotBlank(message = "User ID must not be blank")
    private String id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid address")
    private String email;

    private int loanCount;

    public UserDTO() {}

    public UserDTO(String id, String name, String email, int loanCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.loanCount = loanCount;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getLoanCount() { return loanCount; }
    public void setLoanCount(int loanCount) { this.loanCount = loanCount; }
}
