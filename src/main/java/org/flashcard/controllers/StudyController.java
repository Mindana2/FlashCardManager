package org.flashcard.controllers;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.models.services.DeckService;
import org.flashcard.models.services.StudyService;
import org.springframework.stereotype.Controller;
import org.flashcard.controllers.observer.Observable;
/* We use Spring Data JPA to access the database.
 * This class is annotated with @Controller, which tells Spring
 * that it is a controller-layer component.
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 */

/**
 * This @Controller works as an intermediary between the View and the Service layer.
 * It handles user requests, invokes the appropriate methods in the Service layer,
 * manages the lifecycle of a study session by coordinating flashcard delivery,
 * student rating applications, and real-time session progress via observables,
 * and returns the results back to the View.
 */

@Controller
public class StudyController {

    private final StudyService studyService;

    public StudyController(StudyService studyService) { this.studyService = studyService; }

    public Observable<FlashcardDTO> getCurrentCardObservable() {
        return studyService.getCurrentCardObservable();
    }

    public Observable<Boolean> getSessionFinishedObservable() {
        return studyService.getSessionFinishedObservable();
    }

    public Observable<DeckDTO> getDeckProgressObservable() {
        return studyService.getDeckProgressObservable();
    }

    public void startSession(String algorithmStrategy, int deckId, int userId) {
        studyService.startSession(algorithmStrategy, deckId, userId);

        FlashcardDTO first = studyService.nextCard();
        if (first != null) {
            getCurrentCardObservable().notifyListeners(first);
        } else {
            getSessionFinishedObservable().notifyListeners(true);
        }
    }

    public void applyRating(String rating, int cardId) {
        studyService.applyRating(rating, cardId);
    }

    public void nextCardAndNotify() {
        FlashcardDTO next = studyService.nextCard();
        if (next != null) {
            getCurrentCardObservable().notifyListeners(next);
        } else {
            endSession(true);
            getSessionFinishedObservable().notifyListeners(true);
        }
    }

    public void endSession(boolean notifyView) {
        studyService.endSession();
        if (notifyView) {
            DeckDTO updated = studyService.getDeckProgress();
            if (updated != null) getDeckProgressObservable().notifyListeners(updated);
        }
    }
}
