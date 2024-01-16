CREATE TABLE IF NOT EXISTS student_course (
    student_id BIGINT NOT NULL REFERENCES students (id) ON DELETE CASCADE,
    course_id BIGINT NOT NULL REFERENCES courses (id) ON DELETE CASCADE,
    PRIMARY KEY (student_id, course_id)
);
