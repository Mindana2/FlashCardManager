package org.flashcard.models.ratingstrategy;

/**
 * Implements a conservative learning strategy that slowly increases review intervals by a factor.
 */

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
