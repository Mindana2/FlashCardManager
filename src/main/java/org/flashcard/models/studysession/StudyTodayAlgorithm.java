package org.flashcard.models.studysession;

import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.dataclasses.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StudyTodayAlgorithm implements StudyAlgorithm {
    public StudyTodayAlgorithm() {}

    @Override
    public List<Flashcard> prepareCards(Deck deck, User user) {
        LocalDateTime now = LocalDateTime.now();
        return deck.getCards().stream()
                .filter(card -> card.getCardLearningState() != null && card.getCardLearningState().isDueToday(now))
                .collect(Collectors.toList());
    }
}
