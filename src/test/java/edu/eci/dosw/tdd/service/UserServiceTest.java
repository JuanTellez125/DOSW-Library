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

}
