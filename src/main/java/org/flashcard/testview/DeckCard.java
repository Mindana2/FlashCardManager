package org.flashcard.testview;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.TagDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DeckCard extends JPanel {

    public DeckCard(DeckDTO deck, ActionListener onStudyClick) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        setPreferredSize(new Dimension(220, 150));

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

        // --- Info (Due Count) ---
        JLabel infoLabel = new JLabel("Total Cards: " + deck.getDueCount());
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // --- Action Button ---
        JButton studyButton = new JButton("Start");
        studyButton.setBackground(new Color(60, 120, 240));
        studyButton.setForeground(Color.WHITE);
        studyButton.setFocusPainted(false);
        studyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        studyButton.addActionListener(onStudyClick);

        // --- Center panel ---
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        centerPanel.add(infoLabel);

        add(centerPanel, BorderLayout.CENTER);
        add(studyButton, BorderLayout.SOUTH);
    }
}
