package org.flashcard.view;

import org.flashcard.application.dto.UserDTO;
import org.flashcard.controllers.UserController;
import javax.swing.*;
import java.awt.*;

public class ProfileMenuButton extends JButton {

    private final ProfileMenuPopup popup;
    private final UserController userController;

    public ProfileMenuButton(UserController userController, Navbar navbar) {
        this.userController = userController;
        this.popup = new ProfileMenuPopup(userController, navbar);

        setFont(new Font("Arial", Font.BOLD, 14));
        setFocusable(false);
        setBackground(new Color(70, 70, 70));
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        setOpaque(true);

        refreshUser(); // initial state

        addActionListener(e -> popup.showBelow(this));
    }

    /** Call this when the active user changes */
    public void refreshUser() {
        UserDTO user = userController.getCurrentUser();

        if (user == null) {
            setText("👤");
            setToolTipText("No user");
        } else {
            String name = user.getUsername();
            setText(name.substring(0, 1).toUpperCase());
            setToolTipText(name);
        }
    }
}
