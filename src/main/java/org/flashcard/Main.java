package org.flashcard;

import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.StudyController;
import org.flashcard.controllers.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import view.TestFrame;

import javax.swing.*;

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


            //MainFrame frame = new MainFrame(userController, studyController, deckController);
            TestFrame frame = new TestFrame(userController, studyController, deckController);
            frame.setVisible(true);

        });
    }
}
