INSERT INTO users (username) VALUES
--Alice is a student at the University of Waterloo
('Alice'),      -- 1
--Carol is traveler and geography enthusiast
('Carol'),      -- 2
--Tom Jones is a reader and astronomer
('Tom Jones');  -- 3


INSERT INTO tags (user_id, title, color) VALUES
(1, 'Math', '99FF99'),              -- 1
(1, 'Computer Science', 'CC99FF');  -- 2

-- Carol's tags
INSERT INTO tags (user_id, title, color) VALUES
(2, 'Languages', '3399FF'),         -- 3
(2, 'Geography', '66CC99');         -- 4

-- Tom Jones's tags
INSERT INTO tags (user_id, title, color) VALUES
(3, 'Astronomy', '66CC00'),         -- 5
(3, 'Books', '9900CC');             -- 6

-- Alice's decks
INSERT INTO decks (title, user_id, tag_id) VALUES
('Linear Algebra', 1, 1),                -- 1 Math
('Calculus', 1, 1),                       -- 2 Math
('Java Programming', 1, 2),               -- 3 Computer Science
('Algorithms & Data Structures', 1, 2);   -- 4 Computer Science

INSERT INTO flashcards (front, back, deck_id) VALUES
('Define a vector', 'An object with both magnitude and direction', 1),
('What is a matrix?', 'A rectangular array of numbers arranged in rows and columns', 1),
('Determinant of a 2x2 matrix [[a,b],[c,d]]', 'ad - bc', 1),
('What is a linear transformation?', 'A function that preserves vector addition and scalar multiplication', 1);

INSERT INTO flashcards (front, back, deck_id) VALUES
('Derivative of x^2', '2x', 2),
('Integral of 1/x dx', 'ln|x| + C', 2),
('Limit of sin(x)/x as x → 0', '1', 2),
('d/dx of e^x', 'e^x', 2);

INSERT INTO flashcards (front, back, deck_id) VALUES
('Keyword for inheritance in Java', 'extends', 3),
('Keyword for interface implementation', 'implements', 3),
('Print to console in Java', 'System.out.println()', 3),
('Primitive type for decimals', 'double', 3);

INSERT INTO flashcards (front, back, deck_id) VALUES
('Big-O of binary search', 'O(log n)', 4),
('FIFO data structure', 'Queue', 4),
('LIFO data structure', 'Stack', 4),
('What is a hash table?', 'A data structure that maps keys to values using a hash function', 4);

-- Carol's decks
INSERT INTO decks (title, user_id, tag_id) VALUES
('Spanish Vocabulary', 2, 3),   -- 5 Languages
('Sign Language', 2, 3),        -- 6 Languages
('Country Capitals', 2, 4),     -- 7 Geography
('Mountain Ranges', 2, 4),      -- 8 Geography
('Famous Rivers', 2, 4);        -- 9 Geography

INSERT INTO flashcards (front, back, deck_id) VALUES
('Hello', 'Hola', 5),
('Goodbye', 'Adiós', 5),
('Please', 'Por favor', 5),
('Thank you', 'Gracias', 5),
('Yes', 'Sí', 5),
('No', 'No', 5);

INSERT INTO flashcards (front, back, deck_id) VALUES
('Hello in ASL', 'Wave hand at forehead', 6),
('Thank you in ASL', 'Touch fingers to chin and move forward', 6),
('Yes in ASL', 'Fist nodding up and down', 6),
('No in ASL', 'Index and middle finger tap thumb', 6);

INSERT INTO flashcards (front, back, deck_id) VALUES
('France', 'Paris', 7),
('Germany', 'Berlin', 7),
('Italy', 'Rome', 7),
('Spain', 'Madrid', 7),
('Japan', 'Tokyo', 7),
('Canada', 'Ottawa', 7);

INSERT INTO flashcards (front, back, deck_id) VALUES
('Highest mountain in the world', 'Mount Everest', 8),
('Longest mountain range in the world', 'Andes', 8),
('Famous mountain range in North America', 'Rockies', 8),
('Famous mountain range in Europe', 'Alps', 8);

INSERT INTO flashcards (front, back, deck_id) VALUES
('Longest river in the world', 'Nile', 9),
('Longest river in South America', 'Amazon', 9),
('Major river in Europe', 'Danube', 9),
('Major river in China', 'Yangtze', 9);

-- Tom Jones' decks
INSERT INTO decks (title, user_id, tag_id) VALUES
('Our Solar System', 3, 5),  -- 10 Astronomy
('Space Facts', 3, 5),       -- 11 Astronomy
('Book Summaries', 3, 6);    -- 12 Books

INSERT INTO flashcards (front, back, deck_id) VALUES
('Latin name of Earth’s moon', 'Luna', 10),
('Largest planet in the solar system', 'Jupiter', 10),
('Planet with prominent rings', 'Saturn', 10),
('Planet known as the Red Planet', 'Mars', 10),
('Smallest planet in the solar system', 'Mercury', 10);

INSERT INTO flashcards (front, back, deck_id) VALUES
('Speed of light in vacuum', '299,792,458 m/s', 11),
('Distance from Earth to Sun', 'Approximately 149.6 million km', 11),
('First human in space', 'Yuri Gagarin', 11),
('First Moon landing', 'Apollo 11 in 1969', 11);

INSERT INTO flashcards (front, back, deck_id) VALUES
('1984 by George Orwell', 'Dystopian novel about surveillance and totalitarian regime', 12),
('Sapiens by Yuval Noah Harari', 'A brief history of humankind', 12),
('To Kill a Mockingbird', 'Novel about racial injustice in the US South', 12);

