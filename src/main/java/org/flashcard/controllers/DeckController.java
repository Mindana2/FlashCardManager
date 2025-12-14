package org.flashcard.controllers;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.application.mapper.DeckMapper;
import org.flashcard.application.mapper.FlashcardMapper;
import org.flashcard.application.mapper.TagMapper;
import org.flashcard.controllers.observer.Observable;   // <-- OBSERVER
import org.flashcard.models.dataclasses.*;
import org.flashcard.models.progress.FlashcardProgression;
import org.flashcard.models.progress.DeckProgression;
import org.flashcard.models.services.DeckService;
import org.flashcard.models.timers.ReviewCountdownTimer;
import org.flashcard.repositories.DeckRepository;
import org.flashcard.repositories.FlashcardRepository;
import org.flashcard.repositories.TagRepository;
import org.flashcard.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.flashcard.models.ratingstrategy.RatingStrategy;
import org.flashcard.models.ratingstrategy.StrategyFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class DeckController {

    // Observer pattern implementation
    private DeckService deckService;



    private final DeckRepository deckRepo;
    private final FlashcardRepository flashcardRepo;
    private final UserRepository userRepo;
    private final TagRepository tagRepo;


    public DeckController(DeckRepository deckRepo,
                          FlashcardRepository flashcardRepo,
                          UserRepository userRepo,
                          TagRepository tagRepo
                          ) {
        this.deckRepo = deckRepo;
        this.flashcardRepo = flashcardRepo;
        this.userRepo = userRepo;
        this.tagRepo = tagRepo;

    }
    public Observable<List<DeckDTO>> getDecksObservable() {
        return deckService.getDecksObservable();
    }

    public Observable<List<FlashcardDTO>> getFlashcardsObservable() {
        return deckService.getFlashcardsObservable();
    }
    // --- Deck CRUD ---

    public DeckDTO createDeck(Integer userId, String title) {
        return deckService.createDeck(userId, title);
    }


    public TagDTO assignTagToDeck(Integer deckId, Integer tagId) {
        return deckService.assignTagToDeck(deckId, tagId);
    }

    public List<DeckDTO> getAllDecksForUser(Integer userId) {
        return deckService.getAllDecksForUser(userId);
    }


    public DeckDTO getDeckById(Integer deckId) {
        return deckService.getDeckById(deckId);
    }

    public List<DeckDTO> getDueDecksForUser(Integer userId) {
        return deckService.getDueDecksForUser(userId);
    }

    public List<DeckDTO> getNotDueDecksForUser(Integer userId) {
        return deckService.getNotDueDecksForUser(userId);
    }

    public DeckDTO updateDeck(Integer deckId, String newTitle, Integer newTagId) {
        return deckService.updateDeck(deckId, newTitle, newTagId);
    }

    public void deleteDeck(Integer deckId) {
        deckService.deleteDeck(deckId);
    }

    // --- Flashcard CRUD ---

    public FlashcardDTO addFlashcard(Integer deckId, String front, String back) {
        return deckService.addFlashcard(deckId, front, back);
    }

    public boolean deckExists(Integer userId, String title) {
        return deckRepo.existsByUserIdAndTitle(userId, title);
    }

    public List<FlashcardDTO> getFlashcardsForDeck(Integer deckId) {
        return deckService.getFlashcardsForDeck(deckId);
    }

    public FlashcardDTO updateFlashcard(Integer cardId, String newFront, String newBack) {
        return deckService.updateFlashcard(cardId, newFront, newBack);
    }

    public void deleteFlashcard(Integer cardId) {
        deckService.deleteFlashcard(cardId);
    }

    // Search / Filter
    public List<DeckDTO> searchDecks(Integer userId, String searchText, Integer tagId) {
        return deckService.searchDecks(userId, searchText, tagId);
    }
    public long showEstimatedDate(String rating, int cardID){
        return deckService.showEstimatedDate(rating, cardID);
    }

    // Get all decks with due cards at Home view
    public List<DeckDTO> getAllDecksWithDueInfo(Integer userId) {
        List<Deck> userDecks = deckRepo.findByUserIdWithTag(userId);

        return userDecks.stream()
                .map(deck -> {
                    List<Flashcard> cards = flashcardRepo.findByDeck(deck);
                    deck.setCards(cards);

                    double progress = DeckProgression.calculateDeckProgression(deck);
                    deck.setDeckProgress(new DeckProgress(progress));

                    long dueCount = cards.stream()
                            .filter(this::isCardDue)
                            .count();

                    DeckDTO dto = DeckMapper.toDTO(deck, (int) dueCount);
                    return dto;
                })
                .collect(Collectors.toList());
//    public Flashcard getNextReviewableCard(int deckID){
//        return deckService.getNextReviewableCard(deckID);
//    }
    public Duration timeUntilDue(int deckID){
        return deckService.timeUntilDue(deckID);
    }

}
