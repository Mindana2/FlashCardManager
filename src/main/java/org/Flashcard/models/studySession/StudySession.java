package org.Flashcard.models.studySession;

import org.Flashcard.models.dataClasses.Deck;
import org.Flashcard.models.dataClasses.FlashCard;
import org.Flashcard.models.dataClasses.User;

import java.util.List;

public class StudySession {
    private List<FlashCard> cards;
    private final Deck deck;
    private final User user;

    private final StudyAlgorithm studyAlgorithm;

    public StudySession(Deck deck, User user, StudyAlgorithm studyAlgorithm) {

        this.studyAlgorithm = studyAlgorithm;

        this.deck = deck;
        this.user = user;
    }

    public void startSession(){
        cards = studyAlgorithm.prepareCards(deck, user);
    }

    public FlashCard getNextCardAndRemove() {
        if (cards == null || cards.isEmpty()) return null;
        return cards.remove(0);
    }
}
