CREATE TABLE IF NOT EXISTS groups (
    id BIGSERIAL NOT NULL PRIMARY KEY, 
    name VARCHAR(8) NOT NULL UNIQUE
);