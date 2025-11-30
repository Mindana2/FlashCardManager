package org.Flashcard.application.dto;

public class FlashCardDTO {
    private final int id;
    private final String front;
    private final String back;
    private final int deckId;

    public FlashCardDTO(int id, String front, String back, int deckId) {
        this.id = id;
        this.front = front;
        this.back = back;
        this.deckId = deckId;
    }

    public int getId() { return id; }
    public String getFront() { return front; }
    public String getBack() { return back; }
    public int getDeckId() { return deckId; }
}
