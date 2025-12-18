package org.flashcard.models.ratingstrategy;


import java.util.List;
/**
 * Implements the Factory pattern to decouple strategy instantiation from the client,
 * providing the correct RatingStrategy implementation based on the user's difficulty input.
 * (AGAIN, HARD, MEDIUM, EASY)
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
