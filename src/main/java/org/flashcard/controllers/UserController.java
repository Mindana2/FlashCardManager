package org.flashcard.controllers;

import org.flashcard.application.dto.DeckDTO;
import org.flashcard.application.dto.TagDTO;
import org.flashcard.application.dto.UserDTO;
import org.flashcard.application.mapper.DeckMapper;
import org.flashcard.application.mapper.TagMapper;
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

/* We use Spring Data JPA to access the database.
 *
 * This class is annotated with @Service, which tells Spring
 * that it is a service-layer component.
 *
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 *
 * The @Transactional annotation ensures that no database transactions are left unfinished.
 * It automatically aborts any transactions that result in an error.
 * This allows us to write logic without manually handling database transactions.
 */
@Service
@Transactional
public class UserController {

    private final UserRepository userRepo;
    private final DeckRepository deckRepo;
    private final TagRepository tagRepo;


    private User currentUser;

    public UserController(UserRepository userRepo,
                          DeckRepository deckRepo,
                          TagRepository tagRepo) {
        this.userRepo = userRepo;
        this.deckRepo = deckRepo;
        this.tagRepo = tagRepo;
    }

    // ---User CRUD---

    public UserDTO createUser(String username, String password) {
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

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

    public boolean login(String username, String password) {
        Optional<User> optionalUser = userRepo.findByUsername(username);

        if (optionalUser.isEmpty()) {   //User does not exist
            return false;
        }

        User user = optionalUser.get();


        String hashedInput = hashPassword(password);

        if (user.getPassword().equals(hashedInput)) {
            this.currentUser = user;
            return true;
        }

        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    public UserDTO getCurrentUser() {
        if (currentUser == null) return null;
        return UserMapper.toDTO(currentUser);
    }

    public Integer getCurrentUserId() {
        return (currentUser != null) ? currentUser.getId() : null;
    }

    public void loginByUserId(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

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



    // --- User-related queries ---

    public List<DeckDTO> getDecksForUser(Integer userId) {
        if (!userRepo.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        List<Deck> decks = deckRepo.findByUserId(userId);

        return DeckMapper.toDTOList(decks);
    }

    public List<TagDTO> getTagsForUser(Integer userId) {
        if (!userRepo.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        List<Tag> tags = tagRepo.findByUserId(userId);

        return TagMapper.toDTOList(tags);
    }

    public String getTagText(Integer tagId) {
        return tagRepo.findById(tagId)
                .map(tag -> "Tag: " + tag.getTitle() + ", Color: #" + tag.getColor())
                .orElse("");
    }
}