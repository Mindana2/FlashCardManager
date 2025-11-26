package org.Flashcard.models.ratingStrategy;

public class StrategyHard implements RatingStrategy {
    @Override
    public int calculateNextReviewDate() {
        return 1;
    }
}
