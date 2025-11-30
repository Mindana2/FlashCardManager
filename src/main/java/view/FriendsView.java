package view;

import javax.swing.*;
import java.awt.*;

public class FriendsView extends JPanel {
    private JButton toggleButton;  // pil
    private JPanel headerPanel;
    private JPanel listPanel; // lista för vänner
    private JScrollPane scrollPane;
    private boolean isOpen = true;

    // Konstruktor
    public FriendsView() {
        initComponents();
        layoutComponents();
        styleComponents();
        addListeners();
    }

    private void initComponents(){
        toggleButton = new JButton("⮜");
        toggleButton.setFocusPainted(false);
        toggleButton.setBorder(null);

        // Header panel där pilknappen finns
        headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        headerPanel.setOpaque(false);
        headerPanel.add(toggleButton);

        // Container för vänner
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        // Scroll pane för vänner
        scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

    }

    // Add Listeners för att öppna/stänga friendspanelen
    // i FriendsView (klassnivå)
    public void addListeners() {
        toggleButton.addActionListener(e -> {
            if (isOpen) close(); else open();
        });
    }

    // publika metoder för att öppna/stänga (lägg i FriendsView)
    public void open() {
        isOpen = true;
        toggleButton.setText("⮜");       // pil pekar in
        listPanel.setVisible(true);
        revalidate();
        repaint();
    }

    public void close() {
        isOpen = false;
        toggleButton.setText("⮞");       // pil pekar ut
        listPanel.setVisible(false);
        revalidate();
        repaint();
    }

    public boolean isOpen() { return isOpen; }


    // Style components
    public void styleComponents() {
        setBackground(Theme.BG);
        toggleButton.setBackground(Theme.BG);
        toggleButton.setForeground(Theme.TEXT);
    }



}
