package org.flashcard.testview;

import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.dto.DeckDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.UserController;

import org.flashcard.controllers.observer.Observer; // NEW

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EditDeckView extends JPanel implements Observer<List<FlashcardDTO>> {  // NEW

    private final DeckController deckController;
    private final UserController userController;
    private final AppFrame appFrame;

    private DeckDTO currentDeck;

    private JLabel headerLabel;
    private JPanel cardsListPanel;
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

        // registrera observer
        deckController.getFlashcardsObservable().addListener(this);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        initComponents();
    }

    private void initComponents() {


        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(245, 245, 245));
        top.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        backButton = new JButton("← Back");
        backButton.addActionListener(e -> appFrame.navigateTo("MyDecks"));
        top.add(backButton, BorderLayout.WEST);

        headerLabel = new JLabel("Edit Deck", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        top.add(headerLabel, BorderLayout.CENTER);

        deleteDeckButton = new JButton("Delete Deck");
        deleteDeckButton.setBackground(new Color(220, 60, 60));
        deleteDeckButton.setForeground(Color.WHITE);
        deleteDeckButton.addActionListener(e -> confirmAndDeleteDeck());
        top.add(deleteDeckButton, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        cardsListPanel = new JPanel();
        cardsListPanel.setLayout(new BoxLayout(cardsListPanel, BoxLayout.Y_AXIS));
        cardsListPanel.setBackground(Color.WHITE);
        cardsListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cardsScroll = new JScrollPane(cardsListPanel);
        cardsScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        cardsScroll.getVerticalScrollBar().setUnitIncrement(14);
        add(cardsScroll, BorderLayout.CENTER);

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
        form.add(new JLabel("Front Side:"), gbc);
        newFrontField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        form.add(newFrontField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Back Side:"), gbc);
        newBackField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        form.add(newBackField, gbc);

        addCardButton = new JButton("+ Add Card");
        addCardButton.setPreferredSize(new Dimension(150, 36));
        addCardButton.setBackground(new Color(60, 160, 80));
        addCardButton.setForeground(Color.WHITE);
        addCardButton.addActionListener(e -> handleAddCard());

        JPanel formRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        formRow.setBackground(new Color(245, 245, 245));
        formRow.add(addCardButton);

        bottom.add(form);
        bottom.add(formRow);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(0, 120, 0));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8,0,0,0));
        bottom.add(statusLabel);

        add(bottom, BorderLayout.SOUTH);
    }

    public void loadDeck(int deckId) {
        try {
            currentDeck = deckController.getDeckById(deckId);
            headerLabel.setText("Edit: " + currentDeck.getTitle());
            statusLabel.setText("Loaded deck (ID " + currentDeck.getId() + ")");
            refreshCardsList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Couldn't load the Deck: " + e.getMessage());
        }
    }

    private void refreshCardsList() {
        cardsListPanel.removeAll();

        if (currentDeck == null) {
            cardsListPanel.revalidate();
            cardsListPanel.repaint();
            return;
        }

        List<FlashcardDTO> cards = deckController.getFlashcardsForDeck(currentDeck.getId());

        if (cards.isEmpty()) {
            JLabel empty = new JLabel("No cards in this deck. Add one below.");
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

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);
        JLabel front = new JLabel("<html><b>Q:</b> " + safe(card.getFront()) + "</html>");
        JLabel back = new JLabel("<html><b>A:</b> " + safe(card.getBack()) + "</html>");
        front.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        back.setBorder(BorderFactory.createEmptyBorder(0,6,6,6));
        text.add(front);
        text.add(back);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        actions.setBackground(Color.WHITE);

        JButton deleteBtn = new JButton("Delete");
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
            JOptionPane.showMessageDialog(this, "No Deck chosen.");
            return;
        }
        String front = newFrontField.getText().trim();
        String back = newBackField.getText().trim();

        if (front.isBlank() || back.isBlank()) {
            JOptionPane.showMessageDialog(this, "Both front and back are required.");
            return;
        }

        try {
            deckController.addFlashcard(currentDeck.getId(), front, back);
            newFrontField.setText("");
            newBackField.setText("");
            statusLabel.setText("Card added.");

            currentDeck = deckController.getDeckById(currentDeck.getId());
            refreshCardsList();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Couldn't add the Card " + e.getMessage());
        }
    }

    private void confirmAndDeleteCard(Integer cardId) {
        int res = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this card?",
                "Confirm deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (res == JOptionPane.YES_OPTION) {
            try {
                deckController.deleteFlashcard(cardId);
                statusLabel.setText("Card deleted.");

                currentDeck = deckController.getDeckById(currentDeck.getId());
                refreshCardsList();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Couldn't delete card " + e.getMessage());
            }
        }
    }

    private void confirmAndDeleteDeck() {
        if (currentDeck == null) return;

        int res = JOptionPane.showConfirmDialog(
                this,
                "Are you sure? This will delete the deck and all cards in it.",
                "Confirm deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (res == JOptionPane.YES_OPTION) {
            try {
                deckController.deleteDeck(currentDeck.getId());
                JOptionPane.showMessageDialog(this, "Deck deleted.");
                appFrame.navigateTo("MyDecks");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Couldn't delete Deck " + e.getMessage());
            }
        }
    }


    // OBSERVER CALLBACK METHOD
    @Override
    public void notify(List<FlashcardDTO> updatedCards) {
        // Ladda om kortlistan automatiskt
        SwingUtilities.invokeLater(this::refreshCardsList);
    }
}
