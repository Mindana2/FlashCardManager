package org.flashcard.models.progress;

import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.ratingstrategy.BaseIntervalStrategy;
import org.flashcard.models.ratingstrategy.RatingStrategy;

public abstract class FlashcardProgression {

    public static long estimateDate(RatingStrategy strategy, CardLearningState state) {
        return BaseIntervalStrategy.calculateDays(state, strategy.getMultiplier());
    }

}

