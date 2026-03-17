package edu.eci.dosw.tdd.core.repository;

import edu.eci.dosw.tdd.core.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * In-memory repository for users.
 */
@Repository
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public boolean existsById(String id) {
        return users.containsKey(id);
    }

    public void clear() {
        users.clear();
    }
}
