package org.flashcard.application.mapper;

import org.flashcard.application.dto.UserDTO;
import org.flashcard.models.dataclasses.User;
//Our Mapper classes converts our data classes to DTOs
public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername()
        );
    }
}
