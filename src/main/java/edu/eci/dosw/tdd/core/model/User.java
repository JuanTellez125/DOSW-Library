
package edu.eci.dosw.tdd.core.model;

import edu.eci.dosw.tdd.core.model.enums.Role;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String name;
    private String username;
    private String password;
    private Role role;
}
