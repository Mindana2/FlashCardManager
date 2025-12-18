package org.flashcard.application.dto;

/**
 *We use DataTransferObjects to transfer data between layers,
 *this ensures View is read-only.
 *This class represents a Deck Data Transfer Object.
 */

public class DeckDTO {
    private final String title;
    private final int id;
    private final int cardCount;
    private final TagDTO tagDTO;
    private final int dueCount;
    private final double progressPercent; // nytt fält

    public DeckDTO(String title, int id, int cardCount, TagDTO tagDTO, int dueCount, double progressPercent) {
        this.title = title;
        this.id = id;
        this.cardCount = cardCount;
        this.tagDTO = tagDTO;
        this.dueCount = dueCount;
        this.progressPercent = progressPercent;
    }

    public String getTitle() { return title; }
    public int getId() { return id; }
    public int getCardCount() { return cardCount; }
    public TagDTO getTagDTO() { return tagDTO; }
    public int getDueCount() { return dueCount; }
    public double getProgressPercent() { return progressPercent; }
}
