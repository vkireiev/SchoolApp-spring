CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    group_id BIGINT REFERENCES groups (id) ON DELETE CASCADE,
    last_name VARCHAR(35) NOT NULL,
    first_name VARCHAR(35) NOT NULL,
    CONSTRAINT student_flname UNIQUE (last_name, first_name)
);
