package org.flashcard.view;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.FilterController;
import org.flashcard.controllers.UserController;
import org.flashcard.controllers.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serves as the primary dashboard for the user, displaying a filtered grid of study decks
 * and using real-time scheduling logic to distinguish between active and locked sessions.
 */

public class HomeView extends JPanel implements Observer<List<DeckDTO>>{

    private final DeckController deckController;
    private final UserController userController;
    private final FilterController filterController;
    private final MainFrame mainFrame;
    private final Map<Integer, DeckCard> activeDeckCards = new HashMap<>();
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
        scrollPane.setBorder(null); // <-- removes thin black line
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


        // Apply search and tag filter
        allDecks = filterController.searchDecks(userId, text, tagId);

        // Sort by active decks first
        allDecks = allDecks.stream()
                .sorted((d1, d2) -> Boolean.compare(
                        d2.getDueCount() > 0,
                        d1.getDueCount() > 0
                ))
                .toList();
        //Add decks to grid
        for (DeckDTO deck : allDecks) {
            // Skip over decks with zero cards
            if (deck.getCardCount() == 0) continue;
            //Get a DeckCard from the HashMap
            DeckCard deckcard = activeDeckCards.get(deck.getId());

            //If deckcard doesn't exist or if the deck is not disabled while having at least 1 card due for today
            if (deckcard == null || deckcard.isDisabled() == deck.getDueCount() > 0){

                //If the deck already exists, remove it from the listener list
                if (deckcard != null) deckController.removeTimerListener(deckcard);
                //If the deck is due, make it playable...
                if (deck.getDueCount() > 0){
                    deckcard = new DeckCard(
                            deck,
                            DeckCard.DeckCardContext.HOME_VIEW,
                            e -> mainFrame.startStudySession(deck.getId(),
                                    "today"));

                //...else make it unplayable with a countdown
                } else {
                    deckcard =  new DeckCard(deck, true, () -> refreshData(text, tagId));
                    deckController.addTimerListener(deckcard, deck);
                }
                //(Re)place it in the HashMap
                activeDeckCards.put(deck.getId(), deckcard);

            }
            gridPanel.add(deckcard);

        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }


    @Override
    public void notify(List<DeckDTO> data) {
        activeDeckCards.clear();
        refreshData(null, null);
    }


}