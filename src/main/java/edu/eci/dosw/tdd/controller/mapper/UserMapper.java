package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.User;
import org.springframework.stereotype.Component;

/**
 * Maps between the User domain model and UserDTO.
 */
@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLoanCount()
        );
    }

    public User toModel(UserDTO dto) {
        if (dto == null) return null;
        return new User(dto.getId(), dto.getName(), dto.getEmail());
    }
}
