package org.Flashcard.models;

import java.time.LocalDate;

public class CardLearningState {
    private LocalDate nextReviewDate;
    private double easeFactor;
    private LocalDate lastReviewDate;
    //private RatingStrategy lastRating

    public CardLearningState(LocalDate nextReviewDate,LocalDate lastReviewDate, double easeFactor) {
        this.nextReviewDate = nextReviewDate;
        this.easeFactor = easeFactor;
    }

}
