package org.Flashcard.view;

import org.Flashcard.models.dataClasses.User;
import org.Flashcard.models.dataClasses.Deck;
import org.Flashcard.repositories.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserSelectionPanel extends JPanel {

    private final UserRepository userRepo = new UserRepository();
    private final DeckRepository deckRepo = new DeckRepository();
    private final TagRepository tagRepo = new TagRepository();

    private JComboBox<User> userDropdown;
    private JTextArea deckDisplay;

    public UserSelectionPanel() {
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
            List<User> users = userRepo.findAll();
            for (User u : users) {
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
            List<Deck> decks = deckRepo.findByUserId(user.getId());

            StringBuilder sb = new StringBuilder();
            sb.append("Decks for ").append(user.getUsername()).append(":\n\n");

            for (Deck d : decks) {
                var tag = tagRepo.findById(d.getTagId());
                sb.append("• ").append(d.getTitle());

                if (tag != null) {
                    sb.append(" (Tag: ")
                            .append(tag.getTitle())
                            .append(", Color: #")
                            .append(tag.getColor())
                            .append(")");
                }
                sb.append("\n");
            }

            deckDisplay.setText(sb.toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
