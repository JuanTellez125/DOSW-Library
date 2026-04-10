package edu.eci.dosw.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.tdd.controller.dto.request.UserRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.UserResponseDTO;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRequestDTO requestDTO;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        requestDTO = UserRequestDTO.builder()
                .name("Juan")
                .build();

        responseDTO = UserResponseDTO.builder()
                .id("1")
                .name("Juan")
                .build();
    }

    @Test
    void shouldCreateUserAndReturn201() throws Exception {
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Juan"));
    }

    @Test
    void shouldUpdateUserAndReturn200() throws Exception {
        when(userService.updateUser(eq("1"), any(UserRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan"));
    }

    @Test
    void shouldReturn404WhenUserNotFoundOnUpdate() throws Exception {
        when(userService.updateUser(anyString(), any(UserRequestDTO.class)))
                .thenThrow(new UserNotFoundException("Usuario no encontrado"));

        mockMvc.perform(put("/api/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteUserAndReturn204() throws Exception {
        doNothing().when(userService).deleteUser("1");

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenUserNotFoundOnDelete() throws Exception {
        doThrow(new UserNotFoundException("Usuario no encontrado"))
                .when(userService).deleteUser("1");

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindUserByIdAndReturn200() throws Exception {
        when(userService.findUserById("1")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan"));
    }

    @Test
    void shouldReturn404WhenUserNotFoundById() throws Exception {
        when(userService.findUserById("1"))
                .thenThrow(new UserNotFoundException("Usuario no encontrado"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindAllUsersAndReturn200() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Juan"));
    }

}