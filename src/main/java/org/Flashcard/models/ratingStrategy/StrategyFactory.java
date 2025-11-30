package org.Flashcard.models.ratingStrategy;


import java.util.List;

public final class StrategyFactory {        //No inheritance of this class
    private StrategyFactory(){
    }
    public static RatingStrategy createStrategy(String difficulty){
        return switch (difficulty) {
            case "easy" -> new StrategyEasy();
            case "medium" -> new StrategyMedium();
            case "hard" -> new StrategyHard();
            case "again" -> new StrategyAgain();
            default -> null;
        };
    }
    public static List<String> getAllRatingNames(){
        return List.of("easy", "medium", "hard", "again");
    }
}
