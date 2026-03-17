package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.exception.ValidationException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserService.
 *
 * Scenarios covered:
 *  SUCCESS:
 *   - Register a valid user
 *   - Get all users (empty / populated)
 *   - Get user by valid ID
 *   - canBorrow returns true when below limit
 *   - canBorrow returns false when at limit
 *   - incrementLoanCount and decrementLoanCount
 *
 *  ERROR:
 *   - Register null user
 *   - Register user with blank fields
 *   - Register user with invalid email
 *   - Register duplicate user ID
 *   - Get user by null/blank/nonexistent ID
 */
@DisplayName("UserService Tests")
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    // ─────────────────────────────── SUCCESS SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[SUCCESS] Should register a valid user")
    void shouldRegisterValidUser() {
        User user = new User("U001", "Alice García", "alice@dosw.edu");
        userService.registerUser(user);

        User found = userService.getUserById("U001");
        assertNotNull(found);
        assertEquals("Alice García", found.getName());
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty list when no users registered")
    void shouldReturnEmptyListInitially() {
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should return all registered users")
    void shouldReturnAllUsers() {
        userService.registerUser(new User("U001", "Alice", "alice@dosw.edu"));
        userService.registerUser(new User("U002", "Bob", "bob@dosw.edu"));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("[SUCCESS] Should find user by ID")
    void shouldFindUserById() {
        userService.registerUser(new User("U001", "Alice", "alice@dosw.edu"));
        User found = userService.getUserById("U001");
        assertEquals("U001", found.getId());
    }

    @Test
    @DisplayName("[SUCCESS] canBorrow returns true when user has zero loans")
    void shouldAllowBorrowWhenNoLoans() {
        User user = new User("U001", "Alice", "alice@dosw.edu");
        assertTrue(user.canBorrow());
    }

    @Test
    @DisplayName("[SUCCESS] canBorrow returns false when user has reached max loans")
    void shouldNotAllowBorrowWhenAtLimit() {
        User user = new User("U001", "Alice", "alice@dosw.edu");
        user.incrementLoanCount();
        user.incrementLoanCount();
        user.incrementLoanCount(); // now at MAX_LOANS=3
        assertFalse(user.canBorrow());
    }

    @Test
    @DisplayName("[SUCCESS] decrementLoanCount should not go below zero")
    void decrementShouldNotGoBelowZero() {
        User user = new User("U001", "Alice", "alice@dosw.edu");
        user.decrementLoanCount(); // already at 0
        assertEquals(0, user.getLoanCount());
    }

    @Test
    @DisplayName("[SUCCESS] incrementLoanCount and decrementLoanCount are symmetric")
    void incrementAndDecrementAreSymmetric() {
        User user = new User("U001", "Alice", "alice@dosw.edu");
        user.incrementLoanCount();
        user.incrementLoanCount();
        user.decrementLoanCount();
        assertEquals(1, user.getLoanCount());
    }

    // ─────────────────────────────── ERROR SCENARIOS ───────────────────────────────

    @Test
    @DisplayName("[ERROR] Should throw ValidationException when registering null user")
    void shouldThrowWhenUserIsNull() {
        assertThrows(ValidationException.class, () -> userService.registerUser(null));
    }

    @Test
    @DisplayName("[ERROR] Should throw ValidationException when user name is blank")
    void shouldThrowWhenNameIsBlank() {
        User user = new User("U001", "  ", "alice@dosw.edu");
        assertThrows(ValidationException.class, () -> userService.registerUser(user));
    }

    @Test
    @DisplayName("[ERROR] Should throw ValidationException for invalid email format")
    void shouldThrowForInvalidEmail() {
        User user = new User("U001", "Alice", "not-an-email");
        assertThrows(ValidationException.class, () -> userService.registerUser(user));
    }

    @ParameterizedTest
    @ValueSource(strings = {"plainaddress", "@missinglocal.com", "missing@domain", "two@@at.com"})
    @DisplayName("[ERROR] Should reject various invalid email formats")
    void shouldRejectInvalidEmailFormats(String email) {
        User user = new User("U001", "Alice", email);
        assertThrows(ValidationException.class, () -> userService.registerUser(user));
    }

    @Test
    @DisplayName("[ERROR] Should throw when registering duplicate user ID")
    void shouldThrowForDuplicateUserId() {
        userService.registerUser(new User("U001", "Alice", "alice@dosw.edu"));
        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(new User("U001", "Alice2", "alice2@dosw.edu")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("[ERROR] Should throw ValidationException for blank/null user ID on getById")
    void shouldThrowForBlankUserId(String id) {
        assertThrows(ValidationException.class, () -> userService.getUserById(id));
    }

    @Test
    @DisplayName("[ERROR] Should throw UserNotFoundException for unknown ID")
    void shouldThrowUserNotFoundForUnknownId() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById("GHOST"));
    }
}
