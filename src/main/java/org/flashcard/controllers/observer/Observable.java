package org.flashcard.controllers.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that manages a list of observers (listeners),
 * notifying them when an event occurs
 */

public class Observable<T> {

    private final List<Observer<T>> listeners = new ArrayList<>();

    // Metod för att lägga till en listener
    public void addListener(Observer<T> l) {
        listeners.add(l);
    }


    // Metod för att notifiera alla listeners om en händelse
    public void notifyListeners(T data) {
        for (Observer<T> listener : listeners) {
            listener.notify(data);
        }
    }
}
