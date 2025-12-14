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
import org.flashcard.models.progress.FlashcardProgression;
import org.flashcard.models.ratingstrategy.RatingStrategy;
import org.flashcard.models.ratingstrategy.StrategyFactory;
import org.flashcard.models.timers.ReviewCountdownTimer;
import org.flashcard.repositories.DeckRepository;
import org.flashcard.repositories.FlashcardRepository;
import org.flashcard.repositories.TagRepository;
import org.flashcard.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeckService {
    private final Observable<List<DeckDTO>> decksObservable = new Observable<>();
    private final Observable<List<FlashcardDTO>> flashcardsObservable = new Observable<>();
    private final List<ReviewCountdownTimer> timers = new ArrayList<>();
    private ReviewCountdownTimer countdownTimer;
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

    public Observable<List<FlashcardDTO>> getFlashcardsObservable() {
        return flashcardsObservable;
    }

    public DeckDTO createDeck(Integer userId, String title) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Deck deck = new Deck(title, user);
        Deck savedDeck = deckRepo.save(deck);

        DeckDTO dto = DeckMapper.toDTO(savedDeck);

        // --------------------------------------------------------------------
        // OBSERVER: notify deck list changed
        decksObservable.notifyListeners(getAllDecksForUser(userId));
        // --------------------------------------------------------------------

        return dto;
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

    public List<DeckDTO> getDueDecksForUser(Integer userId) {
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
                .filter(dto -> dto.getDueCount() > 0)
                .collect(Collectors.toList());
    }
//    public List<DeckDTO> getAllDecksWithDueInfo(Integer userId) {
//        List<Deck> userDecks = deckRepo.findByUserIdWithTag(userId);
//
//        return userDecks.stream()
//                .map(deck -> {
//                    List<Flashcard> cards = flashcardRepo.findByDeck(deck);
//                    deck.setCards(cards);
//
//                    double progress = DeckProgression.calculateDeckProgression(deck);
//                    deck.setDeckProgress(new DeckProgress(progress));
//
//                    long dueCount = cards.stream()
//                            .filter(this::isCardDue)
//                            .count();
//
//                    DeckDTO dto = DeckMapper.toDTO(deck, (int) dueCount);
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }
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
        return state == null || state.isDueToday();
    }

    public DeckDTO updateDeck(Integer deckId, String newTitle, Integer newTagId) {
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
            deck.setTag(null);
        }

        Deck savedDeck = deckRepo.save(deck);
        DeckDTO dto = DeckMapper.toDTO(savedDeck);

        decksObservable.notifyListeners(getAllDecksForUser(deck.getUser().getId()));


        return dto;
    }

    public void deleteDeck(Integer deckId) {

        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        Integer userId = deck.getUser().getId();

        deckRepo.deleteById(deckId);


        decksObservable.notifyListeners(getAllDecksForUser(userId));

    }

    // --- Flashcard CRUD ---

    public FlashcardDTO addFlashcard(Integer deckId, String front, String back) {

        Deck deck = deckRepo.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));

        Flashcard card = new Flashcard(front, back, deck);
        Flashcard savedCard = flashcardRepo.save(card);

        FlashcardDTO dto = FlashcardMapper.toDTO(savedCard);


        flashcardsObservable.notifyListeners(getFlashcardsForDeck(deckId));


        return dto;
    }

    public boolean deckExists(Integer userId, String title) {
        return deckRepo.existsByUserIdAndTitle(userId, title);
    }

    public List<FlashcardDTO> getFlashcardsForDeck(Integer deckId) {
        List<Flashcard> cards = flashcardRepo.findByDeckId(deckId);
        return cards.stream().map(FlashcardMapper::toDTO).collect(Collectors.toList());
    }

    public FlashcardDTO updateFlashcard(Integer cardId, String newFront, String newBack) {

        Flashcard card = flashcardRepo.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard not found"));

        if (newFront != null && !newFront.isBlank()) card.setFront(newFront);
        if (newBack != null && !newBack.isBlank()) card.setBack(newBack);

        Flashcard savedCard = flashcardRepo.save(card);


        flashcardsObservable.notifyListeners(
                getFlashcardsForDeck(savedCard.getDeck().getId())
        );
        // --------------------------------------------------------------------

        return FlashcardMapper.toDTO(savedCard);
    }

    public void deleteFlashcard(Integer cardId) {

        Flashcard card = flashcardRepo.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard not found"));

        Integer deckId = card.getDeck().getId();

        flashcardRepo.deleteById(cardId);


        flashcardsObservable.notifyListeners(getFlashcardsForDeck(deckId));

    }

    // Search / Filter
    public List<DeckDTO> searchDecks(Integer userId, String searchText, Integer tagId) {

        List<DeckDTO> all = getAllDecksForUser(userId);

        return all.stream()
                // Filtrate after Deck Title (SearchBar)
                .filter(d -> {
                    if (searchText == null || searchText.isBlank()) return true;
                    return d.getTitle().toLowerCase().contains(searchText.toLowerCase());
                })

                // Tag Filter (Dropdown)
                .filter(d -> {
                    if (tagId == null) return true; // (All Tags)
                    if (d.getTagDTO() == null) return false;
                    return d.getTagDTO().getId() == tagId;
                })

                .toList();
    }
    public long showEstimatedDate(String rating, int cardID){
        Flashcard flashcard = flashcardRepo.findById(cardID)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard not found"));
        RatingStrategy strategy = StrategyFactory.createStrategy(rating);

        CardLearningState state = flashcard.getCardLearningState();
        return FlashcardProgression.estimateDate(strategy, state);
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
