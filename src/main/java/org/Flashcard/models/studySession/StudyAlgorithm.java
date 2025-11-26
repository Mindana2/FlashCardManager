package org.Flashcard.models.studySession;

import org.Flashcard.models.dataClasses.Deck;
import org.Flashcard.models.dataClasses.FlashCard;
import org.Flashcard.models.dataClasses.User;

import java.util.List;

public interface StudyAlgorithm {
    public List<FlashCard> prepareCards(Deck deck, User user);
}
