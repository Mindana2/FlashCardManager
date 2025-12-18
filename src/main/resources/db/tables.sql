CREATE TABLE users (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    date_created Date DEFAULT CURRENT_DATE,
    CHECK (char_length(username) BETWEEN 3 AND 20)
);

CREATE TABLE tags (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    title TEXT NOT NULL,
    color CHAR(6) NOT NULL,

    CHECK (char_length(title) BETWEEN 1 AND 20),
    CHECK (color ~ '^[0-9A-Fa-f]{6}$'),

    UNIQUE (user_id, title),
    UNIQUE (id, user_id),

    FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE decks (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title TEXT NOT NULL,
    date_created DATE DEFAULT CURRENT_DATE,
    user_id INTEGER NOT NULL,
    tag_id INTEGER,

    CHECK (char_length(title) BETWEEN 1 AND 40),
    UNIQUE (user_id, title),

    FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    -- deck and tag must belong to the same user
    FOREIGN KEY (tag_id, user_id)
        REFERENCES tags (id, user_id)
        ON DELETE SET NULL
);

CREATE TABLE flashcards (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    front TEXT NOT NULL,
    back TEXT NOT NULL,
    date_created DATE DEFAULT CURRENT_DATE,
    deck_id INTEGER NOT NULL,

    UNIQUE (deck_id, front),
    CHECK (char_length(front) BETWEEN 1 AND 500),
    CHECK (char_length(back) BETWEEN 1 AND 500),

    FOREIGN KEY (deck_id)
        REFERENCES decks(id)
        ON DELETE CASCADE
);


CREATE TABLE card_learning_state (
    flashcard_id INTEGER PRIMARY KEY,
    last_review_date TIMESTAMP,
    next_review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    interval_between_reviews NUMERIC(10,4) DEFAULT 1,
    number_of_times_viewed INTEGER DEFAULT 0,

    CHECK (interval_between_reviews > 0),
    CHECK (number_of_times_viewed >= 0),

    FOREIGN KEY (flashcard_id)
        REFERENCES flashcards(id)
        ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION create_card_learning_state()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO card_learning_state (
        flashcard_id,
        last_review_date,
        number_of_times_viewed
    )
    VALUES (
        NEW.id,
        NULL,
        0
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER flashcard_after_insert
AFTER INSERT ON flashcards
FOR EACH ROW
EXECUTE FUNCTION create_card_learning_state();