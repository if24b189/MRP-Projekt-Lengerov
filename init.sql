CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE media (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    genre TEXT,
    media_type TEXT,
    age_restriction INT,
    owner_id INT REFERENCES users(id)
);

CREATE TABLE ratings (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    media_id INTEGER NOT NULL REFERENCES media(id) ON DELETE CASCADE,
    rating INTEGER NOT NULL,
    comment TEXT,
    UNIQUE (user_id, media_id)
);

CREATE TABLE favorites (
    user_id INT REFERENCES users(id),
    media_id INT REFERENCES media(id),
    PRIMARY KEY (user_id, media_id)
);
