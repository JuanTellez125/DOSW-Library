package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.request.UserRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.UserResponseDTO;
import edu.eci.dosw.tdd.core.model.User;
import org.springframework.stereotype.Component;


@Component
public interface UserMapper {


    UserResponseDTO toDto(User user);

    User toEntity(UserRequestDTO dto);

}