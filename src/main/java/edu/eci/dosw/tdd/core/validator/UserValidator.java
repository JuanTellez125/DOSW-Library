package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.ValidationUtil;

/**
 * Validator for User entities.
 */
public class UserValidator {

    public void validate(User user) {
        ValidationUtil.requireNonNull(user, "user");
        ValidationUtil.requireNonBlank(user.getId(), "user.id");
        ValidationUtil.requireNonBlank(user.getName(), "user.name");
        ValidationUtil.requireValidEmail(user.getEmail());
    }

    public void validateId(String id) {
        ValidationUtil.requireNonBlank(id, "userId");
    }
}
