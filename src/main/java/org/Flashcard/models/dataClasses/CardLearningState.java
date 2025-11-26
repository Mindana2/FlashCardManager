package org.Flashcard.models.dataClasses;

import java.time.LocalDate;

public class CardLearningState {
    private LocalDate nextReviewDate;
    private LocalDate lastReviewDate;
    private int numberOfTimesViewd;
    //private RatingStrategy lastRating

    public CardLearningState(LocalDate nextReviewDate,LocalDate lastReviewDate, int numberOfTimesViewd) {
        this.nextReviewDate = nextReviewDate;
        this.lastReviewDate = lastReviewDate;
        this.numberOfTimesViewd = numberOfTimesViewd;
    }

}
