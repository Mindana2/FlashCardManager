package org.Flashcard.models.studySession;

import org.Flashcard.models.Deck;
import org.Flashcard.models.FlashCard;
import org.Flashcard.models.User;

import java.util.List;

public interface StudyAlgorithm {
    public List<FlashCard> prepareCards(Deck deck, User user);
}
