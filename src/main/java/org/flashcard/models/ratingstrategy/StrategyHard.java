package org.flashcard.models.ratingstrategy;

/**
 * Implements a conservative learning strategy that slowly increases review intervals by a factor.
 */

public class StrategyHard extends BaseIntervalStrategy {
    @Override
    public double getMultiplier() {
        return 1.2; // Increases slowly
    }

    @Override
    protected long applyExtremeRules(long daysToAdd) {
        if (daysToAdd > 10) return 10;
        return daysToAdd;
    }
}
