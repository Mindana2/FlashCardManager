package org.flashcard.models.studysession;

import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.dataclasses.User;

import java.util.List;

/*
 * This package is responsible for dividing flashcards into sub-decks:
 * Flashcards that are due today or sooner get sorted into one 'pile'.
 * While the option to study all cards remains with StudyAllAlgorithm.
 */

public interface StudyAlgorithm {
    public List<Flashcard> prepareCards(Deck deck, User user);
}
