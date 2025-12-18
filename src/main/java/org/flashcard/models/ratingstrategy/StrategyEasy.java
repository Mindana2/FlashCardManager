package org.flashcard.models.ratingstrategy;

/**
 * Implements an accelerated learning strategy that aggressively increases review intervals by a factor.
 */

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