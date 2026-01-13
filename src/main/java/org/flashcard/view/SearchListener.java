package org.flashcard.view;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchListener implements DocumentListener {

    private final Runnable callback;

    public SearchListener(Runnable callback) {
        this.callback = callback;
    }

    private void changed() {
        callback.run();
    }

    @Override public void insertUpdate(DocumentEvent e) { changed(); }
    @Override public void removeUpdate(DocumentEvent e) { changed(); }
    @Override public void changedUpdate(DocumentEvent e) { changed(); }
}
