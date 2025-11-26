package org.Flashcard.models.studySession;

import org.Flashcard.models.dataClasses.Deck;
import org.Flashcard.models.dataClasses.FlashCard;
import org.Flashcard.models.dataClasses.User;

import java.util.List;

public class StudyAllAlgorithm implements StudyAlgorithm {
    public StudyAllAlgorithm() {}

    @Override
    public List<FlashCard> prepareCards(Deck deck, User user) {
        return deck.getCards();
    }
}
