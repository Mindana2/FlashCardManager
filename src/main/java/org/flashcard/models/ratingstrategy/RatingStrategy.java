package org.flashcard.models.ratingstrategy;

import org.flashcard.models.dataclasses.Flashcard;

/*
 * We use Strategy Pattern to calculate the next review date for a flashcard.
 * Depending on what option the user chooses (AGAIN, HARD, MEDIUM, EASY), we use a different strategy.
 */
public interface RatingStrategy {
    void updateReviewState(Flashcard flashCard);
}
