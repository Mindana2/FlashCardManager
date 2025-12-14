package org.flashcard.testview;

import org.flashcard.controllers.*;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {

    private final UserController userController;
    private final StudyController studyController;
    private final DeckController deckController;
    private final TagController tagController;
    private final FilterController filterController;

    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    private Navbar navbar;

    private HomeView homeView;
    private MyDecksView myDecksView;
    private CreateDeckView createDeckView;
    private StudyView studyView;
    private EditDeckView editDeckView;

    public AppFrame(UserController userController, StudyController studyController,
                    DeckController deckController, TagController tagController, FilterController filterController) {
        this.userController = userController;
        this.studyController = studyController;
        this.deckController = deckController;
        this.tagController = tagController;
        this.filterController = filterController;

        initFrame();
        autoLoginForTesting();
        initComponents();
    }

    private void initFrame() {
        setTitle("Flashcard APP");
        setSize(1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void autoLoginForTesting() {
        try {
            userController.loginByUserId(1);
        } catch (Exception e) {
            System.err.println("Auto-login failed: " + e.getMessage());
        }
    }

    private void initComponents() {

        // Navbar med filter callbacks

        navbar = new Navbar(
                this::navigateTo,
                this::applyFilters,
                userController,
                tagController
        );
        add(navbar, BorderLayout.NORTH);


        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        homeView = new HomeView(deckController, userController,filterController, this);
        myDecksView = new MyDecksView(deckController, userController,filterController, this); // Nu riktig klass
        createDeckView = new CreateDeckView(deckController, userController,tagController, this); // NY
        studyView = new StudyView(studyController,deckController, this);
        editDeckView = new EditDeckView(deckController, userController, this);

        mainContentPanel.add(homeView, "Home");
        mainContentPanel.add(myDecksView, "MyDecks");
        mainContentPanel.add(createDeckView, "CreateDeck");
        mainContentPanel.add(studyView, "Study");
        mainContentPanel.add(editDeckView, "EditDeck");

        add(mainContentPanel, BorderLayout.CENTER);

        navigateTo("Home");
    }

    public void navigateTo(String view) {
        if ("Home".equals(view)) homeView.refreshData(null, null);
        if ("MyDecks".equals(view)) myDecksView.refreshData(null, null);

        cardLayout.show(mainContentPanel, view);
    }

    // När search eller tag ändras
    public void applyFilters() {
        String search = navbar.getSearchText();
        Integer tagId = navbar.getSelectedTagId();

        homeView.applyFilter(search, tagId);
        myDecksView.applyFilter(search, tagId);
    }

    public CreateDeckView getCreateDeckView() {
        return createDeckView;
    }
    private Component getCurrentVisibleView() {
        for (Component comp : mainContentPanel.getComponents()) {
            if (comp.isVisible()) return comp;
        }
        return null;
    }


    public EditDeckView getEditDeckView() {
        return editDeckView;
    }

    public void startStudySession(int deckId, String strategy) {
        try {
            Integer userId = userController.getCurrentUserId();
            if (userId == null) {
                JOptionPane.showMessageDialog(this, "No user logged in.");
                return;
            }

            studyController.startSession(strategy, deckId, userId);
            studyView.initSession(strategy);
            cardLayout.show(mainContentPanel, "Study");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot start study session: " + e.getMessage());
        }
    }
}
