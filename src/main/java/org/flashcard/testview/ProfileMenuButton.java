package org.flashcard.testview;

import org.flashcard.controllers.UserController;

import javax.swing.*;
import java.awt.*;

public class ProfileMenuButton extends JButton {

    private final ProfileMenuPopup popup;

    public ProfileMenuButton(UserController userController, Navbar navbar) {
        this.popup = new ProfileMenuPopup(userController, navbar);

        setText("👤");  // You can replace with an icon here
        setFont(new Font("Arial", Font.BOLD, 16));
        setFocusable(false);
        setBackground(new Color(70, 70, 70));
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        setOpaque(true);

        addActionListener(e -> popup.showBelow(this));
    }
}
