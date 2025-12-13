package org.flashcard.models.timers;




import org.flashcard.models.dataclasses.CardLearningState;
import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ReviewCountdownTimer {
    private final Timer timer;
    private String countdown;

    private TimerListener listener;
    private final List<TimerListener> listeners = new ArrayList<>();
    private Duration timeLeft;
    public ReviewCountdownTimer() {
        this.countdown = "";
        timer = new Timer();

    }
    public void startCountdown(Flashcard flashcard) {
        CardLearningState state = flashcard.getCardLearningState();
        LocalDateTime nextReviewDate = state.getNextReviewDate();


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeLeft = Duration.between(LocalDateTime.now(), nextReviewDate);

                if (timeLeft.isZero() || timeLeft.isNegative()){
                    timeLeft = Duration.ZERO;
                    countdown = "";
                    notifyListeners(timeLeft);
                    timer.cancel();
                }
                notifyListeners(timeLeft);



            }
        };
        timer.schedule(task, 0, 1000);

    }

    public void addTimerListener(TimerListener listener){
        listeners.add(listener);
    }
    public Duration showCountdown(Flashcard flashcard){
        return timeLeft;
    }
    public void notifyListeners(Duration timeLeft){

        long hours = timeLeft.toHours();
        long minutes = timeLeft.toMinutes();
        long seconds = timeLeft.toSeconds();
        String countdown = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        if (timeLeft.isZero()) ;
        for (TimerListener listener : listeners){
            listener.updateTime(countdown);
        }
    }

}
