package org.flashcard.models.ratingstrategy;

import org.flashcard.models.dataclasses.Flashcard;

/**
 * Implements a failure-based review strategy that resets the card's learning schedule,
 * ensuring it is flagged for immediate re-study on the same day.
 */

public class StrategyAgain implements RatingStrategy {
    @Override
    public void updateReviewState(Flashcard flashCard) {           //Reset state for card
        flashCard.getCardLearningState().updateDates(0); // 0 days, show again today
    }

    @Override
    public double getMultiplier() {
        return 0;
    }

}
