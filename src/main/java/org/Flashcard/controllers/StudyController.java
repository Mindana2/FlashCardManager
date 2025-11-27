package org.Flashcard.controllers;

import org.Flashcard.models.dataClasses.FlashCard;
import org.Flashcard.models.ratingStrategy.RatingContext;
import org.Flashcard.models.ratingStrategy.StrategyFactory;
import org.Flashcard.repositories.DeckRepository;
import org.Flashcard.repositories.FlashCardRepository;

import java.sql.SQLException;


public class StudyController {
    private final StrategyFactory strategyFactory;
    private final RatingContext ratingContext;
    private final FlashCardRepository flashCardRepository;
    public StudyController(RatingContext ratingContext, StrategyFactory strategyFactory, FlashCardRepository flashCardRepository){
        this.ratingContext = ratingContext;
        this.strategyFactory = strategyFactory;

        this.flashCardRepository = flashCardRepository;
    }
    public void startSession(){

    }
    public void getCurrentCard(){

    }
    public void applyRating(String rating,int deckID, int cardID) throws SQLException {
        FlashCard flashCard = flashCardRepository.findByDeckId(deckID).get(cardID);
        ratingContext.setStrategy(strategyFactory.createStrategy(rating));
        ratingContext.executeStrategy(flashCard);

    }
    public void nextCard(){

    }
}
