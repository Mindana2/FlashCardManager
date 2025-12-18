package org.flashcard.view;


import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.controllers.DeckController;
import org.flashcard.controllers.StudyController;
import org.flashcard.controllers.observer.Observer;
import javax.swing.*;
import java.awt.*;

/**
 * Implements the interactive learning interface, supporting both free-study and
 * algorithmic spaced-repetition modes with real-time feedback on review intervals.
 */

public class StudyView extends JPanel implements Observer<FlashcardDTO> {

    private final StudyController studyController;
    private final MainFrame mainFrame;
    private final DeckController deckController;

    private JTextArea cardTextArea;
    private JPanel controlsPanel;
    private JButton showAnswerButton;
    private JPanel ratingPanel;
    private JButton nextButton;
    private JPanel intervalPanel;


    private FlashcardDTO currentCard;
    private String currentMode;

    private final Observer<Boolean> finishedListener = finished -> {
        if (finished != null && finished) handleSessionFinished();
    };

    public StudyView(StudyController studyController, DeckController deckController, MainFrame mainFrame) {
        this.deckController = deckController;
        this.studyController = studyController;
        this.mainFrame = mainFrame;

        studyController.getCurrentCardObservable().addListener(this);
        studyController.getSessionFinishedObservable().addListener(finishedListener);

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

        showAnswerButton = new JButton("Show Answer");
        showAnswerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        showAnswerButton.setPreferredSize(new Dimension(200, 50));
        showAnswerButton.addActionListener(e -> showBack());

        // Rating Panel (For 'today' mode)
        JPanel ratingWrapper = new JPanel();
        ratingWrapper.setLayout(new BoxLayout(ratingWrapper, BoxLayout.Y_AXIS));

        intervalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 95, 0));
        intervalPanel.setVisible(false);


        ratingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        ratingPanel.setVisible(false);

        createRatingButton("Again", "again", new Color(255, 80, 80));
        createRatingButton("Hard", "hard", new Color(255, 165, 0));
        createRatingButton("Medium", "medium", new Color(70, 130, 180));
        createRatingButton("Easy", "easy", new Color(60, 179, 113));
        ratingWrapper.add(intervalPanel);
        ratingWrapper.add(ratingPanel);

        nextButton = new JButton("Next Card →");
        nextButton.setPreferredSize(new Dimension(200, 50));
        nextButton.setBackground(new Color(60, 120, 240));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        nextButton.addActionListener(e -> studyController.nextCardAndNotify());
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

    public void initSession(String currentMode) {
        this.currentMode = currentMode;
    }

    @Override
    public void notify(FlashcardDTO card) {
        SwingUtilities.invokeLater(() -> showNewCard(card));
    }

    private void showNewCard(FlashcardDTO card) {
        currentCard = card;

        if (card == null) {
            handleSessionFinished();
            return;
        }
        updateIntervals();

        cardTextArea.setText(card.getFront());

        showAnswerButton.setVisible(true);
        ratingPanel.setVisible(false);
        intervalPanel.setVisible(false);
        nextButton.setVisible(false);

        controlsPanel.revalidate();
        controlsPanel.repaint();
    }

    private void showBack() {
        if (currentCard != null) {

            cardTextArea.setText(
                    currentCard.getFront() +
                            "\n\n----------------\n\n" +
                            currentCard.getBack()
            );

            showAnswerButton.setVisible(false);

            // Show correct controls based on mode
            if ("all".equalsIgnoreCase(currentMode)) {
                nextButton.setVisible(true);
            } else {
                ratingPanel.setVisible(true);
                intervalPanel.setVisible(true);
            }
        }
    }

    private void applyRating(String rating) {
        try {
            studyController.applyRating(rating, currentCard.getId());
            studyController.nextCardAndNotify();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while rating: " + e.getMessage());
        }
    }
    private void updateIntervals() {
        intervalPanel.removeAll();

        if (currentCard != null) {
            intervalPanel.add(new JLabel(String.valueOf(deckController.showEstimatedDate("again", currentCard.getId())) + "d"));
            intervalPanel.add(new JLabel(String.valueOf(deckController.showEstimatedDate("hard", currentCard.getId())) + "d"));
            intervalPanel.add(new JLabel(String.valueOf(deckController.showEstimatedDate("medium", currentCard.getId())) + "d"));
            intervalPanel.add(new JLabel(String.valueOf(deckController.showEstimatedDate("easy", currentCard.getId())) + "d"));
            intervalPanel.revalidate();
            intervalPanel.repaint();
        }
    }
    // Called when sessionFinishedObservable fires
    private void handleSessionFinished() {
        JOptionPane.showMessageDialog(this, "The session is over!");
        mainFrame.navigateTo("Home");
    }
}
