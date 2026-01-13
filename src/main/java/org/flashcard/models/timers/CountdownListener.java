package org.flashcard.models.timers;



public interface CountdownListener {
    void onTick(String countdown);
    void onFinished();
}

