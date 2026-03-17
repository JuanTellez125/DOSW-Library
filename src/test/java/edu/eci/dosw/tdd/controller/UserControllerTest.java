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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    // ─── SUCCESS ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[SUCCESS] POST /api/users → 201 Created")
    void shouldRegisterUser() throws Exception {
        UserDTO dto = new UserDTO("U001", "Alice García", "alice@dosw.edu", 0);
        User user = new User("U001", "Alice García", "alice@dosw.edu");

        when(userService.registerUser(any())).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("U001"))
                .andExpect(jsonPath("$.name").value("Alice García"))
                .andExpect(jsonPath("$.email").value("alice@dosw.edu"))
                .andExpect(jsonPath("$.loanCount").value(0));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/users → 200 with list")
    void shouldGetAllUsers() throws Exception {
        User user = new User("U001", "Alice", "alice@dosw.edu");
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("U001"));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/users → 200 empty list")
    void shouldReturnEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("[SUCCESS] GET /api/users/{id} → 200 with user")
    void shouldGetUserById() throws Exception {
        User user = new User("U001", "Alice", "alice@dosw.edu");
        when(userService.getUserById("U001")).thenReturn(user);

        mockMvc.perform(get("/api/users/U001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("U001"))
                .andExpect(jsonPath("$.email").value("alice@dosw.edu"));
    }

    // ─── ERROR ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("[ERROR] POST /api/users with blank name → 400")
    void shouldReturn400ForBlankName() throws Exception {
        UserDTO dto = new UserDTO("U001", "", "alice@dosw.edu", 0);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("[ERROR] POST /api/users with invalid email → 400")
    void shouldReturn400ForInvalidEmail() throws Exception {
        UserDTO dto = new UserDTO("U001", "Alice", "not-an-email", 0);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("[ERROR] POST /api/users with blank ID → 400")
    void shouldReturn400ForBlankId() throws Exception {
        UserDTO dto = new UserDTO("", "Alice", "alice@dosw.edu", 0);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("[ERROR] POST /api/users duplicate ID → 400")
    void shouldReturn400ForDuplicateUser() throws Exception {
        UserDTO dto = new UserDTO("U001", "Alice", "alice@dosw.edu", 0);
        when(userService.registerUser(any()))
                .thenThrow(new IllegalArgumentException("User with id 'U001' already exists."));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("[ERROR] GET /api/users/{id} nonexistent → 404")
    void shouldReturn404ForUnknownUser() throws Exception {
        when(userService.getUserById("GHOST")).thenThrow(new UserNotFoundException("GHOST"));

        mockMvc.perform(get("/api/users/GHOST"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
    }
}
