package org.Flashcard.models;

public interface Observer<T> {
    void notify(T data);
}
