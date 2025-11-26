package org.Flashcard.tests;

import org.Flashcard.models.dataClasses.Deck;
import org.Flashcard.models.dataClasses.FlashCard;
import org.Flashcard.models.dataClasses.Tag;
import org.Flashcard.models.dataClasses.User;
import org.Flashcard.repositories.*;

import java.sql.SQLException;
import java.util.List;

public class TestDeleteFromDatabase {

    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        TagRepository tagRepo = new TagRepository();
        DeckRepository deckRepo = new DeckRepository();
        FlashCardRepository cardRepo = new FlashCardRepository();

        try {
            System.out.println("=== Creating dummy data for deletion test ===");

            User dummyUser = userRepo.create(new User("DummyUser", "test123"));
            System.out.println("Created dummy user: " + dummyUser);

            Tag dummyTag = tagRepo.create(new Tag(dummyUser.getId(), "TempTag", "FF00AA"));
            System.out.println("Created dummy tag: " + dummyTag);

            Deck dummyDeck = deckRepo.create(
                    new Deck("TempDeck", dummyUser.getId(), dummyTag.getId())
            );
            System.out.println("Created dummy deck: " + dummyDeck);

            FlashCard card1 = cardRepo.create(
                    new FlashCard("Front A", "Back A", dummyDeck.getId())
            );
            FlashCard card2 = cardRepo.create(
                    new FlashCard("Front B", "Back B", dummyDeck.getId())
            );
            System.out.println("Created flashcards: " + card1 + ", " + card2);

            // ========== START DELETE TESTS ==========

            System.out.println("\n=== Testing: delete tag (tag_id should become NULL on deck) ===");

            boolean tagDeleted = tagRepo.delete(dummyTag.getId());
            System.out.println("Tag deleted: " + tagDeleted);

            Deck updatedDeck = deckRepo.findByIdWithCards(dummyDeck.getId());
            System.out.println("Deck after deleting tag: " + updatedDeck);

            // ========== Delete user ==========

            System.out.println("\n=== Testing: delete user (cascade should remove deck + flashcards) ===");

            boolean userDeleted = userRepo.delete(dummyUser.getId());
            System.out.println("User deleted: " + userDeleted);

            // Verify deck is gone
            Deck deckStillThere = deckRepo.findByIdWithCards(dummyDeck.getId());
            System.out.println("Deck after user deletion (should be null): " + deckStillThere);

            // Verify flashcards are gone
            List<FlashCard> cardsAfter = cardRepo.findByDeckId(dummyDeck.getId());
            System.out.println("Flashcards after user deletion (should be empty): " + cardsAfter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}