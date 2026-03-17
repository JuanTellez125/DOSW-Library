package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service that manages library users.
 */
public class UserService {

    private final List<User> users;
    private final Map<String, User> userById;
    private final UserValidator validator;

    public UserService() {
        this.users = new ArrayList<>();
        this.userById = new HashMap<>();
        this.validator = new UserValidator();
    }

    /**
     * Registers a new user in the system.
     */
    public void registerUser(User user) {
        validator.validate(user);
        if (userById.containsKey(user.getId())) {
            throw new IllegalArgumentException("User with id '" + user.getId() + "' already exists.");
        }
        users.add(user);
        userById.put(user.getId(), user);
    }

    /**
     * Returns all registered users.
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Returns a user by their ID.
     */
    public User getUserById(String id) {
        validator.validateId(id);
        User user = userById.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }
}
