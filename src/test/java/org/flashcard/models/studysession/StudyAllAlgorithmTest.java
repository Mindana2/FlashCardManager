package org.flashcard.models.studysession;

import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.dataclasses.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudyAllAlgorithmTest {
    Deck deck;
    User user;
    Flashcard card1, card2;
    StudyAllAlgorithm algo;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        user = new User();
        card1 = new Flashcard();
        card2 = new Flashcard();

        List<Flashcard> flashcards = new ArrayList<>();
        flashcards.add(card1);
        flashcards.add(card2);
        deck.setCards(flashcards);

        algo = new StudyAllAlgorithm();
    }
    @Test
    void testAllCardsAreReturned() {
        List<Flashcard> result = algo.prepareCards(deck, user);

        assertEquals(2, result.size());
        assertTrue(result.contains(card1));
        assertTrue(result.contains(card2));
    }
}