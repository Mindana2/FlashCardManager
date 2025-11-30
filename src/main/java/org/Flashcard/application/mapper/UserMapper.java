package org.Flashcard.application.mapper;


import org.Flashcard.application.dto.UserDTO;
import org.Flashcard.models.dataClasses.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername()
        );
    }
}
