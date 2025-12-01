package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.FlashCard;

public interface RatingStrategy {
    int calculateNextReviewDate(FlashCard card);
    void updateReviewDate(FlashCard flashCard);
}
