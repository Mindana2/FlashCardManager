package org.Flashcard;

import org.Flashcard.controllers.StudyController;
import org.Flashcard.controllers.UserController;
import org.Flashcard.models.ratingStrategy.RatingContext;
import org.Flashcard.models.ratingStrategy.StrategyFactory;
import org.Flashcard.repositories.*;
import org.Flashcard.view.AppWindow;
import org.Flashcard.view.UserSelectionPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final RatingContext ratingContext = new RatingContext();

            //Create repositories
            UserRepository userRepo = new UserRepository();
            DeckRepository deckRepo = new DeckRepository();
            TagRepository tagRepo = new TagRepository();
            FlashCardRepository flashCardRepo = new FlashCardRepository();


            //Create controllers
            final UserController userController = new UserController(userRepo,
                    deckRepo,
                    tagRepo);

            final StudyController studyController = new StudyController(ratingContext,
                    flashCardRepo,
                    deckRepo,
                    userRepo);

            //Create views, and inject controller into panel
            AppWindow window = new AppWindow(userController);
            window.setVisible(true);

        });
    }
}
