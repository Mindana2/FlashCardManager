package org.Flashcard.tests;

import org.Flashcard.models.dataClasses.User;
import org.Flashcard.models.dataClasses.Tag;
import org.Flashcard.models.dataClasses.Deck;
import org.Flashcard.models.dataClasses.FlashCard;
import org.Flashcard.repositories.*;

import java.sql.SQLException;
import java.util.List;

public class TestReadingFromDatabase {

    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        TagRepository tagRepo = new TagRepository();
        DeckRepository deckRepo = new DeckRepository();
        FlashCardRepository cardRepo = new FlashCardRepository();

        try {
            List<User> users = userRepo.findAll();
            System.out.println("All users:");
            users.forEach(System.out::println);

            for (User user : users) {
                int userId = user.getId();

                // Fetch tags
                List<Tag> tags = tagRepo.findByUserId(userId);
                System.out.println("\nTags for user " + user.getUsername() + ":");
                tags.forEach(System.out::println);

                // Fetch decks
                List<Deck> decks = deckRepo.findByUserId(userId);
                System.out.println("\nDecks for user " + user.getUsername() + ":");
                decks.forEach(System.out::println);

                // Fetch flashcards for each deck
                for (Deck deck : decks) {
                    List<FlashCard> cards = cardRepo.findByDeckId(deck.getId());
                    System.out.println("\nFlashcards for deck " + deck.getTitle() + ":");
                    cards.forEach(System.out::println);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}