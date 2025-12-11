package org.flashcard.testview;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.UserController;
import org.flashcard.controllers.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomeView extends JPanel implements Observer<List<DeckDTO>> {

    private final DeckController deckController;
    private final UserController userController;
    private final AppFrame appFrame;

    private JPanel gridPanel;

    public HomeView(DeckController deckController, UserController userController, AppFrame appFrame) {
        this.deckController = deckController;
        this.userController = userController;
        this.appFrame = appFrame;

        // REGISTRERA OBSERVER
        deckController.getDecksObservable().addListener(this);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Header
        JLabel title = new JLabel("To be rehearsed Today");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        add(title, BorderLayout.NORTH);

        // Grid Panel för korten
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

        List<DeckDTO> dueDecks = deckController.getDueDecksForUser(userId);

        if (dueDecks.isEmpty()) {
            JLabel emptyLabel = new JLabel("No cards to study today! Great job.");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gridPanel.add(emptyLabel);
        } else {
            for (DeckDTO deck : dueDecks) {
                if (deck.getDueCount() > 0) {
                    DeckCard card = new DeckCard(
                            deck,
                            e -> appFrame.startStudySession(deck.getId(), "today")
                    );
                    gridPanel.add(card);
                }
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    // OBSERVER CALLBACK METHOD
    @Override
    public void notify(List<DeckDTO> updatedDecks) {
        SwingUtilities.invokeLater(this::refreshData);
    }
}
