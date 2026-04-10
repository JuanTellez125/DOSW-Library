package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.controller.dto.response.UserResponseDTO;
import edu.eci.dosw.tdd.core.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRelationalEntity {

    UserResponseDTO toModel(UserRelationalEntity user);

    UserRelationalEntity toEntity(User user);

}
