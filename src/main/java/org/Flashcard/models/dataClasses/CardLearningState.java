package org.Flashcard.models.dataClasses;
import org.Flashcard.models.ratingStrategy.RatingStrategy;
import java.time.LocalDate;

public class CardLearningState {
    private int id;
    private LocalDate nextReviewDate;
    private LocalDate lastReviewDate;
    private int numberOfTimesViewed;




    public CardLearningState(LocalDate nextReviewDate,LocalDate lastReviewDate, int numberOfTimesViewed, int id) {
        this.nextReviewDate = nextReviewDate;
        this.lastReviewDate = lastReviewDate;
        this.numberOfTimesViewed = numberOfTimesViewed;
        this.id = id;
    }

    public boolean isDueToday() {
        return !this.nextReviewDate.isAfter(LocalDate.now());   //Compares nextReviewDate with current date
    }
    public void incrementNumberOfTimesViewed() {
        this.numberOfTimesViewed++;

    }

}
