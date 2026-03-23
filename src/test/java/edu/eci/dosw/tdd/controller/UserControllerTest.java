package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;

/**
 * Controller-layer tests for UserController using @WebMvcTest.
 *
 * Scenarios covered:
 *  SUCCESS: POST /api/users (201), GET /api/users (200), GET /api/users/{id} (200)
 *  ERROR:   POST blank name (400), POST invalid email (400),
 *           POST duplicate ID (400), GET nonexistent user (404)
 */
@WebMvcTest(UserController.class)
@Import({UserMapper.class, GlobalExceptionHandler.class})
@DisplayName("UserController Web Tests (MockMvc)")
class UserControllerTest {

}
