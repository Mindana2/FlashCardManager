package org.flashcard.models.services;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.FlashcardDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.application.mapper.DeckMapper;
import org.flashcard.application.mapper.FlashcardMapper;
import org.flashcard.application.mapper.TagMapper;
import org.flashcard.controllers.observer.Observable;
import org.flashcard.models.dataclasses.*;
import org.flashcard.models.progress.DeckProgression;
import org.flashcard.repositories.DeckRepository;
import org.flashcard.repositories.FlashcardRepository;
import org.flashcard.repositories.TagRepository;
import org.flashcard.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/* We use Spring Data JPA to access the database.
 * This class is annotated with @Service, which tells Spring
 * that it is a service-layer component.
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 * The @Transactional annotation ensures that no database transactions are left unfinished.
 * It automatically aborts any transactions that result in an error.
 * This allows us to write logic without manually handling database transactions.
 */
@Service
@Transactional
public class DeckService {
    private final Observable<List<DeckDTO>> decksObservable = new Observable<>();
    private final Observable<List<FlashcardDTO>> flashcardsObservable = new Observable<>();
    private final DeckRepository deckRepo;
    private final FlashcardRepository flashcardRepo;
    private final UserRepository userRepo;
    private final TagRepository tagRepo;

    public DeckService(FlashcardRepository flashcardRepo,
                       DeckRepository deckRepo,
                       UserRepository userRepo,
                       TagRepository tagRepo) {
        this.deckRepo = deckRepo;
        this.flashcardRepo = flashcardRepo;
        this.userRepo = userRepo;
        this.tagRepo = tagRepo;

    }
    public Observable<List<DeckDTO>> getDecksObservable() {
        return decksObservable;
    }


    public DeckDTO createDeck(Integer userId, String title) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Deck deck = new Deck(title, user);
        Deck savedDeck = deckRepo.save(deck);

        DeckDTO dto = DeckMapper.toDTO(savedDeck);

        decksObservable.notifyListeners(getAllDecksForUser(userId));

        return dto;
    }

    public void resetDeckProgression(Integer deckId) {
        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        List<Flashcard> cards = deck.getCards();

        cards.forEach(card -> {
            if (card.getCardLearningState() != null) {
                card.getCardLearningState().updateDates(0);
                flashcardRepo.save(card);
            }
        });
    }


    public TagDTO assignTagToDeck(Integer deckId, Integer tagId) {
        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        Tag tag = tagRepo.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));

        deck.setTag(tag);


        decksObservable.notifyListeners(getAllDecksForUser(deck.getUser().getId()));


        return TagMapper.toDTO(tag);
    }

    public List<DeckDTO> getAllDecksForUser(Integer userId) {
        List<Deck> userDecks = deckRepo.findByUserIdWithTag(userId);

        return userDecks.stream()
                .map(deck -> {
                    List<Flashcard> cards = flashcardRepo.findByDeck(deck);
                    deck.setCards(cards);

                    double progress = DeckProgression.calculateDeckProgression(deck);
                    deck.setDeckProgress(new DeckProgress(progress));

                    long dueCount = cards.stream()
                            .filter(this::isCardDue)
                            .count();

                    DeckDTO dto = DeckMapper.toDTO(deck, (int) dueCount);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public DeckDTO getDeckById(Integer deckId) {
        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));
        return DeckMapper.toDTO(deck);
    }

    public List<DeckDTO> getNotDueDecksForUser(Integer userId) {
        List<Deck> userDecks = deckRepo.findByUserId(userId);

        return userDecks.stream()
                .map(deck -> {
                    // Uppdatera deck progress dynamiskt
                    double progressPercent = DeckProgression.calculateDeckProgression(deck);
                    deck.setDeckProgress(new DeckProgress(progressPercent));

                    long dueCount = 0;
                    if (deck.getCards() != null) {
                        dueCount = deck.getCards().stream()
                                .filter(this::isCardDue)
                                .count();
                    }

                    DeckDTO dto = DeckMapper.toDTO(deck, (int) dueCount); // nu innehåller deckProgress
                    return dto;
                })
                .filter(dto -> dto.getDueCount() == 0)
                .collect(Collectors.toList());
    }


    private boolean isCardDue(Flashcard card) {
        CardLearningState state = card.getCardLearningState();
        return state == null || state.isDueToday(LocalDateTime.now());
    }


    public void deleteDeck(Integer deckId) {

        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        Integer userId = deck.getUser().getId();

        deckRepo.deleteById(deckId);


        decksObservable.notifyListeners(getAllDecksForUser(userId));

    }



    public boolean deckExists(Integer userId, String title) {
        return deckRepo.existsByUserIdAndTitle(userId, title);
    }

    public List<FlashcardDTO> getFlashcardsForDeck(Integer deckId) {
        List<Flashcard> cards = flashcardRepo.findByDeckId(deckId);
        return cards.stream().map(FlashcardMapper::toDTO).collect(Collectors.toList());
    }


    private Flashcard getNextReviewableCard(int deckID){
        Deck deck = deckRepo.findById(deckID)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        return deck.getCards().stream()
                .filter(card -> card.getCardLearningState() != null &&
                        card.getCardLearningState().getNextReviewDate().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(card -> card.getCardLearningState().getNextReviewDate()))
                .orElse(null);
    }

    public Duration timeUntilDue(int deckID) {
        Flashcard flashcard = getNextReviewableCard(deckID);
        if(flashcard == null){
            return Duration.ZERO;
        }
        return Duration.between(LocalDateTime.now(), flashcard.getCardLearningState().getNextReviewDate());
    }

}