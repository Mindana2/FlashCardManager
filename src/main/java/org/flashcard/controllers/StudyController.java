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
@Controller
public class StudyController {

    private final StudyService studyService;

    public StudyController(StudyService studyService, DeckService deckService) { this.studyService = studyService; }

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
