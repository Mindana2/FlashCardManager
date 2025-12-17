package org.flashcard.view;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.TagController;
import org.flashcard.controllers.UserController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;

public class CreateDeckView extends JPanel {

    private final DeckController deckController;
    private final UserController userController;
    private final TagController tagController;
    private final MainFrame mainFrame;

    private DeckDTO createdDeck = null;
    private Color selectedTagColor = new Color(0x808080);

    private JPanel formPanel;
    private JTextField titleField;
    private JComboBox<Object> existingTagsCombo;
    private JTextField newTagField;
    private JButton colorChooserButton;

    private JTextField frontField;
    private JTextField backField;

    private JButton mainActionButton;
    private JButton finishButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    private JLabel headerLabel;

    public CreateDeckView(DeckController deckController, UserController userController,
                          TagController tagController, MainFrame mainFrame) {
        this.deckController = deckController;
        this.userController = userController;
        this.tagController = tagController;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {

        headerLabel = new JLabel("Create new Deck");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(headerLabel, BorderLayout.NORTH);

        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));

        titleField = createLabeledField("Deck Title:", formPanel);

        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new BoxLayout(tagPanel, BoxLayout.Y_AXIS));
        tagPanel.setBackground(Color.WHITE);
        tagPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel tagLabel = new JLabel("Tag:");
        tagLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tagLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        tagPanel.add(tagLabel);
        tagPanel.add(Box.createVerticalStrut(6));

        existingTagsCombo = new JComboBox<>();
        existingTagsCombo.setMaximumSize(new Dimension(500, 32));
        existingTagsCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        existingTagsCombo.addItem("-- New Tag --");
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

        colorChooserButton = new JButton("Choose Color");
        colorChooserButton.setBackground(selectedTagColor);
        colorChooserButton.setForeground(contrastColorFor(selectedTagColor));
        colorChooserButton.addActionListener(e -> openColorChooser());
        colorRow.add(colorChooserButton);

        tagPanel.add(colorRow);
        formPanel.add(tagPanel);

        frontField = createLabeledField("Front Side (Question):", formPanel);
        backField = createLabeledField("Back Side (Answer):", formPanel);
        toggleCardFields(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);

        mainActionButton = new JButton("Create Deck");
        mainActionButton.setPreferredSize(new Dimension(150, 40));
        mainActionButton.setBackground(new Color(65, 105, 225));
        mainActionButton.setForeground(Color.WHITE);
        mainActionButton.setFocusPainted(false);
        mainActionButton.addActionListener(e -> handleMainAction());

        finishButton = new JButton("Done (Back to My Decks)");
        finishButton.setPreferredSize(new Dimension(150, 40));
        finishButton.addActionListener(e -> resetAndGoBack());
        finishButton.setVisible(false);

        cancelButton = new JButton("Close");
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setBackground(new Color(200, 60, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> resetAndGoBack());

        buttonPanel.add(cancelButton);
        buttonPanel.add(mainActionButton);
        buttonPanel.add(finishButton);

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

        boolean creatingNew = (sel == null || sel instanceof String);

        newTagField.setVisible(creatingNew);
        colorChooserButton.setVisible(creatingNew);

        revalidate();
        repaint();
    }

    private void openColorChooser() {
        Color c = JColorChooser.showDialog(this, "Choose Tag Color", selectedTagColor);
        if (c != null) {
            selectedTagColor = c;
            colorChooserButton.setBackground(c);
            colorChooserButton.setForeground(contrastColorFor(c));
        }
    }

    private Color contrastColorFor(Color c) {
        double luminance = (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) / 255;
        return luminance > 0.6 ? Color.BLACK : Color.WHITE;
    }

    private void loadTagsForCurrentUser() {
        Integer userId = userController.getCurrentUserId();
        existingTagsCombo.removeAllItems();
        existingTagsCombo.addItem("-- New Tag --");

        if (userId == null) return;

        try {
            List<TagDTO> tags = tagController.getTagsForUser(userId);
            for (TagDTO t : tags) existingTagsCombo.addItem(t);
        } catch (Exception e) {
            System.err.println("Can't read tags: " + e.getMessage());
        }

        onTagSelectionChanged();
    }

    private void toggleCardFields(boolean show) {
        frontField.getParent().setVisible(show);
        backField.getParent().setVisible(show);

        titleField.getParent().setVisible(!show);
        existingTagsCombo.getParent().setVisible(!show);
        newTagField.getParent().setVisible(!show);
        colorChooserButton.setVisible(!show);
    }

    private void handleMainAction() {
        createDeckAndOpenEditor();
    }
    private void createDeckAndOpenEditor() {
        String title = titleField.getText().trim();
        Integer userId = userController.getCurrentUserId();

        if (title.isBlank()) {
            showStyledMessage("Wrong", "You must assign a title!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (deckController.deckExists(userId, title)) {
            showStyledMessage("Deck exists", "You already have a deck with this title.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            DeckDTO deck = deckController.createDeck(userId, title);

            Object sel = existingTagsCombo.getSelectedItem();
            if (sel instanceof TagDTO t) {
                deckController.assignTagToDeck(deck.getId(), t.getId());
            } else {
                String newTagName = newTagField.getText().trim();
                if (!newTagName.isBlank()) {
                    String hex = String.format(
                            Locale.ROOT,
                            "%02X%02X%02X",
                            selectedTagColor.getRed(),
                            selectedTagColor.getGreen(),
                            selectedTagColor.getBlue()
                    );
                    TagDTO t = tagController.createTag(userId, newTagName, hex);
                    deckController.assignTagToDeck(deck.getId(), t.getId());
                }
            }
            mainFrame.navigateTo("EditDeck");
            mainFrame.getEditDeckView().loadDeck(deck.getId());

        } catch (Exception e) {
            showStyledMessage("Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    public void resetFormForNewDeck() {
        createdDeck = null;
        titleField.setText("");
        newTagField.setText("");
        frontField.setText("");
        backField.setText("");
        statusLabel.setText(" ");

        headerLabel.setText("Create new Deck");
        mainActionButton.setText("Create Deck");
        finishButton.setVisible(false);
        toggleCardFields(false);

        loadTagsForCurrentUser();
    }

    private void resetAndGoBack() {
        resetFormForNewDeck();
        mainFrame.navigateTo("MyDecks");
    }

    private void showStyledMessage(String title, String message, int type) {
        JLabel label = new JLabel("<html><div style='text-align:center;'>" + message + "</div></html>");
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JOptionPane.showMessageDialog(this, label, title, type);
    }
}
