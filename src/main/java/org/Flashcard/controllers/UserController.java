package org.Flashcard.controllers;

import org.Flashcard.models.dataClasses.User;
import org.Flashcard.repositories.DeckRepository;
import org.Flashcard.repositories.TagRepository;
import org.Flashcard.repositories.UserRepository;
import org.Flashcard.models.dataClasses.Deck;

import java.sql.SQLException;
import java.util.List;

public class UserController {
    private final UserRepository userRepo;
    private final DeckRepository deckRepo;
    private final TagRepository tagRepo;
    public UserController(UserRepository userRepo, DeckRepository deckRepo, TagRepository tagRepo) {
        this.userRepo = userRepo;
        this.deckRepo = deckRepo;
        this.tagRepo = tagRepo;
    }
    public List<User> getAllUsers() throws SQLException {
        return userRepo.findAll();
    }
    public List<Deck> getDecksForUser(int userId) throws SQLException {
        return deckRepo.findByUserId(userId);
    }
    public String getTagText(int tagId) throws SQLException {
        var tag = tagRepo.findById(tagId);
        if (tag == null) return "";
        return "Tag: " + tag.getTitle() + ", Color: #" + tag.getColor();
    }
}
