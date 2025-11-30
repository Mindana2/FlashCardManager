package org.Flashcard.repositories;

import org.Flashcard.models.dataClasses.Deck;
import org.Flashcard.models.dataClasses.FlashCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeckRepository extends BaseRepository {

    private final FlashCardRepository flashCardRepo = new FlashCardRepository();

    // Create a new deck
    public Deck create(Deck deck) throws SQLException {
        String sql = "INSERT INTO Decks (title, user_id, tag_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, deck.getTitle());
            stmt.setInt(2, deck.getOwnerUserId());
            stmt.setInt(3, deck.getTagId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) deck.setId(rs.getInt("id"));
        }
        return deck;
    }

    // Fetch all decks for a user
    public List<Deck> findByUserId(int userId) throws SQLException {
        List<Deck> decks = new ArrayList<>();
        String sql = "SELECT * FROM Decks WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                decks.add(new Deck(rs.getInt("id"), rs.getString("title"), rs.getInt("user_id"), rs.getInt("tag_id")));
            }
        }
        return decks;
    }

    // Eager loading: fetch deck with its flashcards
    public Deck findByIdWithCards(int deckId) throws SQLException {
        String sql = "SELECT * FROM Decks WHERE id = ?";
        Deck deck = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                deck = new Deck(rs.getInt("id"), rs.getString("title"), rs.getInt("user_id"), rs.getInt("tag_id"));
            }
        }

        if (deck != null) {
            List<FlashCard> cards = flashCardRepo.findByDeckId(deck.getId());
            // You can store flashcards in the deck if you add a List<FlashCard> field in Deck class
        }

        return deck;
    }


    public boolean delete(int deckId) throws SQLException {
        String sql = "DELETE FROM Decks WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

}
