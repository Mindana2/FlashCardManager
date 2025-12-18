package org.flashcard.models.progress;

import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.ratingstrategy.BaseIntervalStrategy;
import org.flashcard.models.ratingstrategy.RatingStrategy;

/**
 * A utility class that estimates the next review date for a flashcard by applying
 * a specific rating strategy's multiplier to the card's current learning state.
 */

public abstract class FlashcardProgression {

    public static long estimateDate(RatingStrategy strategy, CardLearningState state) {
        return BaseIntervalStrategy.calculateDays(state, strategy.getMultiplier());
    }

}

