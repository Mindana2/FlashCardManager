package org.flashcard.testview;

import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.dto.DeckDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.UserController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * EditDeckView - vy för att redigera ett valt deck:
 * - visa lista med flashcards (med delete-knappar)
 * - lägga till flashcards
 * - radera hela leken
 * - back/return
 */
public class EditDeckView extends JPanel {

    private final DeckController deckController;
    private final UserController userController;
    private final AppFrame appFrame;

    // State
    private DeckDTO currentDeck;

    // UI
    private JLabel headerLabel;
    private JPanel cardsListPanel; // innehåller rader för varje kort
    private JScrollPane cardsScroll;
    private JTextField newFrontField;
    private JTextField newBackField;
    private JButton addCardButton;
    private JButton deleteDeckButton;
    private JButton backButton;
    private JLabel statusLabel;

    public EditDeckView(DeckController deckController, UserController userController, AppFrame appFrame) {
        this.deckController = deckController;
        this.userController = userController;
        this.appFrame = appFrame;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(245, 245, 245));
        top.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        backButton = new JButton("← Tillbaka");
        backButton.addActionListener(e -> appFrame.navigateTo("MyDecks"));
        top.add(backButton, BorderLayout.WEST);

        headerLabel = new JLabel("Redigera lek", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        top.add(headerLabel, BorderLayout.CENTER);

        deleteDeckButton = new JButton("Radera lek");
        deleteDeckButton.setBackground(new Color(220, 60, 60));
        deleteDeckButton.setForeground(Color.WHITE);
        deleteDeckButton.addActionListener(e -> confirmAndDeleteDeck());
        top.add(deleteDeckButton, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // Center: cards list
        cardsListPanel = new JPanel();
        cardsListPanel.setLayout(new BoxLayout(cardsListPanel, BoxLayout.Y_AXIS));
        cardsListPanel.setBackground(Color.WHITE);
        cardsListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cardsScroll = new JScrollPane(cardsListPanel);
        cardsScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        cardsScroll.getVerticalScrollBar().setUnitIncrement(14);
        add(cardsScroll, BorderLayout.CENTER);

        // Bottom: add card form + status
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(new Color(245, 245, 245));
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Framsida:"), gbc);
        newFrontField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        form.add(newFrontField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Baksida:"), gbc);
        newBackField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        form.add(newBackField, gbc);

        addCardButton = new JButton("+ Lägg till kort");
        addCardButton.setPreferredSize(new Dimension(150, 36));
        addCardButton.setBackground(new Color(60, 160, 80));
        addCardButton.setForeground(Color.WHITE);
        addCardButton.addActionListener(e -> handleAddCard());

        JPanel formRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        formRow.setBackground(new Color(245, 245, 245));
        formRow.add(addCardButton);

        bottom.add(form);
        bottom.add(formRow);

        // status
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(0, 120, 0));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8,0,0,0));
        bottom.add(statusLabel);

        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Ladda deck i edit-läget (anropa från AppFrame eller MyDecksView när användaren klickar "Edit")
     */
    public void loadDeck(int deckId) {
        try {
            currentDeck = deckController.getDeckById(deckId);
            headerLabel.setText("Redigera: " + currentDeck.getTitle());
            statusLabel.setText("Laddat lek (ID " + currentDeck.getId() + ")");
            refreshCardsList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Kunde inte ladda lek: " + e.getMessage());
        }
    }

    private void refreshCardsList() {
        cardsListPanel.removeAll();

        if (currentDeck == null) {
            cardsListPanel.revalidate();
            cardsListPanel.repaint();
            return;
        }

        // Hämta kort via controllern (säkrare än att förlita sig på lazy-listor)
        List<FlashcardDTO> cards = deckController.getFlashcardsForDeck(currentDeck.getId());

        if (cards.isEmpty()) {
            JLabel empty = new JLabel("Inga kort i denna lek. Lägg till ett nedan.");
            empty.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            cardsListPanel.add(empty);
        } else {
            for (FlashcardDTO c : cards) {
                cardsListPanel.add(cardRowFor(c));
            }
        }

        cardsListPanel.revalidate();
        cardsListPanel.repaint();
    }

    private JPanel cardRowFor(FlashcardDTO card) {
        JPanel row = new JPanel(new BorderLayout(8,8));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0,0,1,0, new Color(230,230,230)));

        // Left: front/back text
        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);
        JLabel front = new JLabel("<html><b>Q:</b> " + safe(card.getFront()) + "</html>");
        JLabel back = new JLabel("<html><b>A:</b> " + safe(card.getBack()) + "</html>");
        front.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        back.setBorder(BorderFactory.createEmptyBorder(0,6,6,6));
        text.add(front);
        text.add(back);

        // Right: action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        actions.setBackground(Color.WHITE);

        JButton deleteBtn = new JButton("Radera");
        deleteBtn.setBackground(new Color(220,60,60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> confirmAndDeleteCard(card.getId()));
        actions.add(deleteBtn);

        row.add(text, BorderLayout.CENTER);
        row.add(actions, BorderLayout.EAST);
        return row;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private void handleAddCard() {
        if (currentDeck == null) {
            JOptionPane.showMessageDialog(this, "Ingen lek vald.");
            return;
        }
        String front = newFrontField.getText().trim();
        String back = newBackField.getText().trim();

        if (front.isBlank() || back.isBlank()) {
            JOptionPane.showMessageDialog(this, "Både framsida och baksida krävs.");
            return;
        }

        try {
            deckController.addFlashcard(currentDeck.getId(), front, back);
            // rensa formulär och uppdatera listan
            newFrontField.setText("");
            newBackField.setText("");
            statusLabel.setText("Kort tillagt!");
            // uppdatera currentDeck DTO (så t.ex. cardCount blir korrekt)
            currentDeck = deckController.getDeckById(currentDeck.getId());
            refreshCardsList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Kunde inte lägga till kort: " + e.getMessage());
        }
    }

    private void confirmAndDeleteCard(Integer cardId) {
        int res = JOptionPane.showConfirmDialog(this,
                "Är du säker på att du vill ta bort detta kort?",
                "Bekräfta radering",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            try {
                deckController.deleteFlashcard(cardId);
                statusLabel.setText("Kort raderat.");
                if (currentDeck != null) {
                    currentDeck = deckController.getDeckById(currentDeck.getId());
                }
                refreshCardsList();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Kunde inte radera kort: " + e.getMessage());
            }
        }
    }

    private void confirmAndDeleteDeck() {
        if (currentDeck == null) return;

        int res = JOptionPane.showConfirmDialog(this,
                "Är du säker? Detta tar bort leken och alla kort i den.",
                "Bekräfta radering",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            try {
                deckController.deleteDeck(currentDeck.getId());
                JOptionPane.showMessageDialog(this, "Leken raderades.");
                // gå tillbaka till MyDecks och uppdatera
                appFrame.navigateTo("MyDecks");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Kunde inte radera lek: " + e.getMessage());
            }
        }
    }
}
