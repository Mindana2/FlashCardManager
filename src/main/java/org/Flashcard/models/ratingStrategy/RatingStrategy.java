package org.Flashcard.models.ratingStrategy;

import org.Flashcard.models.dataClasses.FlashCard;

public interface RatingStrategy {
    int calculateNextReviewDate();
    void setRating(FlashCard flashCard);
}
