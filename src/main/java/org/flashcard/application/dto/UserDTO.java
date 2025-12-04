package org.flashcard.application.dto;

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
