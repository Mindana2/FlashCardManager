package org.flashcard.models.studysession;

import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.dataclasses.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements a focused study strategy that filters a deck to retrieve only the
 * flashcards that are currently due for review based on their spaced repetition schedule.
 */

public class StudyTodayAlgorithm implements StudyAlgorithm {
    public StudyTodayAlgorithm() {}

    @Override
    public List<Flashcard> prepareCards(Deck deck, User user) {
        return deck.getCards().stream()
                .filter(card -> card.getCardLearningState() != null && card.getCardLearningState().isDueToday())
                .collect(Collectors.toList());
    }
}
