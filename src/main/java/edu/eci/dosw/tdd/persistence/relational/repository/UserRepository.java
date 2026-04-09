package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.persistence.relational.entity.UserRelationalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserRelationalEntity,Long> {

    Optional<UserRelationalEntity> findByUserName(String name);

    Optional<UserRelationalEntity> findByRole(String role);

    boolean existsByUsername(String username);

    Optional<UserRelationalEntity> findByUsernameAndPassword(String username, String password);

}