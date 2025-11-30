package org.Flashcard.models.dataClasses;

public class FlashCard {
    private int id;
    private String front;
    private String back;
    private int deckId;
    private CardLearningState cardLearningState;

    // Constructors
    public FlashCard() {}

    public FlashCard(int id, String front, String back, int deckId) {
        if (front == null || front.isBlank()) throw new IllegalArgumentException("Front text required");
        if (back == null || back.isBlank()) throw new IllegalArgumentException("Back text required");

        this.id = id;
        this.front = front;
        this.back = back;
        this.deckId = deckId;
        this.cardLearningState = cardLearningState;
    }

    public FlashCard(String front, String back, int deckId) {
        this.front = front;
        this.back = back;
        this.deckId = deckId;
    }

    public CardLearningState getCardLearningState() { //TODO
        return cardLearningState;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFront() { return front; }
    public void setFront(String front) { this.front = front; }

    public String getBack() { return back; }
    public void setBack(String back) { this.back = back; }

    public int getDeckId() { return deckId; }
    public void setDeckId(int deckId) { this.deckId = deckId; }

    @Override
    public String toString() {
        return "FlashCard{id=" + id + ", front='" + front + "', back='" + back + "', deckId=" + deckId + "}";
    }
}
