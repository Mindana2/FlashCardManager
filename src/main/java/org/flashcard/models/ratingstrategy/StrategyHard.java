package org.flashcard.models.ratingstrategy;

import org.flashcard.models.dataclasses.Flashcard;

import java.time.LocalDate;

public class StrategyHard extends BaseIntervalStrategy {
    @Override
    public double getMultiplier() {
        return 1.2; // Increases slowly
    }
}
