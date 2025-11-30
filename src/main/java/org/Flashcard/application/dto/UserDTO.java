package org.Flashcard.application.dto;

public class UserDTO {
    private final int id;
    private final String username;

    public UserDTO(int id, String username) {
        this.id = id;
        this.username = username;
    }
    public int getId() { return id; }
    public String getUsername() { return username; }
}
