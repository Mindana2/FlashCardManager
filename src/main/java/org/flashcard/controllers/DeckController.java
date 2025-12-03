package org.flashcard.controllers;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.mapper.DeckMapper;
import org.flashcard.application.mapper.FlashcardMapper;
import org.flashcard.models.dataclasses.*;
import org.flashcard.repositories.DeckRepository;
import org.flashcard.repositories.FlashcardRepository;
import org.flashcard.repositories.TagRepository;
import org.flashcard.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeckController {

    private final DeckRepository deckRepo;
    private final FlashcardRepository flashcardRepo;
    private final UserRepository userRepo;
    private final TagRepository tagRepo;

    public DeckController(DeckRepository deckRepo,
                       FlashcardRepository flashcardRepo,
                       UserRepository userRepo,
                       TagRepository tagRepo) {
        this.deckRepo = deckRepo;
        this.flashcardRepo = flashcardRepo;
        this.userRepo = userRepo;
        this.tagRepo = tagRepo;
    }

    // --- Deck CRUD ---
    public DeckDTO createDeck(Integer userId, String title, Integer tagId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Tag tag = null;
        if (tagId != null) {
            tag = tagRepo.findById(tagId)
                    .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
        }

        Deck deck = new Deck(title, user);
        deck.setTag(tag);

        Deck savedDeck = deckRepo.save(deck);
        return DeckMapper.toDTO(savedDeck);
    }

    // Används av MyDecksView
    public List<DeckDTO> getAllDecksForUser(Integer userId) {
        List<Deck> userDecks = deckRepo.findByUserId(userId);

        // Här bryr vi oss kanske inte om 'dueCount', så vi skickar 0 eller räknar ut det om du vill visa det ändå.
        // Men viktigast är att vi INTE filtrerar bort några lekar.
        return userDecks.stream()
                .map(DeckMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DeckDTO getDeckById(Integer deckId) {
        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        return DeckMapper.toDTO(deck);
    }

    // Used by homeview to get all decks that are due today
    public List<DeckDTO> getDueDecksForUser(Integer userId) {
        List<Deck> userDecks = deckRepo.findByUserId(userId);

        return userDecks.stream()
                .map(deck -> {
                    long dueCount = 0;
                    if (deck.getCards() != null) {
                        dueCount = deck.getCards().stream()
                                .filter(this::isCardDue) // Din isDue-logik
                                .count();
                    }
                    return DeckMapper.toDTO(deck, (int) dueCount);
                })
                .filter(dto -> dto.getDueCount() > 0)
                .collect(Collectors.toList());
    }

    // Helper method
    private boolean isCardDue(Flashcard card) {
        CardLearningState state = card.getCardLearningState();
        return state == null || state.isDueToday();
    }

    public DeckDTO updateDeck(Integer deckId, String newTitle, Integer newTagId) {
        // 1. Hämta Entiteten
        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        if (newTitle != null && !newTitle.isBlank()) {
            deck.setTitle(newTitle);
        }

        if (newTagId != null) {
            Tag tag = tagRepo.findById(newTagId)
                    .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
            deck.setTag(tag);
        } else {
            deck.setTag(null);  //If newTagID is null it is removed
        }

        Deck savedDeck = deckRepo.save(deck);
        return DeckMapper.toDTO(savedDeck);
    }

    public void deleteDeck(Integer deckId) {
        if (!deckRepo.existsById(deckId)) {
            throw new IllegalArgumentException("Deck not found");
        }
        deckRepo.deleteById(deckId);
    }

    // --- Flashcard CRUD ---

    public FlashcardDTO addFlashcard(Integer deckId, String front, String back) {

        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        Flashcard card = new Flashcard(front, back, deck);

        card.setCardLearningState(new CardLearningState(card));
        Flashcard savedCard = flashcardRepo.save(card);

        return FlashcardMapper.toDTO(savedCard);    //Convert to DTO
    }

    public List<FlashcardDTO> getFlashcardsForDeck(Integer deckId) {
        List<Flashcard> cards = flashcardRepo.findByDeckId(deckId);

        // Changes from Flashcard to FlashcardDTO for every flashcard
        return cards.stream()
                .map(FlashcardMapper::toDTO)
                .collect(Collectors.toList());
    }
    public FlashcardDTO updateFlashcard(Integer cardId, String newFront, String newBack) {

        Flashcard card = flashcardRepo.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard not found"));


        if (newFront != null && !newFront.isBlank()) card.setFront(newFront);
        if (newBack != null && !newBack.isBlank()) card.setBack(newBack);


        Flashcard savedCard = flashcardRepo.save(card); //Save to database

        return FlashcardMapper.toDTO(savedCard);       //Convert to DTO and return
    }

    public void deleteFlashcard(Integer cardId) {
        if (!flashcardRepo.existsById(cardId)) {
            throw new IllegalArgumentException("Flashcard not found");
        }
        flashcardRepo.deleteById(cardId);
    }
}
