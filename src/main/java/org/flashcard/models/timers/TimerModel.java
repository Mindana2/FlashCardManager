package org.flashcard.models.timers;




import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Flashcard;
import org.flashcard.models.services.DeckService;
import org.flashcard.repositories.DeckRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimerModel {
//    private final Timer timer;

    private CountdownListener listener;
    private final List<CountdownListener> listeners = new ArrayList<>();
    private Duration timeLeft;

    public TimerModel() {



    }

    public void addTimerListener(CountdownListener listener){
        listeners.add(listener);
    }
    public List<CountdownListener> getListeners(){
        return listeners;
    }

    public void updateTimer(Flashcard flashcard, long dueCount, LocalDateTime now) {
        if(flashcard != null) this.timeLeft = Duration.between(now, flashcard.getCardLearningState().getNextReviewDate());
        String countdown;
        if (dueCount > 0 || timeLeft.isNegative() || timeLeft.isZero()) {
            this.timeLeft = Duration.ZERO;
            countdown = "00d : 00h : 00m : 00s";

        }else{

            long days = timeLeft.toDays();
            long hours = timeLeft.toHoursPart();
            long minutes = timeLeft.toMinutesPart();
            long seconds = timeLeft.toSecondsPart();
            countdown = days + "d : " + hours + "h : " + minutes + "m : " + seconds + "s";
        }
        for (CountdownListener listener : listeners) {
            listener.notify(countdown);

        }

    }

}
