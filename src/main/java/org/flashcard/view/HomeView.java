package org.flashcard.view;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.FilterController;
import org.flashcard.controllers.UserController;
import org.flashcard.controllers.observer.Observer;
import org.flashcard.models.timers.CountdownListener;
import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.util.List;

public class HomeView extends JPanel implements Observer<List<DeckDTO>>, CountdownListener {

    private final DeckController deckController;
    private final UserController userController;
    private final FilterController filterController;
    private final MainFrame mainFrame;
    private List<DeckDTO> allDecks;


    private JPanel gridPanel;

    public HomeView(DeckController deckController, UserController userController,
                    FilterController filterController, MainFrame mainFrame) {
        this.deckController = deckController;
        this.userController = userController;
        this.filterController = filterController;
        this.mainFrame = mainFrame;

        deckController.getDecksObservable().addListener(this);
        setDecks();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("To be rehearsed Today");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        add(title, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));


        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null); // <-- tar bort tunna svarta linjen
        add(scrollPane, BorderLayout.CENTER);
    }
    private void setDecks(){
        int userID = userController.getCurrentUserId();
        this.allDecks = deckController .getAllDecksForUser(userID);
    }
    public void applyFilter(String text, Integer tagId) {
        refreshData(text, tagId);
    }

    public void refreshData(String text, Integer tagId) {
        gridPanel.removeAll();

        Integer userId = userController.getCurrentUserId();
        if (userId == null) return;
        allDecks = filterController.searchDecks(userId, text, tagId);
        allDecks = allDecks.stream()
                .sorted((d1, d2) -> Boolean.compare(
                        d2.getDueCount() > 0,
                        d1.getDueCount() > 0
                ))
                .toList();
        //Lägger till decks i vyn
        for (DeckDTO deck : allDecks) {
            // Hoppa över decks utan kort
            if (deck.getCardCount() == 0) continue;

            if (deck.getDueCount() > 0) {
                // Aktiva decks med due cards
                gridPanel.add(new DeckCard(
                        deck,
                        DeckCard.DeckCardContext.HOME_VIEW,
                        e -> mainFrame.startStudySession(deck.getId(), "today")
                ));
            } else {
                // Decks med kort men inga due cards -> utgråade med countdown
                Duration timeLeft = deckController.timeUntilDue(deck.getId());
                gridPanel.add(new DeckCard(
                        deck,
                        true,
                        "Next Card available in: ",
                        timeLeft,
                        deckController,
                        this
                ));
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }


    @Override
    public void notify(List<DeckDTO> data) {
        refreshData(null, null);
    }


    @Override
    public void onCountdownFinished() {

    }
}