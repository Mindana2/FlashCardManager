package org.flashcard.controllers;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.models.services.DeckService;
import org.springframework.stereotype.Controller;

import java.util.List;

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

    public List<DeckDTO> getDueDecksForUser(Integer userId) {
        return deckService.getDueDecksForUser(userId);
    }

    public List<DeckDTO> getNotDueDecksForUser(Integer userId) {
        return deckService.getNotDueDecksForUser(userId);
    }
}
