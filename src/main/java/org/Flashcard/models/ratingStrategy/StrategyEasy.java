package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.FlashCard;

public class StrategyEasy implements RatingStrategy {
    @Override
    public int calculateNextReviewDate(FlashCard card) {
        return 1;
    }

    @Override
    public void setRating(FlashCard flashCard){
    }
}