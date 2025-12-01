package view;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JPanel {
    protected JPanel headerPanel;
    protected JLabel titleLabel;

    private JPanel decksPanel;
    private JScrollPane scrollPane;

    public HomeView() {
        setOpaque(true);
        setBackground(Theme.BG);

        initComponents();
        layoutComponents();
        styleComponents();
    }

    private void initComponents() {
        titleLabel = new JLabel("To be Rehearsed today");

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBackground(Theme.BG);

        decksPanel = new JPanel();
        decksPanel.setLayout(new GridLayout(0, 3, 20, 20));
        decksPanel.setOpaque(true);
        decksPanel.setBackground(Theme.BG);

        JPanel decksWrapper = new JPanel(new BorderLayout());
        decksWrapper.setOpaque(true);
        decksWrapper.setBackground(Theme.BG);
        decksWrapper.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        decksWrapper.add(decksPanel, BorderLayout.CENTER);

        scrollPane = new JScrollPane(decksWrapper);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(Theme.BG);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(Theme.BG);
    }


    private void layoutComponents() {
        setLayout(new BorderLayout());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleComponents() {
        titleLabel.setFont(Theme.TITLE);
        titleLabel.setForeground(Theme.TEXT);
    }

    public void setDecks(java.util.List<JPanel> deckCards) {
        decksPanel.removeAll();
        for (JPanel card : deckCards) {
            decksPanel.add(card);
        }
        decksPanel.revalidate();
        decksPanel.repaint();
    }
}
