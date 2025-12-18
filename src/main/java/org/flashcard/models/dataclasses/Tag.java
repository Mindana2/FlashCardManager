package org.flashcard.models.dataclasses;

import jakarta.persistence.*;


/* Our dataclasses also take use of Spring Framework.
 * @Entity denotes that this class represents the "Tags" table in the database.
 * Spring can then use this class to map between Java objects and database rows:
 * When a repository like UserRepository calls save(), findById(), or delete(), Spring automatically:
 *   1. Reads these annotations to know the table and columns.
 *   2. Generates the appropriate SQL.
 *   3. Maps database rows to Tag objects and vice versa.
 * This helps us reduce the amount of boilerplate SQL we need to write.
 */

/**
 * Represents a customizable category label that allows users to organize their
 * flashcard decks using unique titles and hex-based color coding.
 */

@Entity
@Table(name = "Tags",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "title"})}) // unique per user
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 6) // hex color
    private String color;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    // Constructor
    public Tag() {}

    public Tag(String title, String color, User user) {
        this.title = title;
        this.color = color;
        this.user = user;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getColor() { return color;}
    public void setColor(String color) { this.color = color; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

}
