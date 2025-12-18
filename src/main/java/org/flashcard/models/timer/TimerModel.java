package org.flashcard.models.timer;
import javax.swing.Timer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 Acts as the centralized scheduling engine for the application, managing high-precision
 countdowns for multiple flashcard decks and notifying observers when study intervals are reached.
 */

public class TimerModel {



    private final Map<CountdownListener, LocalDateTime> listenersPerDeck = new HashMap<>();
    private final Timer timer;



    public TimerModel() {
        this.timer = new Timer(1000, (e -> updateTimer()));
        timer.start();
    }

    public void addTimerListener(CountdownListener listener, LocalDateTime nextReviewDate) {
        listenersPerDeck.putIfAbsent(listener, nextReviewDate);
    }
    public void removeTimerListener(CountdownListener listener){
        listenersPerDeck.remove(listener);
    }

    public void updateTimer() {
        Iterator<Map.Entry<CountdownListener, LocalDateTime>> iterator = listenersPerDeck.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<CountdownListener, LocalDateTime> entry = iterator.next();
            CountdownListener listener = entry.getKey();
            LocalDateTime nextReviewDate = entry.getValue();

            Duration timeLeft = Duration.between(LocalDateTime.now(), nextReviewDate);

            if (timeLeft.isZero() || timeLeft.isNegative()) {
                // Remove listener
                iterator.remove();
                //Notify that countdown ended
                listener.onFinished();
            } else {
                listener.onTick(format(timeLeft));
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
