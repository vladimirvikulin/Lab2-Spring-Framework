DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS category;

CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE news (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    date TIMESTAMP NOT NULL,
    category_id INT REFERENCES category (id) ON DELETE CASCADE
);

INSERT INTO category (name) VALUES ('Science');
INSERT INTO category (name) VALUES ('Sport');

INSERT INTO news (title, content, date, category_id)
VALUES (
    'NASA''s Perseverance rover finds evidence of flowing water on Mars',
    'Perseverance, NASA''s rover that''s roaming on the Martian surface carrying out important observations, recently found evidence of gushing water.',
    CURRENT_DATE,
    1
);

INSERT INTO news (title, content, date, category_id)
VALUES (
    'World sport rattled by rulings that open route to goal for new entrants',
    'ECJ decision on football’s European Super League has profound implications for other games, say lawyers.',
    CURRENT_DATE,
    2
);