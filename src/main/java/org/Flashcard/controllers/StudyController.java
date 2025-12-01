package org.Flashcard.controllers;

import org.Flashcard.application.dto.FlashCardDTO;
import org.Flashcard.application.mapper.FlashCardMapper;
import org.Flashcard.models.dataClasses.Deck;
import org.Flashcard.models.dataClasses.FlashCard;
import org.Flashcard.models.dataClasses.User;
import org.Flashcard.models.ratingStrategy.RatingContext;
import org.Flashcard.models.ratingStrategy.StrategyFactory;
import org.Flashcard.models.studySession.StudyAlgorithm;
import org.Flashcard.models.studySession.StudyAlgorithmFactory;
import org.Flashcard.models.studySession.StudySession;
import org.Flashcard.repositories.DeckRepository;
import org.Flashcard.repositories.FlashCardRepository;
import org.Flashcard.repositories.UserRepository;

import java.sql.SQLException;


public class StudyController {

    private final FlashCardRepository flashCardRepository;
    private final DeckRepository deckRepository;
    private final UserRepository userRepository;

    private StudySession currentSession;

    public StudyController(FlashCardRepository flashCardRepository,
                           DeckRepository deckRepository,
                           UserRepository userRepository) {
        this.deckRepository = deckRepository;
        this.userRepository = userRepository;
        this.flashCardRepository = flashCardRepository;
    }

    public void startSession(String algorithmStrategy, int deckId, int userId) throws SQLException {
        User currentUser = userRepository.findById(userId);
        Deck currentDeck = deckRepository.findByIdWithCards(deckId);
        StudyAlgorithm algorithm = StudyAlgorithmFactory.createAlgorithm(algorithmStrategy.toLowerCase());

        currentSession = new StudySession(currentDeck, currentUser, algorithm);
        currentSession.startSession();
    }
    public void applyRating(String rating, int deckID, int cardID) throws SQLException {

        //Hämtar flashcard från FlashCardRepository med cardID
        FlashCard flashCard = flashCardRepository.findByDeckId(deckID).get(cardID);

        //Sätter ny rating på kortet
        RatingContext.executeStrategy(flashCard, StrategyFactory.createStrategy(rating));
    }
    public FlashCardDTO nextCard(){
        FlashCard flashCard = currentSession.getNextCardAndRemove();
        return FlashCardMapper.toDTO(flashCard);    //Data object with readonly functions for the view
    }
}
