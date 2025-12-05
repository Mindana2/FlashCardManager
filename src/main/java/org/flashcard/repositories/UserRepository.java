package org.flashcard.repositories;

import org.flashcard.models.dataclasses.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * This interface is a Spring Data JPA repository for the Deck entity.
 *
 * This is where a lot of magic happens with Spring Data JPA:
 *
 * @Repository marks this interface as a Spring-managed repository.
 * Spring automatically detects it and provides an implementation at runtime.
 *
 * The repository automatically provides access to standard CRUD operations:
 *   save(), findById(), findAll(), delete(), etc., without writing any SQL.
 *
 * Very cool
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find a user by username
    Optional<User> findByUsername(String username);

    // Check if a username exists (needed for UserService)
    boolean existsByUsername(String username);

}
