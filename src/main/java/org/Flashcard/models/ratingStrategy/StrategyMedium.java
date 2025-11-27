package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.FlashCard;

public class StrategyMedium implements RatingStrategy {
    @Override
    public int calculateNextReviewDate() {
        return 1;
    }
    public void setRating(FlashCard flashCard){

    }
}
