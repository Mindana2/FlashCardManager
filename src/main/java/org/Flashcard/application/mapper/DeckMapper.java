package org.Flashcard.application.mapper;


import org.Flashcard.application.dto.DeckDTO;
import org.Flashcard.models.dataClasses.Deck;

public class DeckMapper {
    public static DeckDTO toDTO(Deck deck) {
        return new DeckDTO(
                deck.getTitle(),
                deck.getId(),
                deck.getOwnerUserId(),
                deck.getTagId()
        );
    }
}
