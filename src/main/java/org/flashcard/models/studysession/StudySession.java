package org.flashcard.models.studysession;

import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.dataclasses.User;

import java.util.List;
/*
 * This package is responsible for dividing flashcards into sub-decks:
 * Flashcards that are due today or sooner get sorted into one 'pile'.
 * While the option to study all cards remains with StudyAllAlgorithm.
 *
 * This package is also responsible for creating a study session.
 *
 */

public class StudySession {
    private List<Flashcard> cards;
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

    public Flashcard getNextCardAndRemove() {
        if (cards == null || cards.isEmpty()) return null;
        return cards.remove(0);
    }
}
