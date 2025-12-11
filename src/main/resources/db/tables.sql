CREATE TABLE users (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    date_created DATE DEFAULT CURRENT_DATE,
    CHECK (char_length(username) BETWEEN 3 AND 20)
);

CREATE TABLE tags (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    CHECK (char_length(title) BETWEEN 1 AND 20),
    color CHAR(6) NOT NULL,
    CHECK (color ~ '^[0-9A-Fa-f]{6}$'),
    UNIQUE(user_id, title)
);

CREATE TABLE decks (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title TEXT NOT NULL,
    date_created DATE DEFAULT CURRENT_DATE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    tag_id INTEGER REFERENCES tags(id) ON DELETE SET NULL,
    UNIQUE(user_id, title),
    CHECK (char_length(title) BETWEEN 1 AND 40)
);

CREATE TABLE flashcards (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    front TEXT NOT NULL,
    back TEXT NOT NULL,
    date_created DATE DEFAULT CURRENT_DATE,
    deck_id INTEGER NOT NULL REFERENCES decks(id) ON DELETE CASCADE,
    UNIQUE(deck_id, front),
    CHECK (char_length(front) BETWEEN 1 AND 500),
    CHECK (char_length(back) BETWEEN 1 AND 500)
);

CREATE TABLE card_learning_state (
    flashcard_id INT PRIMARY KEY,
    last_review_date DATE,
    next_review_date DATE, --DEFAULT CURRENT_DATE,
    interval_between_reviews NUMERIC(10,4) DEFAULT 1,
    number_of_times_viewed INT DEFAULT 0,
    CHECK (interval_between_reviews > 0),
    CHECK (number_of_times_viewed >= 0),
    FOREIGN KEY (flashcard_id) REFERENCES flashcards(id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION create_card_learning_state()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO card_learning_state (
        flashcard_id,
        last_review_date,
        next_review_date,
        number_of_times_viewed
    )
    VALUES (
        NEW.id,     -- shared PK = flashcard id
        NULL,       -- last_review_date
        NULL,       -- next_review_date
        0           -- default number_of_times_viewed
    );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER flashcard_after_insert
AFTER INSERT ON flashcards
FOR EACH ROW
EXECUTE FUNCTION create_card_learning_state();
