
--För att köra:
--\i


\set QUIET true
SET client_min_messages TO WARNING;

DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO CURRENT_USER;

\set ON_ERROR_STOP ON
SET client_min_messages TO NOTICE;
\set QUIET false

-- Create tables
\ir tables.sql

-- Insert sample data
\ir inserts.sql

-- Create views (for testing)
\ir views.sql

-- Test queries
SELECT * FROM Users;
SELECT * FROM Decks;
SELECT * FROM Tags;
SELECT * FROM FlashCards;

SELECT * FROM user_decks WHERE user_id = 1;  -- Alice decks

SELECT * FROM flashcards_full WHERE user_id = 1; -- Alice flashcards