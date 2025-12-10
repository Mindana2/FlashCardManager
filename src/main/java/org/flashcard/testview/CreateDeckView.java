package org.flashcard.testview;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.UserController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;

public class CreateDeckView extends JPanel {

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
    private JComboBox<Object> existingTagsCombo;
    private JTextField newTagField;
    private JButton colorChooserButton;

    // Card fields
    private JTextField frontField;
    private JTextField backField;

    private JButton mainActionButton;
    private JButton finishButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    private JLabel headerLabel;

    public CreateDeckView(DeckController deckController, UserController userController, AppFrame appFrame) {
        this.deckController = deckController;
        this.userController = userController;
        this.appFrame = appFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {

        headerLabel = new JLabel("Skapa ny lek");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(headerLabel, BorderLayout.NORTH);

        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));

        titleField = createLabeledField("Titel på leken:", formPanel);

        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new BoxLayout(tagPanel, BoxLayout.Y_AXIS));
        tagPanel.setBackground(Color.WHITE);
        tagPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel tagLabel = new JLabel("Tagg:");
        tagLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tagLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tagPanel.add(tagLabel);
        tagPanel.add(Box.createVerticalStrut(6));

        existingTagsCombo = new JComboBox<>();
        existingTagsCombo.setMaximumSize(new Dimension(500, 32));
        existingTagsCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        existingTagsCombo.addItem("-- Ny tagg --");
        existingTagsCombo.addActionListener(e -> onTagSelectionChanged());
        tagPanel.add(existingTagsCombo);
        tagPanel.add(Box.createVerticalStrut(8));

        newTagField = new JTextField();
        newTagField.setMaximumSize(new Dimension(500, 36));
        newTagField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        newTagField.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagPanel.add(newTagField);
        tagPanel.add(Box.createVerticalStrut(8));

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

        frontField = createLabeledField("Framsida (Fråga):", formPanel);
        backField = createLabeledField("Baksida (Svar):", formPanel);
        toggleCardFields(false);

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
        finishButton.setVisible(false);

        // Cancel (Avbryt) button
        cancelButton = new JButton("Avbryt");
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setBackground(new Color(200, 60, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> resetAndGoBack());

        buttonPanel.add(mainActionButton);
        buttonPanel.add(finishButton);
        buttonPanel.add(cancelButton);

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(buttonPanel);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(0, 150, 0));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(statusLabel);

        add(formPanel, BorderLayout.CENTER);

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
            System.err.println("Kunde inte läsa taggar: " + e.getMessage());
        }
        onTagSelectionChanged();
    }

    private void toggleCardFields(boolean show) {
        frontField.getParent().setVisible(show);
        backField.getParent().setVisible(show);

        titleField.getParent().setVisible(!show);
        existingTagsCombo.getParent().setVisible(!show);
        newTagField.getParent().setVisible(!show);
        newTagField.setVisible(!(!show));
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
            showStyledMessage("Fel", "Du måste ange en titel!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userId == null) {
            showStyledMessage("Fel", "Ingen användare inloggad!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (deckController.deckExists(userId, title)) {
            // Nice styled message instead of raw SQL/text
            showStyledMessage("Du har redan en lek med detta namn!", "Du har redan en lek med detta namn!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            createdDeck = deckController.createDeck(userId, title);

            Object sel = existingTagsCombo.getSelectedItem();
            if (sel == null || (sel instanceof String && ((String) sel).equals("-- Ny tagg --"))) {
                String newTagName = newTagField.getText().trim();
                if (!newTagName.isBlank()) {
                    String hex = String.format(Locale.ROOT, "%02x%02x%02x",
                            selectedTagColor.getRed(), selectedTagColor.getGreen(), selectedTagColor.getBlue()).toUpperCase();
                    try {
                        var tagDto = deckController.createTag(userId, newTagName, hex);
                        deckController.assignTagToDeck(createdDeck.getId(), tagDto.getId());
                        createdDeck = deckController.getDeckById(createdDeck.getId());
                        loadTagsForCurrentUser();
                    } catch (Exception ex) {
                        showStyledMessage("Varning", "Deck skapad men tagg kunde ej läggas till: " + ex.getMessage(), JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else if (sel instanceof TagDTO) {
                TagDTO t = (TagDTO) sel;
                try {
                    deckController.assignTagToDeck(createdDeck.getId(), t.getId());
                    createdDeck = deckController.getDeckById(createdDeck.getId());
                } catch (Exception ex) {
                    showStyledMessage("Varning", "Lek skapad men tagg kunde ej kopplas: " + ex.getMessage(), JOptionPane.WARNING_MESSAGE);
                }
            }

            headerLabel.setText("Lägg till kort i: " + createdDeck.getTitle());
            mainActionButton.setText("Lägg till Kort");
            finishButton.setVisible(true);
            toggleCardFields(true);
            statusLabel.setText("Lek skapad! Lägg nu till kort.");

        } catch (Exception e) {
            showStyledMessage("Fel", "Fel: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            createdDeck = null;
        }
    }

    private void addCard() {
        String front = frontField.getText().trim();
        String back = backField.getText().trim();

        if (front.isBlank() || back.isBlank()) {
            showStyledMessage("Fel", "Både framsida och baksida behövs.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (createdDeck == null) {
            showStyledMessage("Fel", "Ingen lek vald att lägga till kort i.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            deckController.addFlashcard(createdDeck.getId(), front, back);
            createdDeck = deckController.getDeckById(createdDeck.getId());
            int count = createdDeck.getCardCount();

            frontField.setText("");
            backField.setText("");
            frontField.requestFocus();
            statusLabel.setText("Kort tillagt! (" + count + " totalt)");

        } catch (Exception e) {
            // If addFlashcard throws e.g. IllegalArgumentException for duplicate name, show styled message
            showStyledMessage("Fel vid sparande", e.getMessage(), JOptionPane.ERROR_MESSAGE);
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

        selectedTagColor = new Color(0x808080);
        colorChooserButton.setBackground(selectedTagColor);
        colorChooserButton.setForeground(contrastColorFor(selectedTagColor));

        loadTagsForCurrentUser();
    }

    private void resetAndGoBack() {
        resetFormForNewDeck();
        appFrame.navigateTo("MyDecks");
    }

    /**
     * Visar ett stylat meddelandecenterat i en JOptionPane.
     * messageTitle kan vara rubrik; messageText är texten.
     * messageType: JOptionPane.INFORMATION_MESSAGE / WARNING_MESSAGE / ERROR_MESSAGE
     */
    private void showStyledMessage(String messageTitle, String messageText, int messageType) {
        // Använd HTML för att centrera och göra texten fet och större
        String html = "<html><div style='text-align:center; font-weight:bold; font-size:13pt;'>" +
                escapeHtml(messageText) +
                "</div></html>";

        JLabel label = new JLabel(html);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Visa meddelandet med standard-ikon beroende på typ
        JOptionPane.showMessageDialog(this, label, messageTitle, messageType);
    }

    /** Enkel HTML-escape för säker rendering i JLabel */
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br/>");
    }
}
