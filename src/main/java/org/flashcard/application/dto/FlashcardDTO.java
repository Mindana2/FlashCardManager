package org.flashcard.application.dto;

/**
 *We use DataTransferObjects to transfer data between layers,
 *this ensures View is read-only.
 *This class represents a Flashcard Data Transfer Object.
 */

public class FlashcardDTO {
    private final int id;
    private final String front;
    private final String back;

    public FlashcardDTO(int id, String front, String back) {
        this.id = id;
        this.front = front;
        this.back = back;
    }

    public int getId() { return id; }
    public String getFront() { return front; }
    public String getBack() { return back; }
}
