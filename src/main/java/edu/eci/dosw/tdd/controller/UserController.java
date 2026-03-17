package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;

import java.util.List;

/**
 * Controller layer for User operations.
 */
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService) {
        this.userService = userService;
        this.userMapper = new UserMapper();
    }

    public void registerUser(UserDTO dto) {
        User user = userMapper.toModel(dto);
        userService.registerUser(user);
    }

    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserDTO getUserById(String id) {
        return userMapper.toDTO(userService.getUserById(id));
    }
}
