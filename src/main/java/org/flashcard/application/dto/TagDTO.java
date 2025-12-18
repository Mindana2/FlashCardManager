package org.flashcard.application.dto;

import java.awt.*;

/**
 *We use DataTransferObjects to transfer data between layers,
 *this ensures View is read-only.
 *This class represents a Tag Data Transfer Object.
 */

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

    // TODO convert to Color object here instead of in the viewclasses
    public Color getColor() {
        if (colorHex == null || colorHex.isEmpty()) {
            return Color.WHITE; // or a default
        }

        // Ensure the string starts with a '#'
        String hex = colorHex.startsWith("#") ? colorHex : "#" + colorHex;

        return Color.decode(hex);
    }



    public int getId() { return id; }

    @Override
    public String toString() {
        return title;   // or getUsername()
    }


}