------------------------------
-- Users
------------------------------
INSERT INTO users (username) VALUES
('Alice'),
('Bob'),
('Carol'),
('Dave'),
('Tom Jones');

------------------------------
-- Tags
------------------------------
-- Alice's tags (user_id = 1)
INSERT INTO tags (user_id, title, color) VALUES
(1, 'School', 'FF9900'),
(1, 'Hobby', 'CC00FF'),
(1, 'Math', '00FF99');

-- Bob's tags (user_id = 2)
INSERT INTO tags (user_id, title, color) VALUES
(2, 'Work', '00CC88'),
(2, 'Fun', 'FF00CC');

-- Carol's tags (user_id = 3)
INSERT INTO tags (user_id, title, color) VALUES
(3, 'Languages', '3399FF'),
(3, 'Travel', 'FF6633');

-- Dave's tags (user_id = 4)
INSERT INTO tags (user_id, title, color) VALUES
(4, 'Fitness', '66CC00'),
(4, 'Books', '9900CC');

------------------------------
-- Decks
------------------------------
-- Alice's decks (user_id = 1)
INSERT INTO decks (title, user_id, tag_id) VALUES
('Math Deck', 1, 1),         -- deck_id = 1
('Science Deck', 1, 1),      -- deck_id = 2
('Algebra Basics', 1, 3),    -- deck_id = 3
('Country Capitals', 1, 1);  -- deck_id = 4

-- Bob's decks (user_id = 2)
INSERT INTO decks (title, user_id, tag_id) VALUES
('Programming Deck', 2, 3),  -- deck_id = 5
('Office Terms', 2, 4);      -- deck_id = 6

-- Carol's decks (user_id = 3)
INSERT INTO decks (title, user_id, tag_id) VALUES
('Spanish Vocabulary', 3, 5),  -- deck_id = 7
('Travel Phrases', 3, 6);      -- deck_id = 8

-- Dave's decks (user_id = 4)
INSERT INTO decks (title, user_id, tag_id) VALUES
('Workout Routine', 4, 7),     -- deck_id = 9
('Book Summaries', 4, 8);      -- deck_id = 10

------------------------------
-- Flashcards
------------------------------
-- Alice's Math Deck (deck_id = 1)
INSERT INTO flashcards (front, back, deck_id) VALUES
('2 + 2', '4', 1),
('5 * 3', '15', 1),
('Square root of 16', '4', 1),
('10 / 2', '5', 1);

-- Alice's Science Deck (deck_id = 2)
INSERT INTO flashcards (front, back, deck_id) VALUES
('Water chemical formula', 'H2O', 2),
('Speed of light in vacuum', '299,792,458 m/s', 2),
('Planet closest to the Sun', 'Mercury', 2),
('Human body largest organ', 'Skin', 2);

-- Alice's Algebra Basics (deck_id = 3)
INSERT INTO flashcards (front, back, deck_id) VALUES
('Solve for x: 2x + 3 = 7', 'x = 2', 3),
('Factor: x^2 - 9', '(x - 3)(x + 3)', 3);

-- Alice's Country Capitals (deck_id = 4)
INSERT INTO flashcards (front, back, deck_id) VALUES
('France', 'Paris', 4),
('Germany', 'Berlin', 4),
('Italy', 'Rome', 4),
('Spain', 'Madrid', 4),
('United Kingdom', 'London', 4),
('USA', 'Washington, D.C.', 4),
('Canada', 'Ottawa', 4),
('Japan', 'Tokyo', 4),
('Australia', 'Canberra', 4),
('Brazil', 'Brasília', 4);

-- Bob's Programming Deck (deck_id = 5)
INSERT INTO flashcards (front, back, deck_id) VALUES
('Java keyword for inheritance', 'extends', 5),
('Print to console in Java', 'System.out.println()', 5),
('Keyword for interface implementation', 'implements', 5),
('Java primitive type for decimals', 'double', 5);

-- Bob's Office Terms (deck_id = 6)
INSERT INTO flashcards (front, back, deck_id) VALUES
('CEO', 'Chief Executive Officer', 6),
('ROI', 'Return on Investment', 6);

-- Carol's Spanish Vocabulary (deck_id = 7)
INSERT INTO flashcards (front, back, deck_id) VALUES
('Hello', 'Hola', 7),
('Goodbye', 'Adiós', 7),
('Thank you', 'Gracias', 7);

-- Carol's Travel Phrases (deck_id = 8)
INSERT INTO flashcards (front, back, deck_id) VALUES
('Where is the bathroom?', '¿Dónde está el baño?', 8),
('How much does it cost?', '¿Cuánto cuesta?', 8);

-- Dave's Workout Routine (deck_id = 9)
INSERT INTO flashcards (front, back, deck_id) VALUES
('Push-ups', 'Do 3 sets of 15 reps', 9),
('Squats', 'Do 3 sets of 20 reps', 9);

-- Dave's Book Summaries (deck_id = 10)
INSERT INTO flashcards (front, back, deck_id) VALUES
('1984 by George Orwell', 'Dystopian novel about totalitarian regime', 10),
('To Kill a Mockingbird', 'Novel about racial injustice in the US South', 10);
