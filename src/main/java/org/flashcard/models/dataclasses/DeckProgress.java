package org.flashcard.models.dataclasses;


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
