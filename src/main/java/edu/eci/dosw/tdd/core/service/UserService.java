package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.controller.dto.request.UserRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.UserResponseDTO;

public interface UserService{

    UserResponseDTO createUser(UserRequestDTO dto);

    UserResponseDTO updateUser(Long id,UserRequestDTO dto);

    void deleteUser(Long id);

}