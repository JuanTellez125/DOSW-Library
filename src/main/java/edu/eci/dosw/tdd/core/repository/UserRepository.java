package edu.eci.dosw.tdd.core.repository;

import edu.eci.dosw.tdd.core.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByName(String name);

}