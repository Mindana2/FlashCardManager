package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.FlashCard;

public class RatingContext {
    private RatingStrategy strategy;

    public RatingContext() {
        //Default rating på kortet blir null, men kommer ändras under applikationens gång
        this.strategy = null;
    }
    //Väljer strategi
    public void setStrategy(RatingStrategy strategy){
        this.strategy = strategy;
    }

    //Kallar på strategins metoder
    public void executeStrategy(FlashCard flashCard) {
        strategy.calculateNextReviewDate(flashCard);
        strategy.setRating(flashCard);
    }
}



