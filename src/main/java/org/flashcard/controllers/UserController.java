package org.flashcard.controllers;


import org.flashcard.application.dto.UserDTO;
import org.flashcard.models.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


/* We use Spring Data JPA to access the database.
 * This class is annotated with @Controller, which tells Spring
 * that it is a controller-layer component.
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 */

/**
 * This @Controller works as an intermediary between the View and the Service layer.
 * It handles user requests, invokes the appropriate methods in the Service layer,
 * manages user accounts and session state, handling user creation,
 * deletion, and the retrieval of the currently authenticated user's profile,
 * and returns the results back to the View.
 */
@Controller
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserDTO createUser(String username) {
        return userService.createUser(username);
    }

    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
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