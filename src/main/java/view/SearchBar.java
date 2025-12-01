package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


public class SearchBar extends JPanel {

    private final JTextField field;

    public SearchBar(String placeholder, int width) {

        // ---- PANELINSTÄLLNINGAR ----
        setLayout(new BorderLayout());
        setOpaque(false);         // låter rundade hörn synas
        setPreferredSize(new Dimension(width, 36));  // ← ÄNDRA LÄNGD HÄR

        // ---- IKON ----
        JLabel icon = new JLabel("  🔍");
        icon.setForeground(Color.BLACK);

        // ---- TEXTFÄLT ----
        field = new JTextField(placeholder);
        field.setBorder(null);                 // ingen kant
        field.setForeground(Color.WHITE);      // vit text
        field.setBackground(new Color(30, 30, 30)); // mörk bakgrund
        field.setCaretColor(Color.WHITE);      // vit skrivmarkör
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setOpaque(true);

        // Placeholder-effekt
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });

        // ---- LÄGG TILL DELAR ----
        add(icon, BorderLayout.WEST);
        add(field, BorderLayout.CENTER);

        // ---- RUNDADE HÖRN ----
        setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(37),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }


    /** GET TEXT */
    public String getText() {
        return field.getText();
    }


    /** RUNDAD KANT KLASS */
    private static class RoundBorder implements javax.swing.border.Border {
        private final int radius;

        public RoundBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        public boolean isBorderOpaque() { return false; }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(80, 80, 80));         // färg på ramen
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
        }
    }
}
