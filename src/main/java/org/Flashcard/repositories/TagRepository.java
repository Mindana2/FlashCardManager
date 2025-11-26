package org.Flashcard.repositories;

import org.Flashcard.models.dataClasses.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagRepository extends BaseRepository {

    public Tag create(Tag tag) throws SQLException {
        String sql = "INSERT INTO Tags (user_id, title, color) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tag.getUserId());
            stmt.setString(2, tag.getTitle());
            stmt.setString(3, tag.getColor());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) tag.setId(rs.getInt("id"));
        }
        return tag;
    }


    // Fetch tags for a specific user
    public List<Tag> findByUserId(int userId) throws SQLException {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * FROM Tags WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tags.add(new Tag(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("color")));
            }
        }
        return tags;
    }

    public Tag findById(int id) throws SQLException {
        String sql = "SELECT * FROM Tags WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Tag(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("color")
                );
            }
        }
        return null; // Tag not found
    }

    public boolean delete(int tagId) throws SQLException {
        String sql = "DELETE FROM Tags WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tagId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
