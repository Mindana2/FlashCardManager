package org.Flashcard.models.dataClasses;
import org.Flashcard.models.ratingStrategy.RatingStrategy;
import java.time.LocalDate;

public class CardLearningState {
    private LocalDate nextReviewDate;
    private LocalDate lastReviewDate;
    private int numberOfTimesViewd;


    public CardLearningState(LocalDate nextReviewDate,LocalDate lastReviewDate, int numberOfTimesViewd) {
        this.nextReviewDate = nextReviewDate;
        this.lastReviewDate = lastReviewDate;
        this.numberOfTimesViewd = numberOfTimesViewd;
    }

}
