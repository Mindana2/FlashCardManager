package org.flashcard.controllers;

import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.mapper.FlashcardMapper;
import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.dataclasses.User;
import org.flashcard.models.ratingstrategy.RatingStrategy;
import org.flashcard.models.ratingstrategy.StrategyFactory;
import org.flashcard.models.studysession.StudyAlgorithm;
import org.flashcard.models.studysession.StudyAlgorithmFactory;
import org.flashcard.models.studysession.StudySession;
import org.flashcard.repositories.DeckRepository;
import org.flashcard.repositories.FlashcardRepository;
import org.flashcard.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@Transactional
public class StudyController {

    private final FlashcardRepository flashcardRepository;
    private final DeckRepository deckRepository;
    private final UserRepository userRepository;

    private StudySession currentSession;

    public StudyController(FlashcardRepository flashcardRepository,
                           DeckRepository deckRepository,
                           UserRepository userRepository) {
        this.flashcardRepository = flashcardRepository;
        this.deckRepository = deckRepository;
        this.userRepository = userRepository;
    }

    // Start a study session for a given deck and user
    public void startSession(String algorithmStrategy, int deckId, int userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Deck currentDeck = deckRepository.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        // Load flashcards eagerly if needed
        List<Flashcard> cards = flashcardRepository.findByDeck(currentDeck);
        currentDeck.setCards(cards);


        StudyAlgorithm algorithm = StudyAlgorithmFactory.createAlgorithm(algorithmStrategy.toLowerCase());
        currentSession = new StudySession(currentDeck, currentUser, algorithm);
        currentSession.startSession();
    }

    // Apply a rating to a specific card in the current session
    public void applyRating(String rating, int cardId) {
        Flashcard flashcard = flashcardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard not found"));

        RatingStrategy selectedStrategy = StrategyFactory.createStrategy(rating);
        selectedStrategy.updateReviewState(flashcard);
        flashcardRepository.save(flashcard); // persist rating changes
    }

    public FlashcardDTO nextCard(){
        Flashcard flashCard = currentSession.getNextCardAndRemove();

        if (flashCard == null) {
            return null; // Tells the view that the session is over
        }
        return FlashcardMapper.toDTO(flashCard);
    }
    public void endSession() {
        this.currentSession = null;
    }

}
