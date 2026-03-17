package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService using Mockito to mock UserRepository.
 *
 * Scenarios covered:
 *  SUCCESS: registerUser, getAllUsers, getUserById,
 *           canBorrow (below/at limit), incrementLoanCount, decrementLoanCount
 *  ERROR:   registerUser duplicate, getUserById nonexistent
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests (Mockito)")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User("U001", "Alice García", "alice@dosw.edu");
    }

    // ─── SUCCESS ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[SUCCESS] Should register user and save to repository")
    void shouldRegisterUser() {
        when(userRepository.existsById("U001")).thenReturn(false);

        User result = userService.registerUser(sampleUser);

        verify(userRepository).save(sampleUser);
        assertEquals("U001", result.getId());
    }

    @Test
    @DisplayName("[SUCCESS] Should return all users from repository")
    void shouldGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("[SUCCESS] Should return empty list when no users exist")
    void shouldReturnEmptyListInitially() {
        when(userRepository.findAll()).thenReturn(List.of());
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    @DisplayName("[SUCCESS] Should find user by ID")
    void shouldFindUserById() {
        when(userRepository.findById("U001")).thenReturn(Optional.of(sampleUser));

        User result = userService.getUserById("U001");

        assertEquals("U001", result.getId());
        verify(userRepository).findById("U001");
    }

    @Test
    @DisplayName("[SUCCESS] canBorrow returns true when below limit")
    void canBorrowReturnsTrueWhenBelowLimit() {
        assertTrue(sampleUser.canBorrow());
    }

    @Test
    @DisplayName("[SUCCESS] canBorrow returns false at MAX_LOANS")
    void canBorrowReturnsFalseAtLimit() {
        sampleUser.incrementLoanCount();
        sampleUser.incrementLoanCount();
        sampleUser.incrementLoanCount();
        assertFalse(sampleUser.canBorrow());
    }

    @Test
    @DisplayName("[SUCCESS] incrementLoanCount increases count")
    void incrementLoanCount() {
        sampleUser.incrementLoanCount();
        assertEquals(1, sampleUser.getLoanCount());
    }

    @Test
    @DisplayName("[SUCCESS] decrementLoanCount decreases count")
    void decrementLoanCount() {
        sampleUser.incrementLoanCount();
        sampleUser.decrementLoanCount();
        assertEquals(0, sampleUser.getLoanCount());
    }

    @Test
    @DisplayName("[SUCCESS] decrementLoanCount does not go below zero")
    void decrementDoesNotGoBelowZero() {
        sampleUser.decrementLoanCount(); // already 0
        assertEquals(0, sampleUser.getLoanCount());
    }

    // ─── ERROR ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[ERROR] Should throw IllegalArgumentException for duplicate user ID")
    void shouldThrowForDuplicateUser() {
        when(userRepository.existsById("U001")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(sampleUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("[ERROR] Should throw UserNotFoundException for unknown ID")
    void shouldThrowUserNotFoundForUnknownId() {
        when(userRepository.findById("GHOST")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById("GHOST"));
    }
}
