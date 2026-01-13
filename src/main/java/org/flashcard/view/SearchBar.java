package org.flashcard.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SearchBar extends JPanel {

    private final JTextField field;
    private final String placeholder;

    public SearchBar(String placeholder, int width) {
        this.placeholder = placeholder;

        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(width, 42));

        JLabel icon = new JLabel("     🔍  ");
        icon.setForeground(Color.DARK_GRAY);

        field = new JTextField();
        field.setOpaque(false);
        field.setBorder(null);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Ta bort placeholder först när användaren verkligen vill skriva
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });

        add(icon, BorderLayout.WEST);
        add(field, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = getHeight() - 8;
        int padding = 4;

        int w = Math.max(0, getWidth() - padding * 2);
        int h = Math.max(0, getHeight() - padding * 2);

        g2.setColor(new Color(235, 225, 235));
        g2.fillRoundRect(padding, padding, w, h, arc, arc);

        g2.setColor(new Color(150, 150, 150));
        g2.drawRoundRect(padding, padding, w, h, arc, arc);
    }

    public String getText() {
        return field.getText();
    }

    public JTextField getField() {
        return field;
    }


    public String getEffectiveText() {
        String t = field.getText();
        if (t == null) return null;
        t = t.trim();
        if (t.isEmpty()) return null;
        if (t.equals(placeholder)) return null;
        return t;
    }
}
