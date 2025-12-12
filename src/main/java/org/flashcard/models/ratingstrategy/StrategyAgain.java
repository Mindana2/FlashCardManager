package org.flashcard.models.ratingstrategy;

import org.flashcard.models.dataclasses.Flashcard;


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
