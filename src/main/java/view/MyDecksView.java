package view;

import javax.swing.*;
import java.awt.*;

public class MyDecksView extends HomeView {

    private JButton editButton;

    public MyDecksView() {
        super();
        editButton = new JButton("Edit Decks");
        addExtras();
        styleExtras();
    }

    private void addExtras() {

        // Ändra titel
        titleLabel.setText("My Decks");

        // Skapa panel för knappen
        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editPanel.setOpaque(false);
        editPanel.add(editButton);

        // 🔥 GÖR HEADER PANELEN LJUST (Figma-stil)
        headerPanel.setOpaque(true);
        headerPanel.setBackground(Color.WHITE);  // ← LJUS BAKGRUND
        headerPanel.setLayout(new BorderLayout(10, 0));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(editPanel, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    private void styleExtras() {
        editButton.setBackground(new Color(230, 230, 230));
        editButton.setForeground(Color.BLACK);
        editButton.setFocusPainted(false);
    }
}
