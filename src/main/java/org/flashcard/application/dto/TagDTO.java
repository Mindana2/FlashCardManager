package org.flashcard.application.dto;


public class TagDTO {
    private final int id;
    private final String title;
    private final String colorHex; // T.ex. "#FF5733"

    public TagDTO(int id, String title, String colorHex) {
        this.id = id;
        this.title = title;
        this.colorHex = colorHex;
    }

    public String getTitle() { return title; }
    public String getColorHex() { return colorHex; }
    public int getId() { return id; }

    @Override
    public String toString() {
        return title;   // or getUsername()
    }


}