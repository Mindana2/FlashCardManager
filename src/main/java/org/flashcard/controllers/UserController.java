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

    public UserDTO createUser(String username) {
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }


        User user = new User(username);
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

    public UserDTO getUserById(Integer userId) {
        return UserMapper.toDTO(getUserByIdEntity(userId));
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

    public TagDTO createTag(Integer userId, String title, String color) {

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag title cannot be empty");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Tag tag = new Tag(title.trim(), color, user);
        Tag savedTag = tagRepo.save(tag);
        return TagMapper.toDTO(savedTag);
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