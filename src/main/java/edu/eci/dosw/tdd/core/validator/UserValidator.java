package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.persistence.relational.repository.UserRepository;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validate(String name) {
        ValidationUtil.requireNonNull(name, "The name");
    }

}
