package org.flashcard.models.services;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.mapper.DeckMapper;
import org.flashcard.application.mapper.FlashcardMapper;
import org.flashcard.controllers.observer.Observable;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/* We use Spring Data JPA to access the database.
 * This class is annotated with @Service, which tells Spring
 * that it is a service-layer component.
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 * The @Transactional annotation ensures that no database transactions are left unfinished.
 * It automatically aborts any transactions that result in an error.
 * This allows us to write logic without manually handling database transactions.
 */

/**
 * Coordinates the active learning process by managing study sessions, applying
 * spaced repetition ratings to cards, and providing a reactive bridge between
 * the study algorithms and the user interface.
 */

@Service
@Transactional
public class StudyService {

    private final FlashcardRepository flashcardRepository;
    private final DeckRepository deckRepository;
    private final UserRepository userRepository;
    private final Observable<FlashcardDTO> currentCardObservable = new Observable<>();
    private final Observable<Boolean> sessionFinishedObservable = new Observable<>();
    private final Observable<DeckDTO> deckProgressObservable = new Observable<>();

    private StudySession currentSession;

    public StudyService(FlashcardRepository flashcardRepository,
                        DeckRepository deckRepository,
                        UserRepository userRepository
                        ) {
        this.flashcardRepository = flashcardRepository;
        this.deckRepository = deckRepository;
        this.userRepository = userRepository;
    }
    public Observable<FlashcardDTO> getCurrentCardObservable() {
        return currentCardObservable;
    }

    public Observable<Boolean> getSessionFinishedObservable() {
        return sessionFinishedObservable;
    }

    public Observable<DeckDTO> getDeckProgressObservable() {
        return deckProgressObservable;
    }
    public void startSession(String algorithmStrategy, int deckId, int userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Deck currentDeck = deckRepository.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        List<Flashcard> cards = flashcardRepository.findByDeck(currentDeck);
        currentDeck.setCards(cards);

        StudyAlgorithm algorithm = StudyAlgorithmFactory.createAlgorithm(algorithmStrategy.toLowerCase());
        currentSession = new StudySession(currentDeck, currentUser, algorithm);
        currentSession.startSession();
    }

    public FlashcardDTO nextCard() {
        if (currentSession == null) return null;
        Flashcard card = currentSession.getNextCardAndRemove();
        return (card == null) ? null : FlashcardMapper.toDTO(card);
    }

    public void applyRating(String rating, int cardId) {
        Flashcard flashcard = flashcardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard not found"));

        RatingStrategy strategy = StrategyFactory.createStrategy(rating);
        strategy.updateReviewState(flashcard);
        flashcardRepository.save(flashcard);
    }

    public void endSession() {
        if (currentSession != null) {
            currentSession.endSession();
            currentSession = null;

        }
    }

    public DeckDTO getDeckProgress() {
        if (currentSession == null) return null;
        return DeckMapper.toDTO(currentSession.getDeck());
    }
}

