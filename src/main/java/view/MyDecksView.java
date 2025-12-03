package view;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.UserController;
import org.flashcard.models.dataclasses.Deck;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MyDecksView extends HomeView {

    private JButton editButton;
    private final UserController userController;
    private final DeckController deckController;

    public MyDecksView(UserController userController, DeckController deckController) {
        super();
        this.userController = userController;
        this.deckController = deckController;

        editButton = new JButton("Edit Decks");
        addExtras();
        styleExtras();
    }

    private void addExtras() {
        titleLabel.setText("My Decks");

        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editPanel.setOpaque(false);
        editPanel.add(editButton);

        headerPanel.add(editPanel, BorderLayout.EAST);
        headerPanel.setBackground(Color.WHITE);
    }

    private void styleExtras() {
        editButton.setBackground(new Color(230, 230, 230));
        editButton.setForeground(Color.BLACK);
        editButton.setFocusPainted(false);
    }

    // Refresh decks for current user
    public void refreshDecksForCurrentUser() {
        var currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            setDecks(List.of());
            return;
        }

        List<DeckDTO> decks = deckController.getAllDecksForUser(currentUser.getId());

        // Convert to simple strings
        List<String> deckNames = decks.stream()
                .map(DeckDTO::getTitle)
                .collect(Collectors.toList());

        setDecks(deckNames);

        System.out.println("Decks for user " + currentUser.getUsername() + ": " + deckNames);
    }
}
