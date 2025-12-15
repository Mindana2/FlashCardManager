package org.flashcard.testview;

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
    private final AppFrame appFrame;


    private JPanel gridPanel;

    public HomeView(DeckController deckController, UserController userController,
                    FilterController filterController, AppFrame appFrame) {
        this.deckController = deckController;
        this.userController = userController;
        this.filterController = filterController;
        this.appFrame = appFrame;

        deckController.getDecksObservable().addListener(this);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("To be rehearsed Today");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        add(title, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ⬇⬇⬇ NYTT: nu skapar vi scrollPane som variabel och tar bort border
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null); // <-- tar bort tunna svarta linjen
        add(scrollPane, BorderLayout.CENTER);
        // ⬆⬆⬆ END NEW
    }

    public void applyFilter(String text, Integer tagId) {
        refreshData(text, tagId);
    }

    public void refreshData(String text, Integer tagId) {
        gridPanel.removeAll();

        Integer userId = userController.getCurrentUserId();
        if (userId == null) return;

        // Hämta dynamiskt beräknade decks med kort som är due today
        //List<DeckDTO> decks = deckController.getDueDecksForUser(userId);

        // Hämta ALLA decks med due-info
        List<DeckDTO> dueDecks = filterController.getDueDecksForUser(userId);
        List<DeckDTO> notDueDecks = filterController.getNotDueDecksForUser(userId);
        List<DeckDTO> allDecks = deckController.getAllDecksForUser(userId);


        // Applicera sökfilter om text finns
        if (text != null && !text.isBlank()) {
            allDecks = allDecks.stream()
                    .filter(d -> d.getTitle().toLowerCase().contains(text.toLowerCase()))
                    .toList();
        }

        // Applicera tag-filter om tagId finns
        if (tagId != null) {
            allDecks = allDecks.stream()
                    .filter(d -> d.getTagDTO() != null && tagId.equals(d.getTagDTO().getId()))
                    .toList();
        }

        // Sortera så att aktiva decks (med due cards) kommer först
        allDecks = allDecks.stream()
                .sorted((d1, d2) -> Boolean.compare(
                        d2.getDueCount() > 0,
                        d1.getDueCount() > 0
                ))
                .toList();
        //Lägger till decks i vyn
        for (DeckDTO deck : allDecks) {
            //Lägger till decks som kan spelas
            if (deck.getDueCount() > 0) {
                gridPanel.add(new DeckCard(deck, e -> appFrame.startStudySession(deck.getId(), "today")));
            }
            else {
                //Lägger till decks som inte kan spelas med en countdown timer
                Duration timeLeft = deckController.timeUntilDue(deck.getId());
                gridPanel.add(
                        new DeckCard(
                                deck,
                                null,
                                true,
                                "Next Card available in: ",
                                timeLeft,
                                deckController,
                                this
                        ));

            }   }
        }


    @Override
    public void notify(List<DeckDTO> data) {

    }

    @Override
    public void onCountdownFinished() {
        refreshData(null, null);
    }
}