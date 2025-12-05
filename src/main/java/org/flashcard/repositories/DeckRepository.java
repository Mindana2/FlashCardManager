package org.flashcard.repositories;

import org.flashcard.models.dataclasses.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
 * Spring AUTOGENERATES SQL queries based on method names! For example:
 *  findByUserId(Integer userId): Returns a list of all decks belonging to a specific user.
 *  findByUserIdAndTitle(Integer userId, String title): Finds a single deck for a user with the given title.
 *  existsByUserIdAndTitle(Integer userId, String title): Checks if a deck with the given title exists for the user.
 *
 * Very cool
 */

@Repository
public interface DeckRepository extends JpaRepository<Deck, Integer> {

    // Find all decks for a specific user
    List<Deck> findByUserId(Integer userId);

    // Optional: find a deck by user and title
    Deck findByUserIdAndTitle(Integer userId, String title);

    boolean existsByUserIdAndTitle(Integer userId, String title);

}
