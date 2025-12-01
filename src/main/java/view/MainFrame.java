package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private NavbarView navbarView;
    private FriendsView friendsView;

    private HomeView homeView;
    private MyDecksView myDecksView;
    private StudyView studyView;
    private ScheduleView scheduleView;
    private MyAccountView myAccountView;
    private SignInView signInView;

    private JLayeredPane layeredPane;      // transparent overlay container
    private JPanel overlayLayer;           // semi-transparent overlay
    private JPanel backgroundPanel;        // REAL background (fixar vit-buggen)

    public MainFrame() {
        initComponents();
        layoutComponents();
        showPage("Home");

        setTitle("Flashcard APP");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setNavbarListener();
    }

    private void initComponents() {

        // --------- CARD LAYOUT PANEL ---------
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG);

        // --------- ALLA VIEWS ---------
        homeView = new HomeView();
        myDecksView = new MyDecksView();
        studyView = new StudyView();
        scheduleView = new ScheduleView();
        myAccountView = new MyAccountView();
        signInView = new SignInView();

        navbarView = new NavbarView();
        friendsView = new FriendsView();

        contentPanel.add(homeView, "Home");
        contentPanel.add(myDecksView, "MyDecks");
        contentPanel.add(scheduleView, "Schedule");
        contentPanel.add(myAccountView, "MyAccount");
        contentPanel.add(signInView, "SignIn");

        // --------- OVERLAY LAYER ---------
        overlayLayer = new JPanel();
        overlayLayer.setOpaque(false);
        overlayLayer.setLayout(new BorderLayout());

        // --------- JLayeredPane (TRANSPARENT!) ---------
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new BorderLayout());

        // Lägg båda i CENTER med korrekt layer
        layeredPane.add(contentPanel, BorderLayout.CENTER, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(overlayLayer, BorderLayout.CENTER, JLayeredPane.PALETTE_LAYER);


        backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(Theme.BG);  // ← Detta tar bort vit bakgrund
    }

    private void layoutComponents() {

        setLayout(new BorderLayout());

        // Navbar alltid högst upp
        add(navbarView, BorderLayout.NORTH);

        // FriendsView på vänster sida
        add(friendsView, BorderLayout.WEST);

        // Lägg layeredPane i backgroundPanel
        backgroundPanel.add(layeredPane, BorderLayout.CENTER);

        // Lägg backgroundPanel i fönstret
        add(backgroundPanel, BorderLayout.CENTER);
    }

    // -------- PAGE SWITCHING --------
    public void showPage(String pageName) {
        cardLayout.show(contentPanel, pageName);

        if ("Schedule".equals(pageName)) {
            friendsView.setVisible(false);
        } else {
            friendsView.setVisible(true);
        }

        navbarView.highlight(pageName);

        revalidate();
        repaint();
    }

    private void setNavbarListener() {
        navbarView.setOnNavigate(this::showPage);
    }

    // -------- OVERLAY CONTROL (popup blur etc.) --------
    public void showOverlay(Color transparentDark) {
        overlayLayer.setOpaque(true);
        overlayLayer.setBackground(transparentDark);
        overlayLayer.repaint();
    }

    public void hideOverlay() {
        overlayLayer.setOpaque(false);
        overlayLayer.repaint();
    }
}
