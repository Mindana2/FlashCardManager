package org.flashcard.models.progress;

import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Provides utility logic to calculate a deck's overall mastery percentage by analyzing
 * the median review interval of its cards against a 30-day target threshold.
 */

public class DeckProgression {

    private static final int TARGET_PROGRESS_DAYS = 30;

    /** Computes progression for a deck (0-100) */
    public static double calculateDeckProgression(Deck deck) {
        if (deck == null) return 0.0;

        List<Flashcard> cards = deck.getCards();
        if (cards == null || cards.isEmpty()) return 0.0;

        List<Long> intervals = new ArrayList<>();

        for (Flashcard card : cards) {
            if (card == null) continue;
            CardLearningState state = card.getCardLearningState();
            if (state != null) {
                intervals.add(getIntervalDays(state));
            }
        }

        if (intervals.isEmpty()) return 0.0;

        intervals.sort(Comparator.naturalOrder());
        long medianInterval = calculateMedian(intervals);
        double progressRatio = (double) medianInterval / TARGET_PROGRESS_DAYS;

        return Math.min(progressRatio * 100.0, 100.0);
    }

    private static long getIntervalDays(CardLearningState state) {
        if (state == null || state.getLastReviewDate() == null || state.getNextReviewDate() == null)
            return 0;
        return ChronoUnit.DAYS.between(state.getLastReviewDate(), state.getNextReviewDate());
    }

    private static long calculateMedian(List<Long> sortedNumbers) {
        int size = sortedNumbers.size();
        if (size % 2 == 1) return sortedNumbers.get(size / 2);
        long mid1 = sortedNumbers.get(size / 2 - 1);
        long mid2 = sortedNumbers.get(size / 2);
        return Math.round((mid1 + mid2) / 2.0);
    }
}
