package org.flashcard.models.dataclasses;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
/* Our dataclasses also take use of Spring Framework.
 *
 * @Entity denotes that this class represents the "CardLearningState" table in the database.
 *
 * Spring can then use this class to map between Java objects and database rows:
 *
 * When a repository like UserRepository calls save(), findById(), or delete(), Spring automatically:
 *   1. Reads these annotations to know the table and columns.
 *   2. Generates the appropriate SQL.
 *   3. Maps database rows to CardLearningState objects and vice versa.
 *
 * This helps us reduce the amount of boilerplate SQL we need to write.
 *
 * The idea of CardLearningState is to map each Flashcard to a CardLEarningState.
 * This class holds statistics about how often a Flashcard has been reviewed
 * and when the next review is due.
 *
 */
@Entity
@Table(name = "CardLearningState")
public class CardLearningState {

    @Id
    private Integer flashcardId;

    @OneToOne
    @JoinColumn(name = "flashcard_id")
    @MapsId
    private Flashcard flashcard;

    private LocalDate lastReviewDate;
    private LocalDate nextReviewDate;


    @Column(nullable = false)
    private BigDecimal intervalBetweenReviews = BigDecimal.valueOf(1.0);


    @Column(nullable = false)
    private Integer numberOfTimesViewed = 0;

    // Constructors
    public CardLearningState() {}

    public CardLearningState(Flashcard flashcard) {
        this.flashcard = flashcard;
        this.numberOfTimesViewed = 0;
    }


    //TODO uppdatera så att nya intervalBetweenRevies funkar
    public void updateDates(long daysToAdd) {
        this.lastReviewDate = LocalDate.now();
        this.nextReviewDate = LocalDate.now().plusDays(daysToAdd);
        this.numberOfTimesViewed++;
    }

    // Getters and setters
    public Integer getFlashcardId() { return flashcardId; }
    public void setFlashcardId(Integer flashcardId) { this.flashcardId = flashcardId; }

    public Flashcard getFlashcard() { return flashcard; }
    public void setFlashcard(Flashcard flashcard) { this.flashcard = flashcard; }

    public LocalDate getLastReviewDate() { return lastReviewDate; }
    public void setLastReviewDate(LocalDate lastReviewDate) { this.lastReviewDate = lastReviewDate; }

    public LocalDate getNextReviewDate() { return nextReviewDate; }
    public void setNextReviewDate(LocalDate nextReviewDate) { this.nextReviewDate = nextReviewDate; }

    public BigDecimal getIntervalBetweenReviews() { return intervalBetweenReviews; }
    public void setIntervalBetweenReviews(BigDecimal intervalBetweenReviews) { this.intervalBetweenReviews = intervalBetweenReviews; }

    public Integer getNumberOfTimesViewed() { return numberOfTimesViewed; }
    public void setNumberOfTimesViewed(Integer numberOfTimesViewed) { this.numberOfTimesViewed = numberOfTimesViewed; }

    public boolean isDueToday() {
        return nextReviewDate == null || !nextReviewDate.isAfter(LocalDate.now());
    }

    @Override
    public String toString() {
        return "CardLearningState{" +
                "flashcardId=" + flashcardId +
                ", lastReviewDate=" + lastReviewDate +
                ", nextReviewDate=" + nextReviewDate +
                ", intervalBetweenReviews=" + intervalBetweenReviews +
                ", numberOfTimesViewed=" + numberOfTimesViewed +
                '}';
    }
}