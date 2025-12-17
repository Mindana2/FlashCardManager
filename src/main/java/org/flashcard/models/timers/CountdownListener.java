package org.flashcard.models.timers;


import org.flashcard.application.dto.DeckDTO;

public interface CountdownListener {
    void onTick(String countdown);
    void onFinished();
}

