package org.flashcard.models.timers;




import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.services.DeckService;
import org.flashcard.repositories.DeckRepository;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class TimerModel {
//    private final Timer timer;

    private CountdownListener listener;
    private final Map<Integer, Duration> timeLeftPerDeck = new HashMap<>();
    private final Map<Integer, CountdownListener> listenersPerDeck = new HashMap<>();
    private Duration timeLeft;
    private String countdown;
    private boolean finishedSent;
    private final javax.swing.Timer timer;



    public TimerModel() {
        this.timer = new Timer(1000, (e -> updateTimer()));
        timer.start();
    }

    public void addTimerListener(CountdownListener listener, int deckID, LocalDateTime nextReviewDate){
        if (nextReviewDate.isAfter(LocalDateTime.now()))
            timeLeft = Duration.between(LocalDateTime.now(), nextReviewDate);
        listenersPerDeck.put(deckID, listener);
        timeLeftPerDeck.put(deckID, timeLeft);
    }
//    public List<CountdownListener> getListeners(){
//        return listeners;
//    }

    public void updateTimer() {
        for (int deckID : timeLeftPerDeck.keySet()) {
            if (timeLeftPerDeck.get(deckID).isNegative() || timeLeftPerDeck.get(deckID).isZero()) {
                this.timeLeft = Duration.ZERO;
                countdown = format(timeLeft);
                listenersPerDeck.get(deckID).onTick(countdown);
                listenersPerDeck.get(deckID).onFinished();

            } else {
                timeLeft = timeLeftPerDeck.get(deckID);
                countdown = format(timeLeft);
                listenersPerDeck.get(deckID).onTick(countdown);
            }
        }
    }






    private String format(Duration timeLeft){
        long days = timeLeft.toDays();
        long hours = timeLeft.toHoursPart();
        long minutes = timeLeft.toMinutesPart();
        long seconds = timeLeft.toSecondsPart();
        return days + "d : " + hours + "h : " + minutes + "m : " + seconds + "s";
    }

}
