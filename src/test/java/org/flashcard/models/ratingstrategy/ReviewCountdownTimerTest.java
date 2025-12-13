package org.flashcard.models.ratingstrategy;

import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.dataclasses.Flashcard;


import org.flashcard.models.timers.ReviewCountdownTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class ReviewCountdownTimerTest {
    private Flashcard flashcard;
    private CardLearningState state;
    private ReviewCountdownTimer timer;


    @BeforeEach
    void setUp() {

        flashcard = new Flashcard();
        state = new CardLearningState();
        timer = new ReviewCountdownTimer();
        flashcard.setCardLearningState(state);
        state.setFlashcard(flashcard);
    }

    @Test
    void testingCountdownFeature(){
        flashcard.getCardLearningState().setNextReviewDate(LocalDateTime.of(2025, 12, 13, 20, 16, 0));
        timer.startCountdown(flashcard);


    }
}
