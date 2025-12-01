package view;

import javax.swing.*;
import java.awt.*;

public class NavbarView extends JPanel {
    NavigationListener listener;

    // Navigation Buttons
    private JButton homeBtn;
    private JButton decksBtn;
    private JButton scheduleBtn;
    private JButton signinBtn;
    private static final Font ACTIVE = Theme.NORMAL.deriveFont(Font.BOLD);


    // Search box
    private SearchBarView searchBox;

    // panels for layout
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;

    public NavbarView(){
        setLayout(new BorderLayout());
        setBackground(Theme.NAV_BG);
        setPreferredSize(new Dimension(0, 60));

        initComponents();
        layoutComponents();
        styleComponents();
        addListeners();
    }

    public void setOnNavigate(NavigationListener listener) {
            this.listener = listener;
    }

    private void initComponents(){
        homeBtn = new JButton("Home");
        decksBtn = new JButton("My Decks");
        scheduleBtn = new JButton("Schedule");
        signinBtn = new JButton("Sign In");

        searchBox = new SearchBarView(200,10); // width 200px

        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15,10));
        leftPanel.setOpaque(false);
        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15,10));
        centerPanel.setOpaque(false);
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15,10));
        rightPanel.setOpaque(false);
    }

    private void layoutComponents(){
        //add buttons to left panel
        leftPanel.add(homeBtn);
        leftPanel.add(decksBtn);
        leftPanel.add(scheduleBtn);
        rightPanel.add(signinBtn);

        // add search box to center panel
        centerPanel.add(searchBox);

        //add panels to navbar layout
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

    }

    // This method changes the page when a button is clicked

    private void addListeners(){
        homeBtn.addActionListener(e -> listener.onNavigate("Home"));
        decksBtn.addActionListener(e -> listener.onNavigate("MyDecks"));
        scheduleBtn.addActionListener(e -> listener.onNavigate("Schedule"));
        signinBtn.addActionListener(e -> listener.onNavigate("SignIn"));

    }

    private void styleButton(JButton btn) {
        btn.setBackground(Theme.NAV_BG);
        btn.setForeground(Theme.TEXT);
        btn.setBorder(BorderFactory.createEmptyBorder(8,14,8,14));
        btn.setFocusPainted(false);
        btn.setFont(Theme.NORMAL);
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(120, 32));


        // Hover effect
        btn.addChangeListener(e -> {
            if (btn.getModel().isRollover()) {
                btn.setBackground(Theme.NAV_BG.brighter());
            }
            else {
                btn.setBackground(Theme.NAV_BG);
            }
        });
    }

    private void styleComponents(){
        styleButton(homeBtn);
        styleButton(decksBtn);
        styleButton(scheduleBtn);
        styleButton(signinBtn);

        // Style search box
        searchBox.applyTheme(Theme.TEXT, Theme.NAV_BG);

        // borderline at bottom of navbar
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.BORDER));
    }

    // Metod which highlights the active button
    public void highlight(String pageName) {
        homeBtn.setFont(Theme.NORMAL);
        decksBtn.setFont(Theme.NORMAL);
        scheduleBtn.setFont(Theme.NORMAL);
        signinBtn.setFont(Theme.NORMAL);

        switch (pageName) {
            case "Home" -> homeBtn.setFont(ACTIVE);
            case "MyDecks" -> decksBtn.setFont(ACTIVE);
            case "Schedule" -> scheduleBtn.setFont(ACTIVE);
            case "SignIn" -> signinBtn.setFont(ACTIVE);
        }
    }


    private void resetButtonStyles() {
        homeBtn.setFont(Theme.NORMAL);
        decksBtn.setFont(Theme.NORMAL);
        scheduleBtn.setFont(Theme.NORMAL);
        signinBtn.setFont(Theme.NORMAL);
    }


}
