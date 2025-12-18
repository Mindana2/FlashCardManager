package org.flashcard.repositories;

import org.flashcard.models.dataclasses.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access interface that provides specialized query methods for retrieving
 * users, abstracting the SQL logic via Spring Data JPA.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    // Check if a username exists (needed for UserService)
    boolean existsByUsername(String username);
}
