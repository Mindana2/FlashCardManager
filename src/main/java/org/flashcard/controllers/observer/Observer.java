package org.flashcard.controllers.observer;

/**
 * Four view classes implement this interface.
 * Defines a generic contract for view components to receive automatic updates
 * from the controller layer whenever the underlying data changes.
 */

public interface Observer<T> {
    void notify(T data);
}
