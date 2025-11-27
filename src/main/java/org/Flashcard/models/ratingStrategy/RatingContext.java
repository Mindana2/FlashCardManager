package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.FlashCard;

public class RatingContext {
    private RatingStrategy strategy;

    public RatingContext(RatingStrategy strategy) {
        this.strategy = new StrategyMedium();
    }

    public void setStrategy(RatingStrategy strategy){
        this.strategy = strategy;
    }
    public void executeStrategy(FlashCard flashCard) {
        strategy.calculateNextReviewDate();
        strategy.setRating(flashCard);
    }
}



