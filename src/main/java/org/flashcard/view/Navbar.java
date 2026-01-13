package org.flashcard.view;

import org.flashcard.application.dto.TagDTO;
import org.flashcard.controllers.TagController;
import org.flashcard.controllers.UserController;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;


public class Navbar extends JPanel {

    private final Consumer<String> navigate;
    private final Runnable onFilterChanged; // trigger utan parameter (AppFrame läser från getters)
    private final SearchBar searchBar;
    private final TagDropdown tagDropdown;
    private final UserController userController;
    private final TagController tagController;
    private ProfileMenuButton profileMenuButton;



    public Navbar(Consumer<String> navigate,
                  Runnable onFilterChanged,
                  UserController userController,
                  TagController tagController) {

        this.navigate = navigate;
        this.onFilterChanged = onFilterChanged;
        this.userController = userController;
        this.tagController = tagController;

        setLayout(new BorderLayout(20, 10));
        setBackground(new Color(50, 50, 50));

        // LEFT MENU
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        left.setOpaque(false);

        profileMenuButton = new ProfileMenuButton(userController, this);
        left.add(profileMenuButton);


        left.add(createNavButton("Home", "Home"));
        left.add(createNavButton("My Decks", "MyDecks"));

        // CENTER search + tag
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        center.setOpaque(false);

        searchBar = new SearchBar("Search Decks...", 300);

        searchBar.getField().getDocument().addDocumentListener(new SearchListener(() -> {
            onFilterChanged.run();
        }));


        tagDropdown = new TagDropdown();
        reloadTags();
        tagDropdown.getComboBox().addActionListener(e -> onFilterChanged.run());

        center.add(searchBar);
        center.add(tagDropdown);

        add(left, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, String view) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 70, 70));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.addActionListener(e -> navigate.accept(view));
        return btn;
    }

    /** Returnerar "effektiv" söktext (null om placeholder eller tom). */
    public String getSearchText() {
        return searchBar.getEffectiveText();
    }

    /** Returnerar tag id eller null */
    public Integer getSelectedTagId() {
        return tagDropdown.getSelectedTagId();
    }

    /** Called by ProfileDropdown when user changes */
    public void onUserChanged() {
        reloadTags();
        onFilterChanged.run();

        profileMenuButton.refreshUser();
    }

    private void reloadTags() {
        Integer userId = userController.getCurrentUserId();
        List<TagDTO> tags = userId == null ? List.of() : tagController.getTagsForUser(userId);
        tagDropdown.loadTags(tags);
    }
}
