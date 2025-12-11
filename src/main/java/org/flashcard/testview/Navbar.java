package org.flashcard.testview;


import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class Navbar extends JPanel {

    private final Consumer<String> navigationCallback;

    public Navbar(Consumer<String> navigationCallback) {
        this.navigationCallback = navigationCallback;

        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        setBackground(new Color(50, 50, 50)); // Mörkgrå

        add(createNavButton("Home", "Home"));
        add(createNavButton("My Decks", "MyDecks"));
    }

    private JButton createNavButton(String label, String viewName) {
        JButton btn = new JButton(label);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 70, 70));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.addActionListener(e -> navigationCallback.accept(viewName));
        return btn;
    }
}
