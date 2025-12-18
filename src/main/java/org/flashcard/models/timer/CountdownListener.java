package org.flashcard.models.timer;

/**
 * Listener interface for countdown timer events.
 */

public interface CountdownListener {
    void onTick(String countdown);
    void onFinished();
}

