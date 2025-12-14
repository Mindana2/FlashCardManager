package org.flashcard.testview;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.dto.TagDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.Duration;


public class DeckCard extends JPanel {
    JLabel infoLabel;
    JButton studyButton = new JButton("Start");
    DeckDTO deck;
    Timer countdownTimer;

    private JButton studyButton;

    public DeckCard(DeckDTO deck, ActionListener onStudyClick, Duration timeLeft) {

        this.deck = deck;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        setPreferredSize(new Dimension(220, 192));

        countdownTimer = new Timer(0, e -> updateCountdown());

        // --- Top Panel (Tag + Titel + Progress) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // --- Tag i vänstra hörnet ---
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

        // --- Titel centrerad ---
        JLabel titleLabel = new JLabel(deck.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // --- Progress i högra hörnet ---
        double progressPercent = (deck.getProgressPercent() != 0) ? deck.getProgressPercent() : 0;

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int)progressPercent);
        progressBar.setForeground(new Color(50, 180, 50));
        progressBar.setBackground(new Color(220, 220, 220));
        progressBar.setPreferredSize(new Dimension(60, 10));
        progressBar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        progressBar.setStringPainted(false);

        JLabel progressLabel = new JLabel((int)progressPercent + "%");
        progressLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        progressLabel.setForeground(new Color(50, 180, 50));
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(progressLabel, BorderLayout.SOUTH);

        topPanel.add(progressPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // --- Info (Due Count or Next review) ---

        infoLabel = new JLabel("Total Cards: " + deck.getDueCount());
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // --- Action Button ---
        studyButton = new JButton("Start");
        studyButton.setBackground(new Color(60, 120, 240));
        studyButton.setForeground(Color.WHITE);
        studyButton.setFocusPainted(false);
        studyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Lägg till ActionListener om den inte är null
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

    // Ny konstruktor för inaktiverat kort med nedräkningstext
    public DeckCard(
            DeckDTO deck,
            ActionListener onStudyClick,
            boolean disabled,
            String countdownText
    ) {
        this(deck, onStudyClick); // återanvänd befintlig konstruktor

        if (disabled) {
            setBackground(new Color(103, 97, 97));

            studyButton.setEnabled(false);
            studyButton.setBackground(new Color(103, 97, 97));
            studyButton.setForeground(Color.DARK_GRAY);
            studyButton.setCursor(Cursor.getDefaultCursor());
            studyButton.setText(" ");

            JLabel countdownLabel = new JLabel(countdownText, SwingConstants.CENTER);
            countdownLabel.setFont(new Font("SansSerif", Font.ITALIC, 14)); // ⬅ större font
            countdownLabel.setForeground(new Color(255, 255, 255));          // ⬅ tydligare färg


            add(countdownLabel, BorderLayout.CENTER);
        }

    }

    private void updateCountdown(){

    }
}
