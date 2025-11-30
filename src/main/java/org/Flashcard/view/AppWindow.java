package org.Flashcard.view;

import org.Flashcard.controllers.UserController;

import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {

    public AppWindow(UserController userController) {
        super("Flashcard Manager");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Inject UserController into the view
        UserSelectionPanel userSelectionPanel = new UserSelectionPanel(userController);
        add(userSelectionPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null); // center
        setVisible(true);
    }
}
