DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS lessons CASCADE;
DROP TABLE IF EXISTS users_courses CASCADE;
DROP TABLE IF EXISTS users_lessons CASCADE;

CREATE TABLE groups
(
    group_id   BIGSERIAL PRIMARY KEY,
    group_name VARCHAR(5) NOT NULL UNIQUE
);

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    group_id   BIGINT,
    user_type  VARCHAR(15)  NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

CREATE TABLE courses
(
    course_id          BIGSERIAL PRIMARY KEY,
    course_name        VARCHAR(50)  NOT NULL UNIQUE,
    course_description VARCHAR(255) NOT NULL,
    user_id            BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE rooms
(
    room_id   BIGSERIAL PRIMARY KEY,
    room_code VARCHAR(5) NOT NULL UNIQUE
);

CREATE TABLE lessons
(
    lesson_id    BIGSERIAL PRIMARY KEY,
    course_id    BIGINT       NOT NULL,
    user_id      BIGINT       NOT NULL,
    room_id      BIGINT       NOT NULL,
    day_of_week  VARCHAR(10)  NOT NULL,
    week_number  INT          NOT NULL,
    lesson_order INT          NOT NULL,
    lesson_type  VARCHAR(20) NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses (course_id),
    FOREIGN KEY (room_id) REFERENCES rooms (room_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE users_courses
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT,
    course_id BIGINT,
    CONSTRAINT FK_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_courses FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE,
    UNIQUE (user_id, course_id)
);

CREATE TABLE users_lessons
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT,
    lesson_id BIGINT,
    CONSTRAINT FK_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_lessons FOREIGN KEY (lesson_id) REFERENCES lessons (lesson_id) ON DELETE CASCADE,
    UNIQUE (user_id, lesson_id)
);
