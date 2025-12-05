package org.flashcard;

import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.StudyController;
import org.flashcard.controllers.UserController;
import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.User;
import org.flashcard.testview.AppFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
//import view.MainFrame;
//import view.TestFrame;

import javax.swing.*;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Start Spring Boot context
            ApplicationContext context = SpringApplication.run(Main.class);

            // Retrieve beans from Spring
            UserController userController = context.getBean(UserController.class);
            DeckController deckController = context.getBean(DeckController.class);
            StudyController studyController = context.getBean(StudyController.class);

            // Start View
            AppFrame frame = new AppFrame(userController, studyController, deckController);
            frame.setVisible(true);

        });
    }
}
