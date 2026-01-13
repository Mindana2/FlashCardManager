package org.flashcard.repositories;

import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {

    // Fetch all flashcards for a specific deck
    List<Flashcard> findByDeckId(Integer deckId);
    List<Flashcard> findByDeck(Deck deck);  // returns all flashcards for a deck

}
