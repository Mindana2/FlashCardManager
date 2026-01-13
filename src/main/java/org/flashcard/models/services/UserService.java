package org.flashcard.models.services;

import org.flashcard.models.dataclasses.User;
import org.flashcard.repositories.UserRepository;
import org.flashcard.application.dto.UserDTO;
import org.flashcard.application.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
/* We use Spring Data JPA to access the database.
 * This class is annotated with @Service, which tells Spring
 * that it is a service-layer component.
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 * The @Transactional annotation ensures that no database transactions are left unfinished.
 * It automatically aborts any transactions that result in an error.
 * This allows us to write logic without manually handling database transactions.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepo;

    private User currentUser;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


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


    public void deleteUser(Integer userId) {
        if (!userRepo.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        if (currentUser != null && currentUser.getId().equals(userId)) {
            currentUser = null;
        }
        userRepo.deleteById(userId);
    }

    public void loginByUserId(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        currentUser = user;
    }

    public UserDTO getCurrentUser() {
        if (currentUser == null) return null;
        return UserMapper.toDTO(currentUser);
    }

    public Integer getCurrentUserId() {
        return (currentUser != null) ? currentUser.getId() : null;
    }
}
