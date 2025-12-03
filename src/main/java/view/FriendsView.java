package view;

import org.flashcard.application.dto.UserDTO;
import org.flashcard.controllers.UserController;
import org.flashcard.models.dataclasses.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FriendsView extends JPanel {
    private final UserController userController;
    private JButton toggleButton;  // pil
    private JPanel headerPanel;
    private JPanel listPanel; // lista för vänner
    private JScrollPane scrollPane;
    private boolean isOpen = true;
    private JLabel friendsLabel;
    // Konstruktor

    public FriendsView(UserController userController) {
        this.userController = userController;
        initComponents();
        layoutComponents();
        styleComponents();
        addListeners();

        refreshFriendsList();
    }

    private void initComponents(){

        // PILKNAPP
        toggleButton = new JButton("⮜");
        toggleButton.setFocusPainted(false);
        toggleButton.setBorder(null);

        // TEXT: "Friends"
        friendsLabel = new JLabel("Friends");
        friendsLabel.setForeground(Theme.TEXT);
        friendsLabel.setFont(Theme.MEDIUM);
        friendsLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 60));

        // HEADER (Friends + Pil)
        headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        headerPanel.setOpaque(false);

        headerPanel.add(friendsLabel);    // <-- FIX
        headerPanel.add(toggleButton);

        // LIST PANEL
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        // SCROLL
        scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
    }


    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // BorderLine för att separera vänsterpanelen
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Theme.BORDER));
        setPreferredSize(new Dimension(175, getPreferredSize().height));
    }

    // Add Listeners för att öppna/stänga friendspanelen
    // i FriendsView (klassnivå)
    public void addListeners() {
        toggleButton.addActionListener(e -> {
            if (isOpen) close(); else open();
        });
    }

    // publika metoder för att öppna/stänga (lägg i FriendsView)
    public void open() {
        isOpen = true;
        toggleButton.setText("⮜");

        listPanel.setVisible(true);
        friendsLabel.setVisible(true);

        // Expand panelen igen
        setPreferredSize(new Dimension(175, getHeight()));

        revalidate();
        repaint();
    }

    public void close() {
        isOpen = false;
        toggleButton.setText("⮞");

        listPanel.setVisible(false);
        friendsLabel.setVisible(false);

        // Collapse panelen visuellt!
        setPreferredSize(new Dimension(25, getHeight()));

        revalidate();
        repaint();
    }


    public boolean isOpen() { return isOpen; }


    // Style components
    public void styleComponents() {
        setBackground(Theme.BG);
        toggleButton.setBackground(Theme.BG);
        toggleButton.setForeground(Theme.TEXT);
    }

    public void refreshFriendsList() {
        listPanel.removeAll(); // clear existing entries

        // Fetch all users from controller
        List<UserDTO> users = userController.getAllUsers();
        UserDTO currentUser = userController.getCurrentUser(); // get the currently signed-in user


        for (UserDTO user : users) {
            JButton userButton = new JButton(user.getUsername());
            userButton.setFocusPainted(false);
            userButton.setContentAreaFilled(false);
            userButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            userButton.setForeground(Theme.TEXT);
            userButton.setFont(Theme.NORMAL);
            userButton.setHorizontalAlignment(SwingConstants.LEFT);


            // Make current user bold
            if (currentUser != null && currentUser.getUsername().equals(user.getUsername())) {
                userButton.setFont(Theme.NORMAL.deriveFont(Font.BOLD));
            } else {
                userButton.setFont(Theme.NORMAL);
            }



            // When clicked, "sign in" as this user
            userButton.addActionListener(e -> {
                userController.loginByUserId(user.getId());
                JOptionPane.showMessageDialog(this,
                        "You are now signed in as: " + user.getUsername(),
                        "Signed In",
                        JOptionPane.INFORMATION_MESSAGE);

            });

            listPanel.add(userButton);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void openUserProfile(User user) {
        // Auto-login as this user
        userController.loginByUserId(user.getId());

        // Optionally, update UI or navigate to Home
        JOptionPane.showMessageDialog(this,
                "You are now signed in as: " + user.getUsername(),
                "Signed In",
                JOptionPane.INFORMATION_MESSAGE);

        // For example, hide friends panel or refresh views
        close();
    }







}
