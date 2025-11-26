package org.Flashcard.repositories;

import org.Flashcard.models.dataClasses.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends BaseRepository {

    // Create a new user
    public User create(User user) throws SQLException {
        String sql = "INSERT INTO Users (username, password) VALUES (?, ?) RETURNING id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt("id"));
            }
        }
        return user;
    }


    // Find a user by ID
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        }
        return null;
    }

    // Fetch all users
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password")));
            }
        }
        return users;
    }


    //deletes a user, all dependent decks, tags and flashcards are also deleted automatically
    public boolean delete(int userId) throws SQLException {
        String sql = "DELETE FROM Users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // true if a row was deleted
        }
    }
}
