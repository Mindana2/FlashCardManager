package org.flashcard.application.dto;

public class DeckDTO {
    private final String title;
    private final int id;
    private final int cardCount;
    private final TagDTO tagDTO;
    private final int dueCount;
    public DeckDTO(String title, int id, int cardCount, TagDTO tagDTO, int dueCount) {
        this.title = title;
        this.id = id;
        this.cardCount = cardCount;
        this.tagDTO = tagDTO;
        this.dueCount = dueCount;
    }
    public String getTitle() { return title; }
    public int getId() { return id; }
    public int getCardCount() { return cardCount; }
    public TagDTO getTagDTO() { return tagDTO; }
    public int getDueCount() { return dueCount; }
}
