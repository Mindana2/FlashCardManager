package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.CardLearningState;
import org.Flashcard.models.dataClasses.FlashCard;

import java.time.LocalDate;

public class StrategyMedium implements RatingStrategy {
    @Override
    public int calculateNextReviewDate(FlashCard card) {
        return 1;
    }
    public void updateReviewDate(FlashCard flashCard){
        LocalDate newReviewDate = flashCard.getCardLearningState().getLastReviewDate().plusDays(3);
        flashCard.getCardLearningState().setNextReviewDate(newReviewDate);
    }
}
