package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.controller.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRelationalEntity {

    @Mapping(source = "userId", target = "id")
    UserResponseDTO toDto(UserRelationalEntity user);

}
