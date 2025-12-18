package org.flashcard.view;

import org.flashcard.controllers.*;
import javax.swing.*;
import java.awt.*;

/**
 * Acts as the primary window and high-level orchestrator of the application, managing
 * the switching between different screens and centralizing access to the business logic controllers.
 */

public class MainFrame extends JFrame {

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

    public MainFrame(UserController userController, StudyController studyController,
                     DeckController deckController, TagController tagController, FilterController filterController) {
        this.userController = userController;
        this.studyController = studyController;
        this.deckController = deckController;
        this.tagController = tagController;
        this.filterController = filterController;

        initFrame();
        autoLogin();
        initComponents();
    }

    private void initFrame() {
        setTitle("Flashcard APP");
        setSize(1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void autoLogin() {
        var users = userController.getAllUsers();

        if (users.isEmpty()) {
            promptCreateUser();
            return;
        }

        // Prefer user with ID = 1
        users.stream()
                .filter(u -> u.getId() == 1)
                .findFirst()
                .ifPresentOrElse(
                        u -> userController.loginByUserId(1),
                        () -> userController.loginByUserId(users.get(0).getId())
                );
    }

    private void promptCreateUser() {
        while (true) {
            String name = JOptionPane.showInputDialog(
                    null,
                    "No users found.\nPlease create a new user:",
                    "Create User",
                    JOptionPane.PLAIN_MESSAGE
            );

            name = name.trim();
            if (name.length() >= 3) {
                var newUser = userController.createUser(name);
                userController.loginByUserId(newUser.getId());
                return;
            }

            JOptionPane.showMessageDialog(
                    null,
                    "Username must be at least 3 characters.",
                    "Invalid Username",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void initComponents() {

        // Navbar with filter callbacks
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
        myDecksView = new MyDecksView(deckController, userController,filterController, this); // Now correct class
        createDeckView = new CreateDeckView(deckController, userController,tagController, this); // NEW
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
        if ("Home".equals(view)) homeView.refreshData(null,null);
        if ("MyDecks".equals(view)) myDecksView.refreshData(null, null);

        cardLayout.show(mainContentPanel, view);
    }

    // When search or tag changes
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
