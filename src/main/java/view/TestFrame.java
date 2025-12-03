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
        setSize(1000, 700);
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
        contentPanel.setLayout(new GridLayout(0, 3, 20, 20)); // grid: 3 columns, rows auto
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Add user label at the top
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.add(userLabel);

        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(contentPanel), BorderLayout.CENTER); // scroll if many decks
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