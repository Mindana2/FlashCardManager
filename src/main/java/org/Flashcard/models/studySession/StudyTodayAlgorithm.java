package org.Flashcard.models.studySession;

import org.Flashcard.models.dataClasses.Deck;
import org.Flashcard.models.dataClasses.FlashCard;
import org.Flashcard.models.dataClasses.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudyTodayAlgorithm implements StudyAlgorithm {
    public StudyTodayAlgorithm() {}

    @Override
    public List<FlashCard> prepareCards(Deck deck, User user) {
        return deck.getCards().stream()
                .filter(card -> card.getCardLearningState().isDueToday())
                .collect(Collectors.toList());
    }
}
