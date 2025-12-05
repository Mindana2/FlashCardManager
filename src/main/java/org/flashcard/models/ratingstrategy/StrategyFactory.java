package org.flashcard.models.ratingstrategy;


import java.util.List;
/*
 * We use Strategy Pattern to calculate the next review date for a flashcard.
 * Depending on what option the user chooses (AGAIN, HARD, MEDIUM, EASY), we use a different strategy.
 */
public final class StrategyFactory {
    private StrategyFactory(){
    }
    public static RatingStrategy createStrategy(String difficulty){
        return switch (difficulty) {
            case "easy" -> new StrategyEasy();
            case "medium" -> new StrategyMedium();
            case "hard" -> new StrategyHard();
            case "again" -> new StrategyAgain();
            default -> throw new IllegalArgumentException("Unknown rating type: " + difficulty);
        };
    }
    public static List<String> getAllRatingNames(){
        return List.of("easy", "medium", "hard", "again");
    }
}
