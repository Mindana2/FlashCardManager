package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.FlashCard;

public class RatingContext {
    private RatingStrategy strategy;

    public RatingContext(RatingStrategy strategy) {

        //Default rating på kortet blir Medium, kanske kan ändras till något neutralt.
        this.strategy = new StrategyMedium();
    }
    //Väljer strategi
    public void setStrategy(RatingStrategy strategy){
        this.strategy = strategy;
    }

    //Kallar på strategins metoder
    public void executeStrategy(FlashCard flashCard) {
        strategy.calculateNextReviewDate();
        strategy.setRating(flashCard);
    }
}



