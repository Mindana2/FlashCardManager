package org.flashcard.view;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.UserController;
import org.flashcard.controllers.observer.Observer;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Provides a comprehensive editor for managing flashcard content within a specific deck,
 * utilizing the Observer pattern to ensure the card list remains synchronized with the database.
 */

public class EditDeckView extends JPanel implements Observer<List<FlashcardDTO>> {

    private final DeckController deckController;
    private final UserController userController;
    private final MainFrame mainFrame;

    private DeckDTO currentDeck;

    private JLabel headerLabel;
    private JPanel cardsListPanel;
    private JScrollPane cardsScroll;

    private JTextField newFrontField;
    private JTextField newBackField;
    private JButton addCardButton;

    private JButton deleteDeckButton;
    private JButton backButton;
    private JButton doneButton;

    private JLabel statusLabel;

    public EditDeckView(DeckController deckController,
                        UserController userController,
                        MainFrame mainFrame) {

        this.deckController = deckController;
        this.userController = userController;
        this.mainFrame = mainFrame;

        deckController.getFlashcardsObservable().addListener(this);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        initComponents();
    }

    private void initComponents() {

        /* ===== TOP BAR ===== */
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(245, 245, 245));
        top.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        backButton = new JButton("← Back");
        backButton.addActionListener(e -> mainFrame.navigateTo("MyDecks"));
        top.add(backButton, BorderLayout.WEST);

        headerLabel = new JLabel("Edit Deck", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        top.add(headerLabel, BorderLayout.CENTER);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        topRight.setBackground(new Color(245, 245, 245));

        deleteDeckButton = new JButton("Delete Deck");
        deleteDeckButton.setBackground(new Color(220, 60, 60));
        deleteDeckButton.setForeground(Color.WHITE);
        deleteDeckButton.addActionListener(e -> confirmAndDeleteDeck());
        topRight.add(deleteDeckButton);

        doneButton = new JButton("Done");
        doneButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        doneButton.setBackground(new Color(65, 105, 225));
        doneButton.setForeground(Color.WHITE);
        doneButton.addActionListener(e -> mainFrame.navigateTo("MyDecks"));
        topRight.add(doneButton);

        top.add(topRight, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        /* ===== FLASHCARDS OVERVIEW ===== */
        cardsListPanel = new JPanel();
        cardsListPanel.setLayout(new BoxLayout(cardsListPanel, BoxLayout.Y_AXIS));
        cardsListPanel.setBackground(Color.WHITE);
        cardsListPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        cardsScroll = new JScrollPane(cardsListPanel);
        cardsScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        cardsScroll.getVerticalScrollBar().setUnitIncrement(14);

        JPanel overviewPanel = new JPanel(new BorderLayout());
        overviewPanel.setBackground(Color.WHITE);

        JLabel overviewLabel = new JLabel("Flashcards");
        overviewLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        overviewLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        overviewPanel.add(overviewLabel, BorderLayout.NORTH);
        overviewPanel.add(cardsScroll, BorderLayout.CENTER);

        /* ===== RESET PROGRESSION BUTTON ===== */
        JButton resetProgressBtn = new JButton("Reset progression for all cards");
        resetProgressBtn.setBackground(new Color(65, 105, 225)); // mörkblå
        resetProgressBtn.setForeground(Color.WHITE);
        resetProgressBtn.setFocusPainted(false);
        resetProgressBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        resetProgressBtn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this,
                    "Reset progression for all cards in this deck?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                deckController.resetDeckProgression(currentDeck.getId());
                refreshCardsList();
            }
        });

        // Center button in panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        buttonPanel.add(resetProgressBtn);

        overviewPanel.add(buttonPanel, BorderLayout.SOUTH);


        /* ===== CREATE FLASHCARD PANEL ===== */
        JPanel createPanel = new JPanel();
        createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));
        createPanel.setBackground(Color.WHITE);
        createPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel createTitle = new JLabel("Create Flashcard");
        createTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        createPanel.add(createTitle);
        createPanel.add(Box.createVerticalStrut(18));

        JLabel frontLabel = new JLabel("Front (Question)");
        createPanel.add(frontLabel);

        newFrontField = new JTextField();
        newFrontField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        createPanel.add(newFrontField);
        createPanel.add(Box.createVerticalStrut(14));

        JLabel backLabel = new JLabel("Back (Answer)");
        createPanel.add(backLabel);

        newBackField = new JTextField();
        newBackField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        createPanel.add(newBackField);
        createPanel.add(Box.createVerticalStrut(22));

        addCardButton = new JButton("+ Add Card");
        addCardButton.setBackground(new Color(60, 160, 80));
        addCardButton.setForeground(Color.WHITE);
        addCardButton.addActionListener(e -> handleAddCard());
        createPanel.add(addCardButton);
        createPanel.add(Box.createVerticalStrut(10));

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(0, 120, 0));
        createPanel.add(statusLabel);

        /* ===== SPLIT PANE ===== */
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                overviewPanel,
                createPanel
        );
        splitPane.setResizeWeight(0.6);
        splitPane.setDividerSize(6);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
    }

    /* ===== PUBLIC API ===== */
    public void loadDeck(int deckId) {
        currentDeck = deckController.getDeckById(deckId);
        headerLabel.setText("Edit: " + currentDeck.getTitle());
        refreshCardsList();
    }

    /* ===== LOGIC ===== */
    private void refreshCardsList() {
        cardsListPanel.removeAll();

        List<FlashcardDTO> cards =
                deckController.getFlashcardsForDeck(currentDeck.getId());

        if (cards.isEmpty()) {
            JLabel emptyLabel = new JLabel("No cards yet.", SwingConstants.CENTER);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            cardsListPanel.add(emptyLabel);
        } else {
            // Add card on the top
            for (int i = cards.size() - 1; i >= 0; i--) {
                cardsListPanel.add(cardRowFor(cards.get(i)));
                cardsListPanel.add(Box.createVerticalStrut(8));
            }
        }

        cardsListPanel.revalidate();
        cardsListPanel.repaint();
    }

    private JPanel cardRowFor(FlashcardDTO card) {
        JPanel row = new JPanel(new BorderLayout());
        row.setMaximumSize(new Dimension(400, 80)); // bestämd storlek
        row.setPreferredSize(new Dimension(400, 80));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel text = new JLabel("<html><b>Q:</b> " + card.getFront()
                + "<br><b>A:</b> " + card.getBack() + "</html>");
        row.add(text, BorderLayout.CENTER);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 60, 60));
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> confirmAndDeleteCard(card.getId()));
        row.add(deleteBtn, BorderLayout.EAST);

        return row;
    }

    private void handleAddCard() {
        try {
            deckController.addFlashcard(
                    currentDeck.getId(),
                    newFrontField.getText(),
                    newBackField.getText()
            );
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }

        newFrontField.setText("");
        newBackField.setText("");
        refreshCardsList();
    }

    private void confirmAndDeleteCard(Integer cardId) {
        if (JOptionPane.showConfirmDialog(this, "Delete card?",
                "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            deckController.deleteFlashcard(cardId);
            refreshCardsList();
        }
    }

    private void confirmAndDeleteDeck() {
        if (JOptionPane.showConfirmDialog(this,
                "Delete deck and all cards?",
                "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            deckController.deleteDeck(currentDeck.getId());
            mainFrame.navigateTo("MyDecks");
        }
    }

    @Override
    public void notify(List<FlashcardDTO> flashcardDTOS) {
        SwingUtilities.invokeLater(this::refreshCardsList);
    }
}
