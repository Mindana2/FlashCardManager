package org.flashcard.models.dataclasses;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;


/* Our dataclasses also take use of Spring Framework.
 * @Entity denotes that this class represents the "Decks" table in the database.
 * Spring can then use this class to map between Java objects and database rows:
 * When a repository like UserRepository calls save(), findById(), or delete(), Spring automatically:
 *   1. Reads these annotations to know the table and columns.
 *   2. Generates the appropriate SQL.
 *   3. Maps database rows to Deck objects and vice versa.
 * This helps us reduce the amount of boilerplate SQL we need to write.
 */
@Entity
@Table(name = "Decks",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "title"})})
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private LocalDate dateCreated = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tagId", nullable = true)
    private Tag tag;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Flashcard> cards;

    // Optional in-memory DeckProgress
    @Transient
    private DeckProgress deckProgress;


    // Constructors
    public Deck() {}

    public Deck(String title, User user) {
        this.title = title;
        this.user = user;
        this.dateCreated = LocalDate.now();
    }

    // Getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }


    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Tag getTag() { return tag; }
    public void setTag(Tag tag) { this.tag = tag; }

    public List<Flashcard> getCards() { return cards; }
    public void setCards(List<Flashcard> cards) { this.cards = cards; }

    public DeckProgress getDeckProgress() { return deckProgress; }
    public void setDeckProgress(DeckProgress deckProgress) { this.deckProgress = deckProgress; }

}
