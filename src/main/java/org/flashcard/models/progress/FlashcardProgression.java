package org.flashcard.models.progress;

import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.ratingstrategy.BaseIntervalStrategy;
import org.flashcard.models.ratingstrategy.RatingStrategy;

import java.util.Objects;


public abstract class FlashcardProgression {


    public static long estimateDate(String rating, CardLearningState state) {
        long currentInterval = 0;
        if (state.getLastReviewDate() != null && state.getNextReviewDate() != null) {
            currentInterval = java.time.temporal.ChronoUnit.DAYS.between(state.getLastReviewDate(), state.getNextReviewDate());
        }
        if (currentInterval <= 0) currentInterval = 1;

        double multiplier = getMultiplier(rating);
        return Math.round(currentInterval * multiplier);

    }

    public static double getMultiplier(String rating) {
        return switch (rating) {
            case "easy" -> 2.5;
            case "medium" -> 1.5;
            case "hard" -> 1.2;
            case "again" -> 0;
            default -> throw new IllegalArgumentException("Unknown rating type: " + rating);
        };
    }

}

