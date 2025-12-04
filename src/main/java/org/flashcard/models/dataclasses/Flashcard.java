package org.flashcard.models.dataclasses;

import jakarta.persistence.*;
import org.flashcard.controllers.UserController;

import java.time.LocalDate;

@Entity
@Table(name = "Flashcards",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"deckId", "front"})}) // prevent duplicate fronts in a deck
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String front;

    @Column(nullable = false, length = 100)
    private String back;

    @Column(nullable = false)
    private LocalDate dateCreated = LocalDate.now();

    // Many flashcards belong to one deck
    @ManyToOne
    @JoinColumn(name = "deckId", nullable = false)
    private Deck deck;

    // One-to-one relationship with CardLearningState
    @OneToOne(mappedBy = "flashcard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CardLearningState cardLearningState;

    // Constructors
    public Flashcard() {}


    public Flashcard(String front, String back, Deck deck) {
        if (front == null || front.isBlank()) throw new IllegalArgumentException("Front text required");
        if (back == null || back.isBlank()) throw new IllegalArgumentException("Back text required");

        this.front = front;
        this.back = back;
        this.deck = deck;
        this.dateCreated = LocalDate.now();
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFront() { return front; }
    public void setFront(String front) { this.front = front; }

    public String getBack() { return back; }
    public void setBack(String back) { this.back = back; }

    public LocalDate getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDate dateCreated) { this.dateCreated = dateCreated; }

    public Deck getDeck() { return deck; }
    public void setDeck(Deck deck) { this.deck = deck; }

    public CardLearningState getCardLearningState() { return cardLearningState; }
    public void setCardLearningState(CardLearningState cardLearningState) { this.cardLearningState = cardLearningState; }

    @Override
    public String toString() {
        return "Flashcard{id=" + id + ", front='" + front + "', back='" + back + "', deck=" + deck.getId() + "}";
    }

}
