-- Insert sample language
INSERT INTO languages (language_code, language_name, difficulty_level)
VALUES ('es', 'Spanish', 2);

-- Insert sample lesson
INSERT INTO lessons (language_id, title, description, difficulty_level, order_index)
VALUES (
    (SELECT language_id FROM languages WHERE language_code = 'es'),
    'Basic Greetings',
    'Learn common Spanish greetings',
    1,
    1
);

-- Test query to fetch lessons for a language
SELECT l.title, l.description, lang.language_name
FROM lessons l
JOIN languages lang ON l.language_id = lang.language_id
WHERE lang.language_code = 'es'; 