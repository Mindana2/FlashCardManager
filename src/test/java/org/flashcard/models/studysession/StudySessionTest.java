package org.flashcard.models.studysession;

import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.dataclasses.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudySessionTest {

    Deck deck;
    User user;
    StudyAlgorithm studyAlgorithm;
    StudySession session;
    Flashcard card1, card2;

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

        // Create session
        session = new StudySession(deck, user, studyAlgorithm);
    }
    @Test
    void testStudySessionConstruction() {
        StudyAlgorithm algo = new StudyAllAlgorithm();
        session = new StudySession(deck, user, algo);

        assertEquals(deck, session.getDeck());
    }
    @Test
    void testStartSessionPreparesCards() {
        StudyAlgorithm algo = new StudyAllAlgorithm();
        session = new StudySession(deck, user, algo);
        session.startSession();

        assertEquals(card1, session.getNextCardAndRemove());
        assertEquals(card2, session.getNextCardAndRemove());
        assertNull(session.getNextCardAndRemove());
    }

}