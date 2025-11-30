package org.Flashcard.models;

import java.util.ArrayList;

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
