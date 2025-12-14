package org.flashcard.testview;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.UserController;
import org.flashcard.controllers.observer.Observer;
import org.flashcard.models.timers.TimerListener;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
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
        List<DeckDTO> decks = deckController.getAllDecksWithDueInfo(userId);
        List<DeckDTO> notDueDecks = deckController.getNotDueDecksForUser(userId);

        // Applicera sökfilter om text finns
        if (text != null && !text.isBlank()) {
            decks = decks.stream()
                    .filter(d -> d.getTitle().toLowerCase().contains(text.toLowerCase()))
                    .toList();
        }

        // Applicera tag-filter om tagId finns
        if (tagId != null) {
            decks = decks.stream()
                    .filter(d -> d.getTagDTO() != null && tagId.equals(d.getTagDTO().getId()))
                    .toList();
        }

        // Sortera så att aktiva decks (med due cards) kommer först
        decks = decks.stream()
                .sorted((d1, d2) -> Boolean.compare(
                        d2.getDueCount() > 0,
                        d1.getDueCount() > 0
                ))
                .toList();


        if (decks.isEmpty()) {
            JLabel lbl = new JLabel("No cards to study today!");
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            gridPanel.add(lbl);
        } else {
            for (DeckDTO deck : decks) {

                boolean isActive = deck.getDueCount() > 0; // ✅ HÄR

                if (isActive) {
                    gridPanel.add(
                            new DeckCard(
                                    deck,
                                    e -> appFrame.startStudySession(deck.getId(), "today")
                            )
                    );
                } else {
                    gridPanel.add(
                            new DeckCard(
                                    deck,
                                    null,
                                    true,
                                    "Next Card available in X days"
                            )
                    );
                }
            }
//            else {
//                gridPanel.add(new DeckCard(deck,
//                        e -> appFrame.startStudySession(deck.getId(), "today"), timeLeft));
//            }
//        }

//        if (dueDecks.isEmpty()) {
//            JLabel lbl = new JLabel("No cards to study today!");
//            lbl.setHorizontalAlignment(SwingConstants.CENTER);
//            gridPanel.add(lbl);
//        } else {
//            for (DeckDTO deck : dueDecks) {
//                gridPanel.add(new DeckCard(deck,
//                        e -> appFrame.startStudySession(deck.getId(), "today"), ""));
//            }
//        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }


    @Override
    public void notify(List<DeckDTO> data) {
        refreshData(null, null);
    }


//    @Override
//    public void updateTime(String countdown) {
//        this.countdown = countdown;
//        gridPanel.revalidate();
//        gridPanel.repaint();
//    }
}
