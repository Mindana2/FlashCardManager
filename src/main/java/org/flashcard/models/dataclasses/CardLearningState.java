package org.flashcard.models.dataclasses;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "CardLearningState")
public class CardLearningState {

    @Id
    private Integer flashcardId;

    @OneToOne
    @JoinColumn(name = "flashcard_id")
    @MapsId
    private Flashcard flashcard;

    @Column(name = "last_review_date")
    private LocalDateTime lastReviewDate;

    @Column(name = "next_review_date")
    private LocalDateTime nextReviewDate;

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

    // Update review dates using LocalDateTime
    public void updateDates(long daysToAdd) {
        this.lastReviewDate = LocalDateTime.now();
        this.nextReviewDate = LocalDateTime.now().plusDays(daysToAdd);
        this.numberOfTimesViewed++;
    }

    // Getters and setters
    public Integer getFlashcardId() { return flashcardId; }
    public void setFlashcardId(Integer flashcardId) { this.flashcardId = flashcardId; }

    public Flashcard getFlashcard() { return flashcard; }
    public void setFlashcard(Flashcard flashcard) { this.flashcard = flashcard; }

    public LocalDateTime getLastReviewDate() { return lastReviewDate; }
    public void setLastReviewDate(LocalDateTime lastReviewDate) { this.lastReviewDate = lastReviewDate; }

    public LocalDateTime getNextReviewDate() { return nextReviewDate; }
    public void setNextReviewDate(LocalDateTime nextReviewDate) { this.nextReviewDate = nextReviewDate; }

    public BigDecimal getIntervalBetweenReviews() {
        intervalBetweenReviews = BigDecimal.valueOf(ChronoUnit.SECONDS.between(lastReviewDate, nextReviewDate));
        return intervalBetweenReviews;
    }
    public void setIntervalBetweenReviews(BigDecimal intervalBetweenReviews) { this.intervalBetweenReviews = intervalBetweenReviews; }

    public Integer getNumberOfTimesViewed() { return numberOfTimesViewed; }
    public void setNumberOfTimesViewed(Integer numberOfTimesViewed) { this.numberOfTimesViewed = numberOfTimesViewed; }

    // Check if card is due today or earlier
    public boolean isDueToday(LocalDateTime now) {
        return nextReviewDate == null || !nextReviewDate.isAfter(now);
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
