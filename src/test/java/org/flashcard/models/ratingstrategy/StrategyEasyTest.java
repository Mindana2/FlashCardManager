package org.flashcard.models.ratingstrategy;

import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.dataclasses.Flashcard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
class StrategyEasyTest {

    private StrategyEasy strategy;
    private Flashcard flashcard;
    private CardLearningState state;

    @BeforeEach
    void setUp() {
        strategy = new StrategyEasy();

        flashcard = new Flashcard();
        state = new CardLearningState();
        flashcard.setCardLearningState(state);
        state.setFlashcard(flashcard);
    }

    @Test
    void updateReviewState_FirstTimeReview_ShouldSetNextReviewCorrectly() {
        assertNull(state.getLastReviewDate());
        assertNull(state.getNextReviewDate());
        assertEquals(0, state.getNumberOfTimesViewed());

        strategy.updateReviewState(flashcard);

        assertNotNull(state.getLastReviewDate());
        assertNotNull(state.getNextReviewDate());

        // Eftersom intervallet blir minst 1 dag, multiplicerat med 2.5 ≈ 3 dagar
        long expectedInterval = 3;
        long actualInterval = ChronoUnit.DAYS.between(state.getLastReviewDate(), state.getNextReviewDate());
        assertEquals(expectedInterval, actualInterval);

        assertEquals(1, state.getNumberOfTimesViewed());
    }

    @Test
    void updateReviewState_WithPreviousReview_ShouldMultiplyInterval() {
        LocalDate lastReview = LocalDate.now().minusDays(4);
        LocalDate nextReview = LocalDate.now();
        state.setLastReviewDate(lastReview);
        state.setNextReviewDate(nextReview);
        state.setNumberOfTimesViewed(2);

        strategy.updateReviewState(flashcard);

        long previousInterval = ChronoUnit.DAYS.between(lastReview, nextReview); // 4 dagar
        long expectedInterval = Math.round(previousInterval * 2.5); // 4 * 2.5 ≈ 10 dagar

        long actualInterval = ChronoUnit.DAYS.between(state.getLastReviewDate(), state.getNextReviewDate());
        assertEquals(expectedInterval, actualInterval);

        assertEquals(3, state.getNumberOfTimesViewed());
    }
}