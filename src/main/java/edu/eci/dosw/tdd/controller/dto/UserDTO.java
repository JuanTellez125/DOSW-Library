package edu.eci.dosw.tdd.controller.dto;

/**
 * Data Transfer Object for User.
 */
public class UserDTO {
    private String id;
    private String name;
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
