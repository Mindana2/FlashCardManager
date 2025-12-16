package org.flashcard.models.ratingstrategy;

import org.flashcard.models.dataclasses.Flashcard;

import java.time.LocalDate;

public class StrategyEasy extends BaseIntervalStrategy {
    @Override
    public double getMultiplier() {
        return 2.5; //Fast increase
    }
    @Override
    protected long applyExtremeRules(long daysToAdd) {
        if (daysToAdd > 60) return 60;
        return daysToAdd;
    }
}