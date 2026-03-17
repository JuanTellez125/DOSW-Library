package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.exception.ValidationException;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserController.
 *
 * Scenarios covered:
 *  SUCCESS:
 *   - Register user through controller
 *   - Get all users returns DTOs
 *   - Get user by ID returns correct DTO
 *   - loanCount is reflected in DTO
 *
 *  ERROR:
 *   - Register user with blank name
 *   - Register user with invalid email
 *   - Register duplicate user
 *   - Get user by nonexistent ID
 */
@DisplayName("UserController Tests")
class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(new UserService());
    }

    // ─────────────────────────────── SUCCESS SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[SUCCESS] Should register a user through controller")
    void shouldRegisterUserThroughController() {
        UserDTO dto = new UserDTO("U001", "Alice García", "alice@dosw.edu", 0);
        assertDoesNotThrow(() -> userController.registerUser(dto));
    }

    @Test
    @DisplayName("[SUCCESS] Should return all users as DTOs")
    void shouldReturnAllUsersAsDTOs() {
        userController.registerUser(new UserDTO("U001", "Alice", "alice@dosw.edu", 0));
        userController.registerUser(new UserDTO("U002", "Bob", "bob@dosw.edu", 0));

        List<UserDTO> users = userController.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty list when no users registered")
    void shouldReturnEmptyListInitially() {
        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should return correct DTO fields for a user")
    void shouldReturnCorrectDTOFields() {
        userController.registerUser(new UserDTO("U001", "Alice García", "alice@dosw.edu", 0));

        UserDTO result = userController.getUserById("U001");

        assertEquals("U001", result.getId());
        assertEquals("Alice García", result.getName());
        assertEquals("alice@dosw.edu", result.getEmail());
        assertEquals(0, result.getLoanCount());
    }

    @Test
    @DisplayName("[SUCCESS] Should reflect loanCount of zero on fresh user")
    void shouldReflectZeroLoanCount() {
        userController.registerUser(new UserDTO("U001", "Alice", "alice@dosw.edu", 0));
        UserDTO result = userController.getUserById("U001");
        assertEquals(0, result.getLoanCount());
    }

    // ─────────────────────────────── ERROR SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[ERROR] Should throw ValidationException when user name is blank")
    void shouldThrowWhenNameBlank() {
        UserDTO dto = new UserDTO("U001", "  ", "alice@dosw.edu", 0);
        assertThrows(ValidationException.class, () -> userController.registerUser(dto));
    }

    @Test
    @DisplayName("[ERROR] Should throw ValidationException for invalid email")
    void shouldThrowForInvalidEmail() {
        UserDTO dto = new UserDTO("U001", "Alice", "not-an-email", 0);
        assertThrows(ValidationException.class, () -> userController.registerUser(dto));
    }

    @Test
    @DisplayName("[ERROR] Should throw when registering duplicate user ID")
    void shouldThrowForDuplicateUser() {
        userController.registerUser(new UserDTO("U001", "Alice", "alice@dosw.edu", 0));
        assertThrows(IllegalArgumentException.class,
                () -> userController.registerUser(new UserDTO("U001", "Alice2", "alice2@dosw.edu", 0)));
    }

    @Test
    @DisplayName("[ERROR] Should throw UserNotFoundException for unknown ID")
    void shouldThrowWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userController.getUserById("GHOST"));
    }

    @Test
    @DisplayName("[ERROR] Should throw ValidationException for blank user ID on getById")
    void shouldThrowForBlankIdOnGet() {
        assertThrows(ValidationException.class, () -> userController.getUserById("  "));
    }
}
