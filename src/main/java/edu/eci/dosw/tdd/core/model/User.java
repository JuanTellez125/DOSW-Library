package edu.eci.dosw.tdd.core.model;

/**
 * Represents a library user.
 */
public class User {

    private String id;
    private String name;
    private String email;
    private int loanCount;

    public static final int MAX_LOANS = 3;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.loanCount = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getLoanCount() { return loanCount; }
    public void setLoanCount(int loanCount) { this.loanCount = loanCount; }

    public boolean canBorrow() {
        return loanCount < MAX_LOANS;
    }

    public void incrementLoanCount() { this.loanCount++; }
    public void decrementLoanCount() {
        if (this.loanCount > 0) this.loanCount--;
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', email='" + email +
               "', loanCount=" + loanCount + "}";
    }
}
