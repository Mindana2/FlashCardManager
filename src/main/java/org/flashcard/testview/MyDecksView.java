package org.flashcard.testview;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.FilterController;
import org.flashcard.controllers.UserController;
import org.flashcard.controllers.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyDecksView extends JPanel implements Observer<List<DeckDTO>> {

    private final DeckController deckController;
    private final UserController userController;
    private final FilterController filterController;
    private final AppFrame appFrame;

    private JPanel gridPanel;

    public MyDecksView(DeckController deckController, UserController userController, FilterController filtercontroller,
                       AppFrame appFrame) {
        this.deckController = deckController;
        this.userController = userController;
        this.filterController = filtercontroller;
        this.appFrame = appFrame;

        deckController.getDecksObservable().addListener(this);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        JLabel title = new JLabel("My Decks");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        JButton create = new JButton("+ Create New Deck");
        create.setBackground(new Color(46, 204, 113));
        create.setForeground(Color.WHITE);
        create.setFocusPainted(false);
        create.addActionListener(e -> {
            appFrame.getCreateDeckView().resetFormForNewDeck();
            appFrame.navigateTo("CreateDeck");
        });

        top.add(title, BorderLayout.WEST);
        top.add(create, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // Grid
        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));


        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null); // <-- tar bort linjen
        add(scrollPane, BorderLayout.CENTER);

    }

    public void applyFilter(String text, Integer tagId) {
        refreshData(text, tagId);
    }

    public void refreshData(String text, Integer tagId) {
        gridPanel.removeAll();

        Integer userId = userController.getCurrentUserId();
        List<DeckDTO> decks = filterController.searchDecks(userId, text, tagId);

        if (decks.isEmpty()) {
            JLabel lbl = new JLabel("No decks found.");
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            gridPanel.add(lbl);
        } else {
            for (DeckDTO d : decks) {
                JPanel wrapper = new JPanel(new BorderLayout());
                wrapper.setOpaque(false);

                DeckCard card = new DeckCard(d, DeckCard.DeckCardContext.MY_DECKS_VIEW,
                        e -> appFrame.startStudySession(d.getId(), "all"));
                wrapper.add(card, BorderLayout.CENTER);

                JButton editBtn = new JButton("Edit");
                editBtn.setPreferredSize(new Dimension(90, 28));
                editBtn.setBackground(new Color(70, 130, 180));
                editBtn.setForeground(Color.WHITE);
                editBtn.addActionListener(e -> {
                    appFrame.getEditDeckView().loadDeck(d.getId());
                    appFrame.navigateTo("EditDeck");
                });

                JPanel panel = new JPanel();
                panel.setOpaque(false);
                panel.add(editBtn);

                wrapper.add(panel, BorderLayout.SOUTH);

                gridPanel.add(wrapper);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    @Override
    public void notify(List<DeckDTO> data) {
        refreshData(null, null);
    }
}
