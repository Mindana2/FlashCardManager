package org.flashcard.models.studysession;

import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.dataclasses.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a comprehensive study strategy that prepares every flashcard within
 * a deck for review, regardless of its current mastery level or due date.
 */

public class StudyAllAlgorithm implements StudyAlgorithm {
    public StudyAllAlgorithm() {}

    @Override
    public List<Flashcard> prepareCards(Deck deck, User user) {
        return new ArrayList<>(deck.getCards());
    }
}
