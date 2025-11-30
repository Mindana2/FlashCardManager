package org.Flashcard.view;

import org.Flashcard.controllers.UserController;
import org.Flashcard.models.dataClasses.User;
import org.Flashcard.models.dataClasses.Deck;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserSelectionPanel extends JPanel {

    private final UserController controller;

    private JComboBox<User> userDropdown;
    private JTextArea deckDisplay;

    public UserSelectionPanel(UserController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        userDropdown = new JComboBox<>();
        deckDisplay = new JTextArea();
        deckDisplay.setEditable(false);

        add(userDropdown, BorderLayout.NORTH);
        add(new JScrollPane(deckDisplay), BorderLayout.CENTER);

        loadUsers();
        userDropdown.addActionListener(e -> loadDecks());
    }

    private void loadUsers() {
        try {
            for (User u : controller.getAllUsers()) {
                userDropdown.addItem(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDecks() {
        User user = (User) userDropdown.getSelectedItem();
        if (user == null) return;

        try {
            List<Deck> decks = controller.getDecksForUser(user.getId());

            StringBuilder sb = new StringBuilder();
            sb.append("Decks for ").append(user.getUsername()).append(":\n\n");

            for (Deck d : decks) {
                sb.append("• ").append(d.getTitle());

                String tagText = controller.getTagText(d.getTagId());
                if (!tagText.isEmpty()) {
                    sb.append(" (").append(tagText).append(")");
                }
                sb.append("\n");
            }

            deckDisplay.setText(sb.toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
