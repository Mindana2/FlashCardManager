package org.Flashcard.repositories;

import org.Flashcard.models.dataClasses.FlashCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlashCardRepository extends BaseRepository {

    public FlashCard create(FlashCard card) throws SQLException {
        String sql = "INSERT INTO FlashCards (front, back, deck_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, card.getFront());
            stmt.setString(2, card.getBack());
            stmt.setInt(3, card.getDeckId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) card.setId(rs.getInt("id"));
        }
        return card;
    }

    // Fetch all flashcards for a deck
    public List<FlashCard> findByDeckId(int deckId) throws SQLException {
        List<FlashCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM FlashCards WHERE deck_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cards.add(new FlashCard(rs.getInt("id"),
                        rs.getString("front"),
                        rs.getString("back"),
                        rs.getInt("deck_id"))); //Add foreign key for state_id from CardLearningRepo
            }
        }
        return cards;
    }


    public boolean delete(int cardId) throws SQLException {
        String sql = "DELETE FROM FlashCards WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cardId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

}
