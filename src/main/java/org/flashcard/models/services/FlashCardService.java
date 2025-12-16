package org.flashcard.models.services;


import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.mapper.FlashcardMapper;
import org.flashcard.controllers.observer.Observable;
import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.progress.FlashcardProgression;
import org.flashcard.models.ratingstrategy.RatingStrategy;
import org.flashcard.models.ratingstrategy.StrategyFactory;
import org.flashcard.repositories.DeckRepository;
import org.flashcard.repositories.FlashcardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashCardService {
    private final FlashcardRepository flashcardRepository;
    private final DeckRepository deckRepository;
    private final Observable<List<FlashcardDTO>> flashcardsObservable = new Observable<>();

    public Observable<List<FlashcardDTO>> getFlashcardsObservable() {
        return flashcardsObservable;
    }

    public FlashCardService(FlashcardRepository flashcardRepository, DeckRepository deckRepository) {
        this.flashcardRepository = flashcardRepository;
        this.deckRepository = deckRepository;
    }

    // --- Flashcard CRUD ---

    public FlashcardDTO addFlashcard(Integer deckId, String front, String back) {

        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        boolean exists = flashcardRepository.findByDeckId(deckId).stream()
                .anyMatch(c -> c.getFront().equalsIgnoreCase(front));

        if (exists) {
            throw new IllegalArgumentException("A flashcard with the same front already exists in this deck.");
        }

        Flashcard card = new Flashcard(front, back, deck);
        Flashcard savedCard = flashcardRepository.save(card);

        FlashcardDTO dto = FlashcardMapper.toDTO(savedCard);


        flashcardsObservable.notifyListeners(getFlashcardsForDeck(deckId));

        return dto;
    }
    public List<FlashcardDTO> getFlashcardsForDeck(Integer deckId) {
        List<Flashcard> cards = flashcardRepository.findByDeckId(deckId);
        return cards.stream().map(FlashcardMapper::toDTO).collect(Collectors.toList());
    }
    public FlashcardDTO updateFlashcard(Integer cardId, String newFront, String newBack) {

        Flashcard card = flashcardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard not found"));

        if (newFront != null && !newFront.isBlank()) card.setFront(newFront);
        if (newBack != null && !newBack.isBlank()) card.setBack(newBack);

        Flashcard savedCard = flashcardRepository.save(card);


        flashcardsObservable.notifyListeners(
                getFlashcardsForDeck(savedCard.getDeck().getId())
        );
        // --------------------------------------------------------------------

        return FlashcardMapper.toDTO(savedCard);
    }
    public void deleteFlashcard(Integer cardId) {

        Flashcard card = flashcardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard not found"));

        Integer deckId = card.getDeck().getId();

        flashcardRepository.deleteById(cardId);


        flashcardsObservable.notifyListeners(getFlashcardsForDeck(deckId));

    }
    public long showEstimatedDate(String rating, int cardID){
        Flashcard flashcard = flashcardRepository.findById(cardID)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard not found"));
        RatingStrategy strategy = StrategyFactory.createStrategy(rating);

        CardLearningState state = flashcard.getCardLearningState();
        return FlashcardProgression.estimateDate(strategy, state);
    }

}
