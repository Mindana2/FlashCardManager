package org.flashcard.view;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A specialized listener that bridges Swing's text-change events to the application's
 * filtering logic, enabling real-time "search-as-you-type" functionality.
 */

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
