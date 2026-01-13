package org.flashcard.view;

import org.flashcard.application.dto.TagDTO;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TagDropdown extends JPanel {

    private JComboBox<Object> combo;

    public TagDropdown() {
        setLayout(new BorderLayout());
        setOpaque(false);

        combo = new JComboBox<>();
        combo.setPreferredSize(new Dimension(150, 34));
        combo.addItem("All Tags");

        add(combo, BorderLayout.CENTER);
    }

    public void loadTags(List<TagDTO> tags) {
        combo.removeAllItems();
        combo.addItem("All Tags");
        if (tags != null) {
            for (TagDTO t : tags) combo.addItem(t);
        }
    }

    public Integer getSelectedTagId() {
        Object sel = combo.getSelectedItem();
        if (sel instanceof TagDTO t) {
            return t.getId();
        }
        return null;
    }

    public JComboBox<Object> getComboBox() {
        return combo;
    }
}
