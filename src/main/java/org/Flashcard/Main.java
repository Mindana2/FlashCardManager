package org.Flashcard;

import org.Flashcard.controllers.StudyController;
import org.Flashcard.controllers.UserController;
import org.Flashcard.repositories.*;
import org.Flashcard.view.AppWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            //Create repositories
            UserRepository userRepo = new UserRepository();
            DeckRepository deckRepo = new DeckRepository();
            TagRepository tagRepo = new TagRepository();
            FlashCardRepository flashCardRepo = new FlashCardRepository();


            //Create controllers
            final UserController userController = new UserController(userRepo,
                    deckRepo,
                    tagRepo);

            final StudyController studyController = new StudyController(
                    flashCardRepo,
                    deckRepo,
                    userRepo);

            //Create views, and inject controller into panel
            AppWindow window = new AppWindow(userController);
            window.setVisible(true);

        });
    }
}
