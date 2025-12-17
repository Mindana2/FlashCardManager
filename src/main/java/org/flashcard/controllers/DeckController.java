package org.flashcard.controllers;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.controllers.observer.Observable;   // <-- OBSERVER
import org.flashcard.models.services.DeckService;
import org.flashcard.models.services.FlashCardService;
import org.flashcard.models.timers.CountdownListener;
import org.springframework.stereotype.Controller;
import java.time.Duration;
import java.util.List;
/* We use Spring Data JPA to access the database.
 * This class is annotated with @Controller, which tells Spring
 * that it is a controller-layer component.
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 */
@Controller
public class DeckController {

    private final DeckService deckService;
    private final FlashCardService flashCardService;



    public DeckController(DeckService deckService, FlashCardService flashCardService) {
        this.deckService = deckService;
        this.flashCardService = flashCardService;

    }
    public Observable<List<DeckDTO>> getDecksObservable() {
        return deckService.getDecksObservable();
    }

    public Observable<List<FlashcardDTO>> getFlashcardsObservable() {
        return flashCardService.getFlashcardsObservable();
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


    public void deleteDeck(Integer deckId) {
        deckService.deleteDeck(deckId);
    }

    // --- Flashcard CRUD ---

    public FlashcardDTO addFlashcard(Integer deckId, String front, String back) {
        return flashCardService.addFlashcard(deckId, front, back);
    }

    public boolean deckExists(Integer userId, String title) {
        return deckService.deckExists(userId, title);
    }

    public List<FlashcardDTO> getFlashcardsForDeck(Integer deckId) {
        return flashCardService.getFlashcardsForDeck(deckId);
    }
    

    public void deleteFlashcard(Integer cardId) {
        flashCardService.deleteFlashcard(cardId);
    }

    public void resetDeckProgression(Integer deckId) {
        deckService.resetDeckProgression(deckId);
    }

    // Search / Filter
    public long showEstimatedDate(String rating, int cardID){
        return flashCardService.showEstimatedDate(rating, cardID);
    }

    public Duration timeUntilDue(int deckID){
        return deckService.timeUntilDue(deckID);
    }
    public void updateDeckCards(CountdownListener listener){
        listener.onCountdownFinished();

    }


}
