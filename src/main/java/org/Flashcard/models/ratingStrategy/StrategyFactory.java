package org.Flashcard.models.ratingStrategy;



public abstract class StrategyFactory {
    public StrategyFactory(){
    }
    public RatingStrategy createStrategy(String difficulty){
        return switch (difficulty) {
            case "easy" -> new StrategyEasy();
            case "medium" -> new StrategyMedium();
            case "hard" -> new StrategyHard();
            case "again" -> new StrategyAgain();
            default -> null;
        };
    }
}
