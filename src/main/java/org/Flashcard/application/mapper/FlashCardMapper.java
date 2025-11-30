package org.Flashcard.application.mapper;

import org.Flashcard.application.dto.FlashCardDTO;
import org.Flashcard.models.dataClasses.FlashCard;

public class FlashCardMapper {
    public static FlashCardDTO toDTO(FlashCard card) {
        return new FlashCardDTO(
                card.getId(),
                card.getFront(),
                card.getBack(),
                card.getDeckId()
        );
    }
}
