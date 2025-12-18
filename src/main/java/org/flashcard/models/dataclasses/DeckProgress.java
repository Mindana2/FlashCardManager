package org.flashcard.models.dataclasses;

/**
 * A simple data carrier used to represent the current study completion percentage
 * of a deck, typically used for updating progress bars in the user interface.
 */

public class DeckProgress {
    private final double progressPercent; // 0.0 - 100.0

    public DeckProgress(double progressPercent) {
        this.progressPercent = progressPercent;
    }

    public double getProgressPercent() { return progressPercent; }

    @Override
    public String toString() {
        return "DeckProgress{" +
                "progressPercent=" + progressPercent +
                '}';
    }
}
