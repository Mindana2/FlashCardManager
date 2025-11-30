package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.CardLearningState;
import org.Flashcard.models.dataClasses.FlashCard;

public class StrategyMedium implements RatingStrategy {
    @Override
    public int calculateNextReviewDate(FlashCard card) {
        return 1;
    }
    public void setRating(FlashCard flashCard){

    }
}
