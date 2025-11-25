package org.Flashcard.models.studySession;

import org.Flashcard.models.Deck;
import org.Flashcard.models.FlashCard;
import org.Flashcard.models.User;

import java.util.List;

public class StudyAllAlgorithm implements StudyAlgorithm {
    public StudyAllAlgorithm() {}

    @Override
    public List<FlashCard> prepareCards(Deck deck, User user) {
        return deck.getCards();
    }
}
