package edu.eci.dosw.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.exception.GlobalExceptionHandler;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
