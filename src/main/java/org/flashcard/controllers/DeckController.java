package org.flashcard.controllers;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.controllers.observer.Observable;   // <-- OBSERVER
import org.flashcard.models.dataclasses.*;
import org.flashcard.models.services.DeckService;
import org.flashcard.repositories.DeckRepository;
import org.springframework.stereotype.Controller;
import java.time.Duration;
import java.util.List;

@Controller
public class DeckController {

    // Observer pattern implementation
    private final DeckService deckService;



    private final DeckRepository deckRepo;



    public DeckController(DeckRepository deckRepo, DeckService deckService) {
        this.deckService = deckService;
        this.deckRepo = deckRepo;

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
    public long showEstimatedDate(String rating, int cardID){
        return deckService.showEstimatedDate(rating, cardID);
    }

//    // Get all decks with due cards at Home view
//    public List<DeckDTO> getAllDecksWithDueInfo(Integer userId) {
//        return deckService.getAllDecksWithDueInfo(userId);
//    }
//    public List<DeckDTO> getNotDueDecksForUser(int userID){
//        return deckService.getNotDueDecksForUser(userID);
//    }
//    public Flashcard getNextReviewableCard(int deckID){
//        return deckService.getNextReviewableCard(deckID);
//    }
    public Duration timeUntilDue(int deckID){
        return deckService.timeUntilDue(deckID);
    }

}
