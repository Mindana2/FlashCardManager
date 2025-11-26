package org.Flashcard.models.ratingStrategy;

public class StrategyMedium implements RatingStrategy {
    @Override
    public int calculateNextReviewDate() {
        return 1;
    }
}
