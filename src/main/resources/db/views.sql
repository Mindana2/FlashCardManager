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
