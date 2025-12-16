package org.flashcard.models.ratingstrategy;

import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.dataclasses.Flashcard;

public abstract class BaseIntervalStrategy implements RatingStrategy {

    @Override
    public void updateReviewState(Flashcard flashCard) {
        CardLearningState state = flashCard.getCardLearningState();

        long daysToAdd = calculateDays(state, getMultiplier());

        daysToAdd = applyExtremeRules(daysToAdd);

        state.updateDates(daysToAdd);
    }

    protected abstract long applyExtremeRules(long daysToAdd);

    //Helper method
    public static long calculateDays(CardLearningState state, double multiplier) {
        long currentInterval = 0;
        if (state.getLastReviewDate() != null && state.getNextReviewDate() != null) {
            currentInterval = java.time.temporal.ChronoUnit.DAYS.between(state.getLastReviewDate(), state.getNextReviewDate());
        }
        if (currentInterval <= 0) currentInterval = 1;

        return Math.round(currentInterval * multiplier);
    }

    public abstract double getMultiplier();
}
