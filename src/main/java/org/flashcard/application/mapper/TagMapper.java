package org.flashcard.application.mapper;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Tag;

import java.util.List;

public class TagMapper {
    public static TagDTO toDTO(Tag tag) {
        if (tag == null) {
            return null; // If the deck does not have any tag
        }
        return new TagDTO(
                tag.getId(),
                tag.getTitle(),
                tag.getColor()
        );
    }

    public static List<TagDTO> toDTOList(List<Tag> tags) {
        return tags.stream().map(TagMapper::toDTO).toList();
    }
}

