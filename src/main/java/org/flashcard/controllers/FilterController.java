package org.flashcard.controllers;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.models.services.DeckService;
import org.springframework.stereotype.Controller;

import java.util.List;
/* We use Spring Data JPA to access the database.
 * This class is annotated with @Controller, which tells Spring
 * that it is a controller-layer component.
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 */
@Controller
public class FilterController {

    private final DeckService deckService;

    public FilterController(DeckService deckService) {
        this.deckService = deckService;
    }

    public List<DeckDTO> searchDecks(Integer userId, String searchText, Integer tagId) {
        List<DeckDTO> allDecks = deckService.getAllDecksForUser(userId);

        return allDecks.stream()
                .filter(d -> searchText == null || searchText.isBlank() ||
                        d.getTitle().toLowerCase().contains(searchText.toLowerCase()))
                .filter(d -> tagId == null || (d.getTagDTO() != null && d.getTagDTO().getId() == tagId))
                .toList();
    }

    
}
