package org.flashcard.repositories;

import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
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
 *  findByDeckId(deckId)     :  Returns a list of all flashcards belonging to a specific deck.
 *  countByDeckId(deckId)    :  Counts the cards in a deck.
 *
 * Very cool
 */
@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {

    // Fetch all flashcards for a specific deck
    List<Flashcard> findByDeckId(Integer deckId);

    long countByDeckId(Integer deckId);

    List<Flashcard> findByDeck(Deck deck);  // returns all flashcards for a deck

}
