package org.flashcard.testview;


import org.flashcard.application.dto.DeckDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DeckCard extends JPanel {

    public DeckCard(DeckDTO deck, ActionListener onStudyClick) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        setPreferredSize(new Dimension(220, 150));

        // --- Header (Titel) ---
        JLabel titleLabel = new JLabel(deck.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // --- Tag (Färgkodad) ---
        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tagPanel.setOpaque(false);
        if (deck.getTagDTO() != null) {
            JLabel tagLabel = new JLabel(deck.getTagDTO().getTitle());
            tagLabel.setOpaque(true);
            try {
                String rawHex = deck.getTagDTO().getColorHex();
                if (rawHex == null) rawHex = "808080";
                String normalized = rawHex.startsWith("#") ? rawHex : ("#" + rawHex);
                Color bg = Color.decode(normalized);
                tagLabel.setBackground(bg);

                double luminance = (0.299 * bg.getRed() + 0.587 * bg.getGreen() + 0.114 * bg.getBlue()) / 255;
                tagLabel.setForeground(luminance > 0.6 ? Color.BLACK : Color.WHITE);
            } catch (Exception e) {
                tagLabel.setBackground(Color.GRAY);
                tagLabel.setForeground(Color.WHITE);
            }

            tagLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            tagLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            tagPanel.add(tagLabel);
        }

        // --- Info (Due Count) ---
        JLabel infoLabel = new JLabel(deck.getDueCount() + " kort redo");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // --- Action Button ---
        JButton studyButton = new JButton("Starta");
        studyButton.setBackground(new Color(60, 120, 240));
        studyButton.setForeground(Color.WHITE);
        studyButton.setFocusPainted(false);
        studyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        studyButton.addActionListener(onStudyClick);

        // Layout
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        centerPanel.add(titleLabel);
        centerPanel.add(tagPanel);
        centerPanel.add(infoLabel);

        add(centerPanel, BorderLayout.CENTER);
        add(studyButton, BorderLayout.SOUTH);
    }
}