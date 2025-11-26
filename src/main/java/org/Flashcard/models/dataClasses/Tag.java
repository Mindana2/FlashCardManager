package org.Flashcard.models.dataClasses;

public class Tag {
    private int id;
    private int userId;    // owner of the tag
    private String title;
    private String color;  // hex color code

    // Constructors
    public Tag() {}

    public Tag(int id, int userId, String title, String color) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.color = color;
    }

    public Tag(int userId, String title, String color) {
        this.userId = userId;
        this.title = title;
        this.color = color;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    @Override
    public String toString() {
        return "Tag{id=" + id + ", userId=" + userId + ", title='" + title + "', color='" + color + "'}";
    }
}
