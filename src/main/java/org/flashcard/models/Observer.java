package org.flashcard.models;

//TODO Implement observer pattern for view classes.
public interface Observer<T> {
    void notify(T data);
}
