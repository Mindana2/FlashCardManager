-- View: flashcards_learning_minimal
CREATE OR REPLACE VIEW flashcards_learning_minimal AS
SELECT
    f.id,
    cls.last_review_date,
    cls.next_review_date,
    cls.interval_between_reviews,
    cls.number_of_times_viewed
FROM flashcards f
LEFT JOIN card_learning_state cls ON f.id = cls.flashcard_id;


CREATE OR REPLACE VIEW deck_debug AS
SELECT d.id, d.title, t.title AS tag_title
FROM decks d
LEFT JOIN tags t ON d.tag_id = t.id;

CREATE OR REPLACE VIEW deck_debug2 AS
SELECT
    d.id            AS deck_id,
    d.title         AS deck_title,
    t.id            AS tag_id,
    t.title         AS tag_title,
    d.user_id       AS deck_user_id,
    t.user_id       AS tag_user_id
FROM decks d
LEFT JOIN tags t ON d.tag_id = t.id;
