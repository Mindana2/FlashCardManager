package org.Flashcard.models.ratingStrategy;

public class RatingContext {
    private RatingStrategy strategy;

    public RatingContext(RatingStrategy strategy) {
    }

    public void setStrategy(RatingStrategy strategy){
        this.strategy = strategy;
    }
    public void executeStrategy() {
        this.strategy.calculateNextReviewDate();
    }
}



