package org.Flashcard.application.dto;

import java.util.List;

public class DeckDTO {
    private final String title;
    private final int id;
    private final int ownerUserId;
    private final int tagId;
    public DeckDTO(String title, int id, int ownerUserId, int tagId) {
        this.title = title;
        this.id = id;
        this.ownerUserId = ownerUserId;
        this.tagId = tagId;
    }
    public String getTitle() { return title; }
    public int getId() { return id; }

    public int getOwnerUserId() { return ownerUserId; }
    public int getTagId() { return tagId; }
}
