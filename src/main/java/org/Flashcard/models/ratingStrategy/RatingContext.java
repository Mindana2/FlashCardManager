package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.FlashCard;

public final class RatingContext {

    private RatingContext() {
    }

    //Kallar på strategins metoder
    public static void executeStrategy(FlashCard flashCard, RatingStrategy ratingStrategy) {

        if (ratingStrategy == null) {
            throw new IllegalArgumentException("ratingStrategy cannot be null");
        }
        ratingStrategy.calculateNextReviewDate(flashCard);
        ratingStrategy.updateReviewDate(flashCard);
    }
}



