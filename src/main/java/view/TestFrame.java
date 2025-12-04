package view;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.TagDTO; // Behövs för taggen
import org.flashcard.application.dto.UserDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.StudyController;
import org.flashcard.controllers.UserController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TestFrame extends JFrame {

    private JPanel contentPanel;
    private JLabel userLabel;

    private final UserController userController;
    private final DeckController deckController;
    private final StudyController studyController;

    public TestFrame(UserController userController,
                     StudyController studyController,
                     DeckController deckController) {

        this.userController = userController;
        this.studyController = studyController;
        this.deckController = deckController;

        setTitle("Test Frame - DTO Version");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        layoutComponents();

        // --- VIKTIGT: Logga in FÖRST, sen uppdatera UI ---
        // För testning hårdkodar vi inloggning av ID 1 här
        try {
            userController.loginByUserId(1);
        } catch (Exception e) {
            System.out.println("Kunde inte logga in user 1: " + e.getMessage());
        }

        refreshCurrentUser(); // Nu kommer denna hitta användaren
        refreshDecks();       // Och denna kommer hitta lekarna
    }

    private void initComponents() {
        // Label to display current user
        userLabel = new JLabel("No user");
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));
        userLabel.setForeground(Color.BLACK);

        // Main content panel
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.DARK_GRAY);
        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,20)); // grid: 3 columns, rows auto
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.add(userLabel);

        // NEW: Switch User button
        JButton switchUserButton = new JButton("Switch User");
        switchUserButton.addActionListener(e -> openSwitchUserDialog());
        headerPanel.add(switchUserButton);

        // NEW: Add Deck button
        JButton addDeckButton = new JButton("Add Deck");
        addDeckButton.addActionListener(e -> openAddDeckDialog());
        headerPanel.add(addDeckButton);


        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }

    private void openSwitchUserDialog() {
        // Fetch all users via your UserController
        List<UserDTO> users = userController.getAllUsers();
        // If method is different (like findAllUsers), tell me and I’ll adapt.

        if (users == null || users.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No users found in system.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ComboBox model
        JComboBox<UserDTO> userCombo = new JComboBox<>(users.toArray(new UserDTO[0]));

        // Show dialog
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select user to log in as:"));
        panel.add(userCombo);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Switch User",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            UserDTO selectedUser = (UserDTO) userCombo.getSelectedItem();

            if (selectedUser != null) {
                try {
                    userController.loginByUserId(selectedUser.getId());

                    refreshCurrentUser();
                    refreshDecks();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Could not switch user: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Call whenever current user changes
    public void refreshCurrentUser() {
        // HÄR VAR FELET: Vi hämtar DTO:n nu, inte Entiteten
        UserDTO currentUser = userController.getCurrentUser();

        if (currentUser != null) {
            userLabel.setText("Current User: " + currentUser.getUsername());
        } else {
            userLabel.setText("No user logged in");
        }
    }

    private void openAddDeckDialog() {

        UserDTO currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this,
                    "No user logged in.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Fetch tags
        List<TagDTO> tags = userController.getTagsForUser(currentUser.getId()); // or tagController if separate

        // Components for dialog
        JTextField titleField = new JTextField(15);
        JComboBox<TagDTO> tagCombo = new JComboBox<>();

        tagCombo.addItem(null); // “no tag”
        for (TagDTO t : tags) tagCombo.addItem(t);

        // Set renderer to display "No Tag" when item is null
        tagCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("No Tag");
                } else if (value instanceof TagDTO) {
                    setText(((TagDTO) value).getTitle());
                }
                return this;
            }
        });

        JTextField newTagField = new JTextField(10);
        JTextField newTagColorField = new JTextField("#cccccc", 7);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Deck name:"));
        panel.add(titleField);

        panel.add(new JLabel("Existing tag:"));
        panel.add(tagCombo);

        panel.add(new JLabel("OR new tag name:"));
        panel.add(newTagField);

        panel.add(new JLabel("New tag color hex:"));
        panel.add(newTagColorField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Create New Deck",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String deckTitle = titleField.getText().trim();
        if (deckTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Deck must have a title.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }



        TagDTO chosenTag = (TagDTO) tagCombo.getSelectedItem();
        TagDTO newTag = null;

// User typed a new tag name → create new tag
        if (!newTagField.getText().trim().isEmpty()) {
            String hex = newTagColorField.getText().trim();
            if (hex.startsWith("#")) hex = hex.substring(1);
            newTag = deckController.createTag(currentUser.getId(), newTagField.getText().trim(), hex);
        }

// Decide which tag to attach
        TagDTO tagToUse = newTag != null ? newTag : chosenTag;
        Integer tagId = tagToUse != null ? tagToUse.getId() : null;

// Create the deck with the resolved tag


        try {
            DeckDTO newDeck = deckController.createDeck(currentUser.getId(), deckTitle);
            deckController.assignTagToDeck(newDeck.getId(), tagId);

            refreshDecks();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not create deck: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    // Display all decks for current user in contentPanel
    public void refreshDecks() {
        contentPanel.removeAll();

        // Ändrat till UserDTO
        UserDTO currentUser = userController.getCurrentUser();

        if (currentUser != null) {
            // Anropa controllern som returnerar List<DeckDTO>
            // Obs: Kontrollera att metoden heter 'getAllDecksForUser' i din DeckController,
            // annars 'getDecksForUser'
            List<DeckDTO> decks = deckController.getAllDecksForUser(currentUser.getId());

            for (DeckDTO deck : decks) {
                JPanel card = createDeckCard(deck);
                contentPanel.add(card);
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Simple deck card panel using DTO
    private JPanel createDeckCard(DeckDTO deck) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        card.setPreferredSize(new Dimension(200, 140)); // space for tag

        // Deck title at center
        JLabel nameLabel = new JLabel(deck.getTitle());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(nameLabel, BorderLayout.CENTER);

        // Tag at bottom (if it exists)
        // Nu använder vi DTO:ns nestlade TagDTO
        if (deck.getTagDTO() != null) {
            TagDTO tag = deck.getTagDTO();

            JLabel tagLabel = new JLabel(tag.getTitle());
            tagLabel.setOpaque(true);

            // Fixa hex-färgen
            String hex = tag.getColorHex();
            if (hex != null && !hex.startsWith("#")) {
                hex = "#" + hex;
            }

            try {
                tagLabel.setBackground(Color.decode(hex));
                // Avgör textfärg baserat på bakgrund (enkelt hack: vit text på mörk)
                tagLabel.setForeground(Color.BLACK);
            } catch (Exception e) {
                tagLabel.setBackground(Color.LIGHT_GRAY); // Fallback
            }

            tagLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            tagLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
            tagPanel.setBackground(Color.WHITE);
            tagPanel.add(tagLabel);

            card.add(tagPanel, BorderLayout.SOUTH);
        }

        return card;
    }
}