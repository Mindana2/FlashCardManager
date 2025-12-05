package org.flashcard.application.mapper;

import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.models.dataclasses.Flashcard;
//Our Mapper classes converts our data classes to DTOs
public class FlashcardMapper {
    public static FlashcardDTO toDTO(Flashcard card) {
        return new FlashcardDTO(
                card.getId(),
                card.getFront(),
                card.getBack()
        );
    }
}
