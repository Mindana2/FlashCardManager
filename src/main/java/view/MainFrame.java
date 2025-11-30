package view;

import javax.swing.*;
import java.awt.*;
import java.util.NavigableMap;
import java.util.Set;


public class MainFrame extends JFrame {
    // Attributes that define the main frame can be added here
    // Navigering
    // Card layout to switch between different views
    // Card layout and content panel work together to manage different views in the main frame
    private CardLayout cardLayout;
    private JPanel contentPanel;

    // Always-visible components
    private NavbarView navbarView;
    private FriendsView friendsView;
    private boolean friendsVisible = true;


    // Pages
    private HomeView homeView;
    private MyDecksView myDecksView;
    private StudyView studyView;
    private ScheduleView scheduleView;
    private MyAccountView myAccountView;
    private SignInView signInView;

    // Overlay (for popup blur)
    private JPanel overlayLayer;   // används för blur eller popup-darkening

    public MainFrame(){
        initComponents();
        LayoutComponents();
        showPage("Home"); // Show home view by default

        // Frame settings
        setTitle("Flashcard APP");
        setSize(1920, 1080);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setNavbarListener();

    }

    private void initComponents() {
        // init card layout + contentPanel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG);



        //  init all views/pages
        homeView = new HomeView();
        myDecksView = new MyDecksView();
        studyView = new StudyView();
        scheduleView = new ScheduleView();
        myAccountView = new MyAccountView();
        signInView = new SignInView();
        navbarView = new NavbarView();
        friendsView = new FriendsView();

        // init overlay layer
        overlayLayer = new JPanel();
        overlayLayer.setOpaque(false); // Start as transparent
        overlayLayer.setLayout(new BorderLayout());


        // add views to content panel with unique identifiers
        contentPanel.add(homeView, "Home");
        contentPanel.add(myDecksView, "MyDecks");
        //contentPanel.add(studyView, "Study");
        contentPanel.add(scheduleView, "Schedule");
        contentPanel.add(myAccountView, "MyAccount");
        contentPanel.add(signInView, "SignIn");
    }

    // Layout setup
    private void LayoutComponents(){
        // Main frame layout setup
        setLayout(new BorderLayout());
        // add navbar to the top
        add(navbarView, BorderLayout.NORTH);
        // add friends view to the left/west
        add(friendsView, BorderLayout.WEST);
        //content panel in center
        add(contentPanel, BorderLayout.CENTER);
        // add overlay layer on top of content panel
        add(overlayLayer, BorderLayout.CENTER);
    }

    // Methods to switch views, toggle friends view, and manage overlay can be added here
    // Ändrar view helt enkelt i contentPanel
    public void showPage(String pageName) {
        cardLayout.show(contentPanel, pageName);
        navbarView.highlight(pageName); // Highlight the active page in the navbar
    }

    public void setNavbarListener(){
        navbarView.setOnNavigate(this::showPage);
    }

}
