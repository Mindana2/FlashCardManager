package org.Flashcard.models.studySession;

import org.Flashcard.models.dataClasses.FlashCard;
import org.Flashcard.models.ratingStrategy.RatingStrategy;
import org.Flashcard.models.ratingStrategy.StrategyFactory;

import java.util.HashMap;
import java.util.Map;

public class StudyScheduler {
    private final StrategyFactory strategyFactory;

    public StudyScheduler(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public boolean isDue(FlashCard card) {
        return true;
        //return card.getCardLearningState().isDueToday();
    }
}
