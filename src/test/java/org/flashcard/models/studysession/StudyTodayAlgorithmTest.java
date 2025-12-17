package org.flashcard.models.studysession;

import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.dataclasses.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudyTodayAlgorithmTest {
    Deck deck;
    User user;
    Flashcard cardDue, cardNotDue;
    StudyTodayAlgorithm algo;
    CardLearningState dueState, notDueState;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        user = new User();
        cardDue = new Flashcard();
        cardNotDue = new Flashcard();

        dueState = new CardLearningState();
        notDueState = new CardLearningState();

        dueState.setNextReviewDate(LocalDateTime.now());
        notDueState.setNextReviewDate(LocalDateTime.now().plusDays(1));

        cardDue.setCardLearningState(dueState);
        cardNotDue.setCardLearningState(notDueState);


        List<Flashcard> flashcards = new ArrayList<>();
        flashcards.add(cardDue);
        flashcards.add(cardNotDue);
        deck.setCards(flashcards);

        algo = new StudyTodayAlgorithm();
    }
    @Test
    void testOnlyDueCardsReturned() {
        List<Flashcard> result = algo.prepareCards(deck, user);

        assertEquals(1, result.size());
        assertTrue(result.contains(cardDue));
        assertFalse(result.contains(cardNotDue));
    }
}