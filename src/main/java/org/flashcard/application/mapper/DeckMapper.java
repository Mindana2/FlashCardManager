package org.flashcard.application.mapper;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.DeckProgress;

import java.util.List;

/**
 *Our Mapper classes converts our data classes to DTOs.
 * This class maps Deck data class to DeckDTO.
 */

public class DeckMapper {

    public static DeckDTO toDTO(Deck deck, int dueCount) {
        int cardCount = (deck.getCards() != null) ? deck.getCards().size() : 0;
        TagDTO tagDTO = TagMapper.toDTO(deck.getTag());

        double progress = 0.0;
        DeckProgress dp = deck.getDeckProgress();
        if (dp != null) {
            progress = dp.getProgressPercent();
        }

        return new DeckDTO(
                deck.getTitle(),
                deck.getId(),
                cardCount,
                tagDTO,
                dueCount,
                progress
        );
    }

    public static DeckDTO toDTO(Deck deck) {
        return toDTO(deck, 0);
    }


}
