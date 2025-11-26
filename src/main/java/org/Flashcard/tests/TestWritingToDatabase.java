package org.Flashcard.tests;


import org.Flashcard.models.dataClasses.Deck;
import org.Flashcard.models.dataClasses.FlashCard;
import org.Flashcard.models.dataClasses.Tag;
import org.Flashcard.models.dataClasses.User;
import org.Flashcard.repositories.*;

import java.sql.SQLException;
import java.util.List;

public class TestWritingToDatabase {

    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        TagRepository tagRepo = new TagRepository();
        DeckRepository deckRepo = new DeckRepository();
        FlashCardRepository cardRepo = new FlashCardRepository();

        try {
            User zack = new User();
            zack.setUsername("Zack");
            zack.setPassword("bingbong");
            zack = userRepo.create(zack);
            System.out.println("Created user: " + zack);

            Tag schoolTag = new Tag();
            schoolTag.setUserId(zack.getId());
            schoolTag.setTitle("School");
            schoolTag.setColor("FF9900");
            schoolTag = tagRepo.create(schoolTag);
            System.out.println("Created tag: " + schoolTag);

            Deck biologyDeck = new Deck();
            biologyDeck.setTitle("Biology");
            biologyDeck.setUserId(zack.getId());
            biologyDeck.setTagId(schoolTag.getId());
            biologyDeck = deckRepo.create(biologyDeck);
            System.out.println("Created deck: " + biologyDeck);

            FlashCard card1 = new FlashCard();
            card1.setFront("2 + 2");
            card1.setBack("4");
            card1.setDeckId(biologyDeck.getId());
            card1 = cardRepo.create(card1);

            FlashCard card2 = new FlashCard();
            card2.setFront("3 * 3");
            card2.setBack("9");
            card2.setDeckId(biologyDeck.getId());
            card2 = cardRepo.create(card2);

            System.out.println("Created flashcards:");
            List<FlashCard> cards = cardRepo.findByDeckId(biologyDeck.getId());
            cards.forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
