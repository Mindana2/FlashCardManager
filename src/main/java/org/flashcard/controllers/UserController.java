package org.flashcard.controllers;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.UserDTO;
import org.flashcard.application.mapper.DeckMapper;
import org.flashcard.application.mapper.UserMapper;
import org.flashcard.models.dataclasses.Deck;
import org.flashcard.models.dataclasses.Tag;
import org.flashcard.models.dataclasses.User;
import org.flashcard.repositories.DeckRepository;
import org.flashcard.repositories.TagRepository;
import org.flashcard.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserController {

    private final UserRepository userRepo;
    private final DeckRepository deckRepo;
    private final TagRepository tagRepo;

    // Håller koll på den inloggade användaren i minnet (Stateful för Desktop-app)
    private User currentUser;

    public UserController(UserRepository userRepo,
                          DeckRepository deckRepo,
                          TagRepository tagRepo) {
        this.userRepo = userRepo;
        this.deckRepo = deckRepo;
        this.tagRepo = tagRepo;
    }

    // --- Authentication ---

    public boolean login(String username, String password) {
        Optional<User> optionalUser = userRepo.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return false; // Användaren finns inte
        }

        User user = optionalUser.get();

        // Hasha inmatat lösenord för att jämföra med det lagrade hashet
        String hashedInput = hashPassword(password);

        if (user.getPassword().equals(hashedInput)) {
            this.currentUser = user; // VIKTIGT: Sätter den inloggade användaren
            return true;
        }

        return false; // Fel lösenord
    }

    public void logout() {
        this.currentUser = null;
    }

    // Hämtar den inloggade användaren som DTO för Vyn
    public UserDTO getCurrentUser() {
        if (currentUser == null) return null;
        return UserMapper.toDTO(currentUser);
    }

    // Hjälpmetod för att få ID (används ofta av andra Controllers/Vyer)
    public Integer getCurrentUserId() {
        return (currentUser != null) ? currentUser.getId() : null;
    }

    public void loginByUserId(Integer userId) {
        // 1. Controllern använder ID:t för att hämta den riktiga Entiteten
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2. Sätt sessionen
        this.currentUser = user;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    // --- User CRUD ---

    public UserDTO createUser(String username, String password) {
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // VIKTIGT: Hasha lösenordet INNAN vi sparar det!
        String hashedPassword = hashPassword(password);

        User user = new User(username, hashedPassword);
        User savedUser = userRepo.save(user);

        return UserMapper.toDTO(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Hämtar Entiteten (används internt eller vid behov)
    public User getUserByIdEntity(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserDTO updateUser(Integer userId, String newUsername, String newPassword) {
        User user = getUserByIdEntity(userId);

        if (newUsername != null && !newUsername.isBlank()) {
            if (!newUsername.equals(user.getUsername()) && userRepo.existsByUsername(newUsername)) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setUsername(newUsername);
        }

        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(hashPassword(newPassword));    //Hash password when updating as well
        }

        User savedUser = userRepo.save(user);
        return UserMapper.toDTO(savedUser);
    }

    public void deleteUser(Integer userId) {
        if (!userRepo.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        if (currentUser != null && currentUser.getId().equals(userId)) {
            this.currentUser = null;
        }
        userRepo.deleteById(userId);
    }

    // --- User-related queries ---

    public List<DeckDTO> getDecksForUser(Integer userId) {
        if (!userRepo.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        List<Deck> decks = deckRepo.findByUserId(userId);

        return DeckMapper.toDTOList(decks);
    }

    public List<Tag> getTagsForUser(Integer userId) {
        if (!userRepo.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        return tagRepo.findByUserId(userId);
    }

    public String getTagText(Integer tagId) {
        return tagRepo.findById(tagId)
                .map(tag -> "Tag: " + tag.getTitle() + ", Color: #" + tag.getColor())
                .orElse("");
    }
}