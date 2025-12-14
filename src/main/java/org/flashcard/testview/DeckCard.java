package org.flashcard.testview;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.controllers.DeckController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.Duration;

public class DeckCard extends JPanel {

    public enum DeckCardContext {
        HOME_VIEW,
        MY_DECKS_VIEW
    }

    JLabel infoLabel;
    JButton studyButton = new JButton("Start");
    DeckDTO deck;
    Timer countdownTimer;
    Duration timeLeft;
    JLabel countdownLabel = new JLabel();
    String countdownText;
    DeckController deckController;

    // --- Standard konstruktor ---
    public DeckCard(DeckDTO deck, DeckCardContext context, ActionListener onStudyClick) {
        this.deck = deck;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        setPreferredSize(new Dimension(220, 192));

        // --- Top Panel (Tag + Title + Progress) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Tag
        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        TagDTO tagDTO = deck.getTagDTO();
        tagPanel.setOpaque(false);
        if (tagDTO != null) {
            JLabel tagLabel = new JLabel(tagDTO.getTitle());
            tagLabel.setOpaque(true);
            tagLabel.setBackground(tagDTO.getColor());
            tagLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            tagLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            tagPanel.add(tagLabel);
        }
        topPanel.add(tagPanel, BorderLayout.WEST);

        // Title
        JLabel titleLabel = new JLabel(deck.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Progress
        double progressPercent = (deck.getProgressPercent() != 0) ? deck.getProgressPercent() : 0;
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) progressPercent);
        progressBar.setForeground(new Color(50, 180, 50));
        progressBar.setBackground(new Color(220, 220, 220));
        progressBar.setPreferredSize(new Dimension(60, 10));
        progressBar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        progressBar.setStringPainted(false);

        JLabel progressLabel = new JLabel((int) progressPercent + "%");
        progressLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        progressLabel.setForeground(new Color(50, 180, 50));
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(progressLabel, BorderLayout.SOUTH);

        topPanel.add(progressPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- Info Label (beroende på kontext) ---
        if (context == DeckCardContext.HOME_VIEW) {
            infoLabel = new JLabel("Cards due: " + deck.getDueCount());
        } else { // MY_DECKS_VIEW
            infoLabel = new JLabel("Total cards: " + deck.getCardCount());
        }
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // --- Action Button ---
        studyButton = new JButton("Start");
        studyButton.setBackground(new Color(60, 120, 240));
        studyButton.setForeground(Color.WHITE);
        studyButton.setFocusPainted(false);
        studyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (onStudyClick != null) {
            studyButton.addActionListener(onStudyClick);
        }

        // --- Center panel ---
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(infoLabel);

        add(centerPanel, BorderLayout.CENTER);
        add(studyButton, BorderLayout.SOUTH);
    }

    // --- Konstruktor med countdown och disabled state ---
    public DeckCard(
            DeckDTO deck,
            ActionListener onStudyClick,
            boolean disabled,
            String countdownText,
            Duration timeLeft,
            DeckController deckController
    ) {
        this.deck = deck;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        setPreferredSize(new Dimension(220, 192));

        this.timeLeft = timeLeft;
        this.deck = deck;
        this.countdownText = countdownText;
        this.deckController = deckController;

        // --- Top Panel (Tag + Title + Progress) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        TagDTO tagDTO = deck.getTagDTO();
        tagPanel.setOpaque(false);
        if (tagDTO != null) {
            JLabel tagLabel = new JLabel(tagDTO.getTitle());
            tagLabel.setOpaque(true);
            tagLabel.setBackground(tagDTO.getColor());
            tagLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            tagLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            tagPanel.add(tagLabel);
        }
        topPanel.add(tagPanel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel(deck.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        double progressPercent = (deck.getProgressPercent() != 0) ? deck.getProgressPercent() : 0;
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) progressPercent);
        progressBar.setForeground(new Color(50, 180, 50));
        progressBar.setBackground(new Color(220, 220, 220));
        progressBar.setPreferredSize(new Dimension(60, 10));
        progressBar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        progressBar.setStringPainted(false);

        JLabel progressLabel = new JLabel((int) progressPercent + "%");
        progressLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        progressLabel.setForeground(new Color(50, 180, 50));
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(progressLabel, BorderLayout.SOUTH);

        topPanel.add(progressPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- Info Label (default till total cards) ---
        infoLabel = new JLabel("Total cards: " + deck.getCardCount());
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // --- Action Button ---
        studyButton = new JButton("Start");
        studyButton.setBackground(new Color(60, 120, 240));
        studyButton.setForeground(Color.WHITE);
        studyButton.setFocusPainted(false);
        studyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (onStudyClick != null) {
            studyButton.addActionListener(onStudyClick);
        }

        // --- Center panel ---
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(infoLabel);

        add(centerPanel, BorderLayout.CENTER);
        add(studyButton, BorderLayout.SOUTH);

        // --- Countdown om disabled ---
        this.countdownTimer = new Timer(1000, e -> updateCountdown());
        this.countdownTimer.start();

        if (disabled) {
            setBackground(new Color(103, 97, 97));
            studyButton.setEnabled(false);
            studyButton.setBackground(new Color(103, 97, 97));
            studyButton.setForeground(Color.DARK_GRAY);
            studyButton.setCursor(Cursor.getDefaultCursor());
            studyButton.setText(" ");

            countdownLabel = new JLabel(countdownText, SwingConstants.CENTER);
            countdownLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            countdownLabel.setForeground(new Color(255, 255, 255));

            add(countdownLabel, BorderLayout.CENTER);
        }
    }

    private void updateCountdown() {
        timeLeft = deckController.timeUntilDue(deck.getId());
        if (timeLeft.isNegative() || timeLeft.isZero()) {
            countdownLabel.setText(countdownText + "00:00:00");
            countdownTimer.stop();
        } else {
            long hours = timeLeft.toHours();
            long minutes = timeLeft.toMinutesPart();
            long seconds = timeLeft.toSecondsPart();
            countdownLabel.setText(countdownText + hours + ":" + minutes + ":" + seconds);
        }
    }
}
