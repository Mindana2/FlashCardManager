package org.flashcard.controllers.observer;

// fyra view klasser implementerar denna interface
// för att kunna ta emot notifikationer från controllers när data ändras

public interface Observer<T> {
    void notify(T data);
}
