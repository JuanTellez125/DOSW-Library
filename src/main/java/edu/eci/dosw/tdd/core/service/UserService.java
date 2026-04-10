package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.controller.dto.request.UserRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.UserResponseDTO;
import edu.eci.dosw.tdd.core.model.User;

import java.util.List;

public interface UserService{

    UserResponseDTO createUser(UserRequestDTO dto);

    UserResponseDTO updateUser(String id,UserRequestDTO dto);

    void deleteUser(String id);

    User getUserById(String id);

    List<User> getAllUsers();








}