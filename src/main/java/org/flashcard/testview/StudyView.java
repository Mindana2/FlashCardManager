package org.flashcard.testview;

import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.controllers.StudyController;
import org.flashcard.controllers.observer.Observer;  // NEW

import javax.swing.*;
import java.awt.*;

public class StudyView extends JPanel implements Observer<FlashcardDTO> {

    private final StudyController studyController;
    private final AppFrame appFrame;

    private JTextArea cardTextArea;
    private JPanel controlsPanel;
    private JButton showAnswerButton;
    private JPanel ratingPanel;
    private JButton nextButton;

    private FlashcardDTO currentCard;
    private String currentStrategy;

    // Observer for session finished
    private final Observer<Boolean> finishedListener = finished -> {
        if (finished != null && finished) {
            handleSessionFinished();
        }
    };

    public StudyView(StudyController studyController, AppFrame appFrame) {
        this.studyController = studyController;
        this.appFrame = appFrame;

        // Register observers
        studyController.getCurrentCardObservable().addListener(this);
        studyController.getSessionFinishedObservable().addListener(finishedListener);

        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {

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

        controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        controlsPanel.setPreferredSize(new Dimension(800, 100));

        showAnswerButton = new JButton("Show Answer");
        showAnswerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        showAnswerButton.setPreferredSize(new Dimension(200, 50));
        showAnswerButton.addActionListener(e -> showBack());

        ratingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        ratingPanel.setVisible(false);

        createRatingButton("Again", "again", new Color(255, 80, 80));
        createRatingButton("Hard", "hard", new Color(255, 165, 0));
        createRatingButton("Medium", "medium", new Color(70, 130, 180));
        createRatingButton("Easy", "easy", new Color(60, 179, 113));

        nextButton = new JButton("Next Card ->");
        nextButton.setPreferredSize(new Dimension(200, 50));
        nextButton.setBackground(new Color(60, 120, 240));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        nextButton.addActionListener(e -> studyController.nextCard());
        nextButton.setVisible(false);

        controlsPanel.add(showAnswerButton);
        controlsPanel.add(ratingPanel);
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

        // Previously you called loadNextCard() here, now StudyController does it
    }

    // Observer callback for new card
    @Override
    public void notify(FlashcardDTO card) {
        SwingUtilities.invokeLater(() -> showNewCard(card));
    }

    private void showNewCard(FlashcardDTO card) {
        this.currentCard = card;

        if (card == null) {
            handleSessionFinished();
            return;
        }

        cardTextArea.setText(card.getFront());

        showAnswerButton.setVisible(true);
        ratingPanel.setVisible(false);
        nextButton.setVisible(false);

        controlsPanel.revalidate();
        controlsPanel.repaint();
    }

    private void showBack() {
        if (currentCard != null) {
            cardTextArea.setText(currentCard.getFront()
                    + "\n\n----------------\n\n"
                    + currentCard.getBack());

            showAnswerButton.setVisible(false);

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
            studyController.nextCard();  // Observer will handle UI update
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while rating: " + e.getMessage());
        }
    }

    // Called when sessionFinishedObservable fires
    private void handleSessionFinished() {
        JOptionPane.showMessageDialog(this, "The session is over!");
        appFrame.navigateTo("Home");
    }
}
