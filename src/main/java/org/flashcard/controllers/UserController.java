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
import org.flashcard.models.services.UserService;
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


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ---User CRUD---

    public UserDTO createUser(String username) {
        return userService.createUser(username);
    }


    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    public UserDTO getUserById(Integer userId) {
        return userService.getUserById(userId);
    }

    public void deleteUser(Integer userId) {
        userService.deleteUser(userId);
    }

    public UserDTO getCurrentUser() {
        return userService.getCurrentUser();
    }

    public Integer getCurrentUserId() {
        return userService.getCurrentUserId();
    }

    public void loginByUserId(Integer userId) {
        userService.loginByUserId(userId);
    }

}