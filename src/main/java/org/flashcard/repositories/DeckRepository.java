package org.flashcard.repositories;

import org.flashcard.models.dataclasses.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Integer> {

    // Find all decks for a specific user
    List<Deck> findByUserId(Integer userId);

    // Optional: find a deck by user and title
    Deck findByUserIdAndTitle(Integer userId, String title);

    boolean existsByUserIdAndTitle(Integer userId, String title);

}
