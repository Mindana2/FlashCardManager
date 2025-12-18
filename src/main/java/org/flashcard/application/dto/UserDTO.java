package org.flashcard.application.dto;

/**
 *We use DataTransferObjects to transfer data between layers,
 *this ensures View is read-only.
 *This class represents a User Data Transfer Object.
 */

public class UserDTO {
    private final int id;
    private final String username;

    public UserDTO(int id, String username) {
        this.id = id;
        this.username = username;
    }
    public int getId() { return id; }
    public String getUsername() { return username; }

    @Override
    public String toString() {
        return username;   // or getUsername()
    }
}
