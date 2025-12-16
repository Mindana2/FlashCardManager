package org.flashcard.models.ratingstrategy;

import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.dataclasses.Flashcard;

import java.time.LocalDate;

public class StrategyMedium extends BaseIntervalStrategy {
    @Override
    public double getMultiplier() {
        return 1.5; //Increases lagom
    }

    @Override
    protected long applyExtremeRules(long daysToAdd) {
        if (daysToAdd > 20) return 20;
        return daysToAdd;
    }
}
