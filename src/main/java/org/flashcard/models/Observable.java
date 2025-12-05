package org.flashcard.models;

import java.util.ArrayList;

//TODO Implement observer pattern for view classes.
public class Observable<T> {
    private ArrayList<Observer<T>> listeners;

    public void addListener(Observer<T> l) {
        listeners.add(l);
    }

    public void notify(T data) {
        for (Observer<T> l : listeners) {
            l.notify(data);
        }
    }
}
