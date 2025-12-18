package org.flashcard;

import org.flashcard.controllers.*;
import org.flashcard.view.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import javax.swing.*;

/**
 * Acts as the entry point for the application, bootstrapping the Spring Boot context
 * alongside the Swing Event Dispatch Thread to integrate backend dependency injection
 * with the graphical user interface.
 */

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
            TagController tagController = context.getBean(TagController.class);
            FilterController filterController = context.getBean(FilterController.class);

            // Start View
            MainFrame frame = new MainFrame(userController, studyController, deckController,
                    tagController, filterController);
            frame.setVisible(true);
        });
    }
}
