package org.flashcard.testview;

import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.StudyController;
import org.flashcard.controllers.UserController;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {

    // Controllers
    private final UserController userController;
    private final StudyController studyController;
    private final DeckController deckController;

    // UI Components
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private Navbar navbar;

    // Views
    private HomeView homeView;
    private MyDecksView myDecksView; // Inte längre placeholder
    private CreateDeckView createDeckView; // NY
    private StudyView studyView;
    private EditDeckView editDeckView;

    public AppFrame(UserController userController, StudyController studyController, DeckController deckController) {
        this.userController = userController;
        this.studyController = studyController;
        this.deckController = deckController;

        initFrame();
        autoLoginForTesting();
        initComponents();
    }

    private void initFrame() {
        setTitle("Flashcard APP");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void autoLoginForTesting() {
        try {
            userController.loginByUserId(1);
            System.out.println("DEBUG: Auto-logged in as User ID 1");
        } catch (Exception e) {
            System.err.println("DEBUG: Kunde inte autologga in: " + e.getMessage());
        }
    }

    private void initComponents() {
        navbar = new Navbar(this::navigateTo);
        add(navbar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(new Color(245, 245, 245));

        // --- Initiera Vyer ---
        homeView = new HomeView(deckController, userController, this);
        myDecksView = new MyDecksView(deckController, userController, this); // Nu riktig klass
        createDeckView = new CreateDeckView(deckController, userController, this); // NY
        studyView = new StudyView(studyController,deckController, this);
        editDeckView = new EditDeckView(deckController, userController, this);

        mainContentPanel.add(homeView, "Home");
        mainContentPanel.add(myDecksView, "MyDecks");
        mainContentPanel.add(createDeckView, "CreateDeck"); // Lägg till i layouten
        mainContentPanel.add(studyView, "Study");
        mainContentPanel.add(editDeckView, "EditDeck");

        add(mainContentPanel, BorderLayout.CENTER);

        navigateTo("Home");
    }

    public void navigateTo(String viewName) {
        // If we leave StudyView, endsession
        Component current = getCurrentVisibleView();
        if (current == studyView) {
            studyController.endSession(false);
        }

        if ("Home".equals(viewName)) homeView.refreshData();
        if ("MyDecks".equals(viewName)) myDecksView.refreshData();

        cardLayout.show(mainContentPanel, viewName);
    }

    public CreateDeckView getCreateDeckView() {
        return createDeckView; // din instans av CreateDeckViewTest
    }
    private Component getCurrentVisibleView() {
        for (Component comp : mainContentPanel.getComponents()) {
            if (comp.isVisible()) return comp;
        }
        return null;
    }


    public EditDeckView getEditDeckView() { return editDeckView; }

    // Uppdaterad metod: Tar emot 'strategy' ("today" eller "all")
    public void startStudySession(int deckId, String strategy) {
        try {
            Integer userId = userController.getCurrentUserId();
            if (userId == null) {
                JOptionPane.showMessageDialog(this, "No User loggedIn!");
                return;
            }

            // Starta sessionen i controllern
            studyController.startSession(strategy, deckId, userId);

            // Konfigurera vyn (Skicka med strategin så vyn vet om den ska visa knappar eller ej)
            studyView.initSession(strategy);

            cardLayout.show(mainContentPanel, "Study");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Couldn't start the session : " + e.getMessage());
            e.printStackTrace();
        }
    }
}