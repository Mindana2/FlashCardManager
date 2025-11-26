package org.Flashcard.models.ratingStrategy;

public class StrategyEasy implements RatingStrategy {
    @Override
    public int calculateNextReviewDate() {
        return 1;
    }
}