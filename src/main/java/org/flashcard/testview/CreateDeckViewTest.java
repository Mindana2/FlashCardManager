package org.flashcard.testview;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.UserController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;

public class CreateDeckViewTest extends JPanel {

    private final DeckController deckController;
    private final UserController userController;
    private final AppFrame appFrame;

    // State
    private DeckDTO createdDeck = null;
    private Color selectedTagColor = new Color(0x808080); // default gray

    // UI Components
    private JPanel formPanel;
    private JTextField titleField;

    // Tag UI
    private JComboBox<Object> existingTagsCombo; // items: "-- Ny tag --", TagDTO ...
    private JTextField newTagField;
    private JButton colorChooserButton;

    // Card fields
    private JTextField frontField;
    private JTextField backField;

    private JButton mainActionButton; // "Skapa Lek" eller "Lägg till Kort"
    private JButton finishButton;
    private JButton newDeckButton;
    private JLabel statusLabel;
    private JLabel headerLabel;

    public CreateDeckViewTest(DeckController deckController, UserController userController, AppFrame appFrame) {
        this.deckController = deckController;
        this.userController = userController;
        this.appFrame = appFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        // --- Header ---
        headerLabel = new JLabel("Skapa ny lek");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(headerLabel, BorderLayout.NORTH);

        // --- Center Form ---
        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));

        // Deck Fields (Visas först)
        titleField = createLabeledField("Titel på leken:", formPanel);

        // Tag chooser area
        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new BoxLayout(tagPanel, BoxLayout.Y_AXIS));
        tagPanel.setBackground(Color.WHITE);
        tagPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel tagLabel = new JLabel("Tagg:");
        tagLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tagLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tagPanel.add(tagLabel);
        tagPanel.add(Box.createVerticalStrut(6));

        // existing tags combo
        existingTagsCombo = new JComboBox<>();
        existingTagsCombo.setMaximumSize(new Dimension(500, 32));
        existingTagsCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        existingTagsCombo.addItem("-- Ny tagg --"); // index 0 means create new tag
        existingTagsCombo.addActionListener(e -> onTagSelectionChanged());
        tagPanel.add(existingTagsCombo);
        tagPanel.add(Box.createVerticalStrut(8));

        // new tag name input
        newTagField = new JTextField();
        newTagField.setMaximumSize(new Dimension(500, 36));
        newTagField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        newTagField.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagPanel.add(newTagField);
        tagPanel.add(Box.createVerticalStrut(8));

        // color chooser button row
        JPanel colorRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        colorRow.setBackground(Color.WHITE);
        colorRow.setMaximumSize(new Dimension(500, 36));

        colorChooserButton = new JButton("Välj färg");
        colorChooserButton.setBackground(selectedTagColor);
        colorChooserButton.setForeground(contrastColorFor(selectedTagColor));
        colorChooserButton.addActionListener(e -> openColorChooser());
        colorRow.add(colorChooserButton);

        JLabel colorHint = new JLabel();
        colorHint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        colorRow.add(colorHint);

        tagPanel.add(colorRow);

        formPanel.add(tagPanel);

        // Card Fields (Dolda först)
        frontField = createLabeledField("Framsida (Fråga):", formPanel);
        backField = createLabeledField("Baksida (Svar):", formPanel);
        toggleCardFields(false);

        // --- Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);

        mainActionButton = new JButton("Skapa Lek");
        mainActionButton.setPreferredSize(new Dimension(150, 40));
        mainActionButton.setBackground(new Color(65, 105, 225));
        mainActionButton.setForeground(Color.WHITE);
        mainActionButton.setFocusPainted(false);
        mainActionButton.addActionListener(e -> handleMainAction());

        finishButton = new JButton("Klar (Tillbaka)");
        finishButton.setPreferredSize(new Dimension(150, 40));
        finishButton.addActionListener(e -> resetAndGoBack());
        finishButton.setVisible(false); // Visas först när leken är skapad

        newDeckButton = new JButton("Skapa ny lek");
        newDeckButton.setPreferredSize(new Dimension(150, 40));
        newDeckButton.addActionListener(e -> resetFormForNewDeck());

        buttonPanel.add(mainActionButton);
        buttonPanel.add(finishButton);
        buttonPanel.add(newDeckButton);

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(buttonPanel);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(0, 150, 0));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(statusLabel);

        add(formPanel, BorderLayout.CENTER);

        // load tags initially for logged-in user (if any)
        SwingUtilities.invokeLater(this::loadTagsForCurrentUser);
    }

    private JTextField createLabeledField(String labelText, JPanel parent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(500, 60));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.PLAIN, 16));

        p.add(lbl, BorderLayout.NORTH);
        p.add(tf, BorderLayout.CENTER);

        parent.add(p);
        parent.add(Box.createVerticalStrut(15));
        return tf;
    }

    private void onTagSelectionChanged() {
        Object sel = existingTagsCombo.getSelectedItem();
        boolean creatingNew = (sel == null || sel instanceof String && ((String) sel).equals("-- Ny tagg --"));
        newTagField.setVisible(creatingNew);
        colorChooserButton.setVisible(creatingNew);
        revalidate();
        repaint();
    }

    private void openColorChooser() {
        Color c = JColorChooser.showDialog(this, "Välj taggfärg", selectedTagColor);
        if (c != null) {
            selectedTagColor = c;
            colorChooserButton.setBackground(selectedTagColor);
            colorChooserButton.setForeground(contrastColorFor(selectedTagColor));
        }
    }

    private Color contrastColorFor(Color c) {
        // enkel kontrast: välj vitt eller svart beroende på ljushet
        double luminance = (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) / 255;
        return luminance > 0.6 ? Color.BLACK : Color.WHITE;
    }

    private void loadTagsForCurrentUser() {
        Integer userId = userController.getCurrentUserId();
        existingTagsCombo.removeAllItems();
        existingTagsCombo.addItem("-- Ny tagg --");

        if (userId == null) return;
        try {
            List<TagDTO> tags = userController.getTagsForUser(userId);
            for (TagDTO t : tags) {
                existingTagsCombo.addItem(t);
            }
        } catch (Exception e) {
            // ignore or show small notice
            System.err.println("Kunde inte läsa taggar: " + e.getMessage());
        }
        onTagSelectionChanged();
    }

    private void toggleCardFields(boolean show) {
        frontField.getParent().setVisible(show);
        backField.getParent().setVisible(show);

        titleField.getParent().setVisible(!show);
        existingTagsCombo.getParent().setVisible(!show);
        newTagField.getParent().setVisible(!show); // parent är same wrapper so ensure correct
        // Instead of relying on parents, we simply toggle the visible state for the specific components:
        newTagField.setVisible(!(!show)); // keep it in layout but its visibility controlled by onTagSelectionChanged
        colorChooserButton.setVisible(!(!show));

        titleField.setEditable(!show);
    }

    private void handleMainAction() {
        if (createdDeck == null) {
            createDeck();
        } else {
            addCard();
        }
    }

    private void createDeck() {
        String title = titleField.getText().trim();
        Integer userId = userController.getCurrentUserId();

        if (title.isBlank()) {
            JOptionPane.showMessageDialog(this, "Du måste ange en titel!");
            return;
        }

        if (userId == null) {
            JOptionPane.showMessageDialog(this, "Ingen användare inloggad!");
            return;
        }

        if (deckController.deckExists(userId, title)) {
            JOptionPane.showMessageDialog(this, "Du har redan en lek med detta namn!");
            return;
        }

        try {
            // Skapa deck först
            createdDeck = deckController.createDeck(userId, title);

            // Tagghantering:
            Object sel = existingTagsCombo.getSelectedItem();
            if (sel == null || (sel instanceof String && ((String) sel).equals("-- Ny tagg --"))) {
                // skapa ny tagg om användaren angivit namn
                String newTagName = newTagField.getText().trim();
                if (!newTagName.isBlank()) {
                    // convert color to hex (RRGGBB)
                    String hex = String.format(Locale.ROOT, "%02x%02x%02x",
                            selectedTagColor.getRed(), selectedTagColor.getGreen(), selectedTagColor.getBlue()).toUpperCase();
                    try {
                        var tagDto = deckController.createTag(userId, newTagName, hex);
                        deckController.assignTagToDeck(createdDeck.getId(), tagDto.getId());
                        // uppdatera createdDeck
                        createdDeck = deckController.getDeckById(createdDeck.getId());
                        // reload tag-list so new tag appears
                        loadTagsForCurrentUser();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Deck skapad men tagg kunde ej läggas till: " + ex.getMessage());
                    }
                }
            } else if (sel instanceof TagDTO) {
                TagDTO t = (TagDTO) sel;
                try {
                    deckController.assignTagToDeck(createdDeck.getId(), t.getId());
                    createdDeck = deckController.getDeckById(createdDeck.getId());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lek skapad men tagg kunde ej kopplas: " + ex.getMessage());
                }
            }

            // Uppdatera UI för att lägga till kort
            headerLabel.setText("Lägg till kort i: " + createdDeck.getTitle());
            mainActionButton.setText("Lägg till Kort");
            finishButton.setVisible(true);
            toggleCardFields(true);
            statusLabel.setText("Lek skapad! Lägg nu till kort.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fel: " + e.getMessage());
            createdDeck = null;
        }
    }

    private void addCard() {
        String front = frontField.getText().trim();
        String back = backField.getText().trim();

        if (front.isBlank() || back.isBlank()) {
            JOptionPane.showMessageDialog(this, "Både framsida och baksida behövs.");
            return;
        }

        if (createdDeck == null) {
            JOptionPane.showMessageDialog(this, "Ingen lek vald att lägga till kort i.");
            return;
        }

        try {
            deckController.addFlashcard(createdDeck.getId(), front, back);

            // Hämta uppdaterad deck eller lista med kort så vi får korrekt antal
            createdDeck = deckController.getDeckById(createdDeck.getId());
            int count = createdDeck.getCardCount();

            // Rensa fält och ge feedback
            frontField.setText("");
            backField.setText("");
            frontField.requestFocus();
            statusLabel.setText("Kort tillagt! (" + count + " totalt)");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Kunde inte spara kort: " + e.getMessage());
        }
    }

    public void resetFormForNewDeck() {
        createdDeck = null;
        titleField.setText("");
        newTagField.setText("");
        frontField.setText("");
        backField.setText("");
        statusLabel.setText(" ");

        headerLabel.setText("Skapa ny lek");
        mainActionButton.setText("Skapa Lek");
        finishButton.setVisible(false);
        toggleCardFields(false);

        // reset color to default
        selectedTagColor = new Color(0x808080);
        colorChooserButton.setBackground(selectedTagColor);
        colorChooserButton.setForeground(contrastColorFor(selectedTagColor));

        // reload tags
        loadTagsForCurrentUser();
    }

    private void resetAndGoBack() {
        resetFormForNewDeck();
        appFrame.navigateTo("MyDecks");
    }
}
