package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.FlashCard;

import java.time.LocalDate;

public class StrategyAgain implements RatingStrategy {
    @Override
    public int calculateNextReviewDate(FlashCard card) {
        return 1;
    }
    public void updateReviewDate(FlashCard flashCard){
        LocalDate newReviewDate = flashCard.getCardLearningState().getLastReviewDate().plusDays(1);
        flashCard.getCardLearningState().setNextReviewDate(newReviewDate);
    }
}
