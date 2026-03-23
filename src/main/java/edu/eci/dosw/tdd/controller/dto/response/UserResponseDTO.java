package edu.eci.dosw.tdd.controller.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class UserResponseDTO {

    private String name;
    private String id;


}
