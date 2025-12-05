package org.flashcard.controllers;
import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.application.mapper.DeckMapper;
import org.flashcard.application.mapper.FlashcardMapper;
import org.flashcard.application.mapper.TagMapper;
import org.flashcard.models.dataclasses.*;
import org.flashcard.repositories.DeckRepository;
import org.flashcard.repositories.FlashcardRepository;
import org.flashcard.repositories.TagRepository;
import org.flashcard.repositories.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/* We use Spring Data JPA to access the database.
 *
 * This class is annotated with @Service, which tells Spring
 * that it is a service-layer component.
 *
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 *
 * The @Transactional annotation ensures that no database transactions are left unfinished.
 * It automatically aborts any transactions that result in an error.
 * This allows us to write logic without manually handling database transactions.
 */
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


    // ---Deck CRUD---
    public DeckDTO createDeck(Integer userId, String title) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Deck deck = new Deck(title, user);
        Deck savedDeck = deckRepo.save(deck);

        return DeckMapper.toDTO(savedDeck);
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

    // ---Deck-related Getters---
    public List<DeckDTO> getAllDecksForUser(Integer userId) {
        List<Deck> userDecks = deckRepo.findByUserId(userId);

        return userDecks.stream()
                .map(deck -> {
                    // Räkna antal kort via repository istället för deck.getCards()
                    long cardCount = flashcardRepo.countByDeckId(deck.getId());
                    return DeckMapper.toDTO(deck, (int) cardCount);
                })
                .collect(Collectors.toList());
    }
    public DeckDTO getDeckById(Integer deckId) {
        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        return DeckMapper.toDTO(deck);
    }
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
    public boolean deckExists(Integer userId, String title) {
        return deckRepo.existsByUserIdAndTitle(userId, title);
    }

    // Helper method
    private boolean isCardDue(Flashcard card) {
        CardLearningState state = card.getCardLearningState();
        return state == null || state.isDueToday();
    }


    // ---Tag-related---
    public TagDTO createTag(Integer userId, String title, String color) {

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag title cannot be empty");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        Tag tag = new Tag(title.trim(), color, user);
        Tag savedTag = tagRepo.save(tag);
        return TagMapper.toDTO(savedTag);
    }
    public TagDTO assignTagToDeck(Integer deckId, Integer tagId) {
        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        Tag tag = tagRepo.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));

        deck.setTag(tag);
        return TagMapper.toDTO(tag);
    }


    // ---Flashcard CRUD---
    public FlashcardDTO addFlashcard(Integer deckId, String front, String back) {

        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        Flashcard card = new Flashcard(front, back, deck);
        Flashcard savedCard = flashcardRepo.save(card); // trigger skapar CardLearningState automatiskt

        return FlashcardMapper.toDTO(savedCard);
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
