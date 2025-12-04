package org.flashcard.testview;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.UserController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyDecksViewTest extends JPanel {

    private final DeckController deckController;
    private final UserController userController;
    private final AppFrame appFrame;

    private JPanel gridPanel;

    public MyDecksViewTest(DeckController deckController, UserController userController, AppFrame appFrame) {
        this.deckController = deckController;
        this.userController = userController;
        this.appFrame = appFrame;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // --- Header Panel (Titel + Create Button) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel title = new JLabel("Mina Lekar");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        JButton createButton = new JButton("+ Skapa Ny Lek");
        createButton.setBackground(new Color(46, 204, 113)); // Grön
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createButton.addActionListener(e -> {
            // Nollställ create-deck-form och navigera
            appFrame.getCreateDeckView().resetFormForNewDeck();
            appFrame.navigateTo("CreateDeck");
        });

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(createButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Grid Panel ---
        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(new Color(245, 245, 245));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        gridPanel.removeAll();

        Integer userId = userController.getCurrentUserId();
        if (userId == null) return;

        // Hämta ALLA decks för användaren
        List<DeckDTO> allDecks = deckController.getAllDecksForUser(userId);

        if (allDecks.isEmpty()) {
            JLabel emptyLabel = new JLabel("Du har inga lekar än. Skapa en!");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gridPanel.add(emptyLabel);
        } else {
            for (DeckDTO deck : allDecks) {
                // wrapper-panel: DeckCard + liten knapprad under
                JPanel wrapper = new JPanel();
                wrapper.setLayout(new BorderLayout());
                wrapper.setOpaque(false);

                // DeckCard (visuellt)
                DeckCard card = new DeckCard(deck, e -> appFrame.startStudySession(deck.getId(), "all"));
                wrapper.add(card, BorderLayout.CENTER);

                // Buttons panel (Edit, kanske framtida andra knappar)
                JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
                btnRow.setBackground(new Color(245, 245, 245));

                JButton editBtn = new JButton("Redigera");
                editBtn.setPreferredSize(new Dimension(90, 28));
                editBtn.setBackground(new Color(70, 130, 180));
                editBtn.setForeground(Color.WHITE);
                editBtn.setFocusPainted(false);
                editBtn.addActionListener(e -> {
                    // Ladda deck i EditDeckView och navigera
                    try {
                        appFrame.getEditDeckView().loadDeck(deck.getId());
                        appFrame.navigateTo("EditDeck");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Kunde inte öppna redigeringsvyn: " + ex.getMessage());
                    }
                });

                btnRow.add(editBtn);

                // Lägg till knappraden under kortet
                wrapper.add(btnRow, BorderLayout.SOUTH);

                gridPanel.add(wrapper);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }
}
