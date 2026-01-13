package org.flashcard.models.dataclasses;

import jakarta.persistence.*;
import java.time.LocalDate;


/* Our dataclasses also take use of Spring Framework.
 * @Entity denotes that this class represents the "Users" table in the database.
 * Spring can then use this class to map between Java objects and database rows:
 * When a repository like UserRepository calls save(), findById(), or delete(), Spring automatically:
 *   1. Reads these annotations to know the table and columns.
 *   2. Generates the appropriate SQL.
 *   3. Maps database rows to User objects and vice versa.
 * This helps us reduce the amount of boilerplate SQL we need to write.
 */

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // matches GENERATED ALWAYS AS IDENTITY
    private Integer id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;


    @Column(nullable = false)
    private LocalDate dateCreated = LocalDate.now(); // column name now matches Java field

    // Constructors
    public User() {}

    public User(String username) {
        this.username = username;
        this.dateCreated = LocalDate.now();
    }


    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }

}
