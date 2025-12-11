package org.flashcard.controllers.observer;

import java.util.ArrayList;
import java.util.List;

// En klass som hanterar en lista av observers (listeners)
// och notifierar dem när en händelse inträffar

public class Observable<T> {

    private final List<Observer<T>> listeners = new ArrayList<>();

    // Metod för att lägga till en listener
    public void addListener(Observer<T> l) {
        listeners.add(l);
    }

    public void removeListener(Observer<T> l) {
        listeners.remove(l);
    }

    // Metod för att notifiera alla listeners om en händelse
    public void notifyListeners(T data) {
        for (Observer<T> listener : listeners) {
            listener.notify(data);
        }
    }
}
