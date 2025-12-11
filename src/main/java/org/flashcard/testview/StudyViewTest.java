package org.flashcard.testview;

import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.StudyController;
import org.flashcard.models.dataclasses.Deck;

import javax.swing.*;
import java.awt.*;

public class StudyViewTest extends JPanel {

    private final StudyController studyController;
    private final AppFrame appFrame;
    private final DeckController deckController;

    private JTextArea cardTextArea;
    private JPanel controlsPanel;
    private JButton showAnswerButton;
    private JPanel ratingPanel;
    private JPanel intervalPanel;
    private JButton nextButton; // NY: För Study All mode

    private FlashcardDTO currentCard;
    private String currentStrategy; // "today" eller "all"

    public StudyViewTest(DeckController deckController, StudyController studyController, AppFrame appFrame) {
        this.deckController = deckController;
        this.studyController = studyController;
        this.appFrame = appFrame;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // --- Card Area ---
        cardTextArea = new JTextArea();
        cardTextArea.setFont(new Font("SansSerif", Font.PLAIN, 24));
        cardTextArea.setLineWrap(true);
        cardTextArea.setWrapStyleWord(true);
        cardTextArea.setEditable(false);
        cardTextArea.setMargin(new Insets(40, 40, 40, 40));

        JPanel cardContainer = new JPanel(new BorderLayout());
        cardContainer.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        cardContainer.add(new JScrollPane(cardTextArea), BorderLayout.CENTER);

        add(cardContainer, BorderLayout.CENTER);

        // --- Controls Area ---
        controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        controlsPanel.setPreferredSize(new Dimension(800, 100));

        showAnswerButton = new JButton("Visa Svar");
        showAnswerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        showAnswerButton.setPreferredSize(new Dimension(200, 50));
        showAnswerButton.addActionListener(e -> showBack());

        // Rating Panel (För 'today' mode)
        JPanel ratingWrapper = new JPanel(new GridBagLayout());
        intervalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        GridBagConstraints constraints = new GridBagConstraints();
        addRatingIntervals();
        ratingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        ratingPanel.setVisible(false);
        createRatingButton("Again", "again", new Color(255, 80, 80));
        createRatingButton("Hard", "hard", new Color(255, 165, 0));
        createRatingButton("Medium", "medium", new Color(70, 130, 180));
        createRatingButton("Easy", "easy", new Color(60, 179, 113));
        ratingWrapper.add(intervalPanel);
        ratingWrapper.add(ratingPanel);

        // Next Button (För 'all' mode)
        nextButton = new JButton("Nästa Kort ->");
        nextButton.setPreferredSize(new Dimension(200, 50));
        nextButton.setBackground(new Color(60, 120, 240));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        nextButton.addActionListener(e -> loadNextCard()); // Bara ladda nästa, ingen rating
        nextButton.setVisible(false);

        controlsPanel.add(showAnswerButton);
        controlsPanel.add(ratingWrapper);
        controlsPanel.add(nextButton);

        add(controlsPanel, BorderLayout.SOUTH);
    }

    private void createRatingButton(String label, String ratingKey, Color color) {
        JButton btn = new JButton(label);
        btn.setPreferredSize(new Dimension(100, 50));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.addActionListener(e -> applyRating(ratingKey));
        ratingPanel.add(btn);
    }

    public void initSession(String strategy) {
        this.currentStrategy = strategy;
        loadNextCard();
    }

    private void loadNextCard() {
        currentCard = studyController.nextCard();

        if (currentCard == null) {
            JOptionPane.showMessageDialog(this, "Passet är slut!");
            appFrame.navigateTo("Home"); // Gå tillbaka dit vi kom ifrån egentligen
            return;
        }

        cardTextArea.setText(currentCard.getFront());

        showAnswerButton.setVisible(true);
        ratingPanel.setVisible(false);
        nextButton.setVisible(false); // Dölj alltid först

        controlsPanel.revalidate();
        controlsPanel.repaint();
    }

    private void showBack() {
        if (currentCard != null) {
            cardTextArea.setText(currentCard.getFront() + "\n\n----------------\n\n" + currentCard.getBack());
            showAnswerButton.setVisible(false);

            // Visa rätt kontroller baserat på strategi
            if ("all".equalsIgnoreCase(currentStrategy)) {
                nextButton.setVisible(true);
            } else {
                ratingPanel.setVisible(true);
            }
        }
    }

    private void applyRating(String rating) {
        try {
            studyController.applyRating(rating, currentCard.getId());
            loadNextCard();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fel vid rating: " + e.getMessage());
        }
    }
    private void addRatingIntervals(){
        JLabel text = new JLabel("ehej");

        intervalPanel.add(text);
//        intervalPanel.add(new JLabel(String.valueOf(deckController.showEstimatedDate("again", currentCard.getId()))));
//        intervalPanel.add(new JLabel(String.valueOf(deckController.showEstimatedDate("hard", currentCard.getId()))));
//        intervalPanel.add(new JLabel(String.valueOf(deckController.showEstimatedDate("medium", currentCard.getId()))));
//        intervalPanel.add(new JLabel(String.valueOf(deckController.showEstimatedDate("easy", currentCard.getId()))));


    }
}