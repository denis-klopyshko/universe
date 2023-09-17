BEGIN;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS authorities CASCADE;
DROP TABLE IF EXISTS users_roles CASCADE;
DROP TABLE IF EXISTS roles_authorities CASCADE;

ALTER TABLE users
    ADD COLUMN password TEXT    NOT NULL default '{bcrypt}$2a$12$TovaPwvRTDaDlrTCVfSJrOXnwGi8V//AkfImQfKN.F6LXQL8HJJgu',
    ADD COLUMN enabled  boolean NOT NULL default true,
    ALTER COLUMN user_type DROP not null;

CREATE TABLE roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE authorities
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users_roles
(
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE roles_authorities
(
    role_id      BIGINT REFERENCES roles (id) ON DELETE CASCADE,
    authority_id BIGINT REFERENCES authorities (id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, authority_id)
);

-- add test data

INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_STUDENT'),
       ('ROLE_PROFESSOR');

INSERT INTO authorities (name)
VALUES ('students::read'),
       ('students::write'),
       ('lessons::read'),
       ('lessons::write'),
       ('rooms::read'),
       ('rooms::write'),
       ('courses::read'),
       ('courses::write'),
       ('professors::read'),
       ('professors::write'),
       ('groups::read'),
       ('groups::write'),
       ('users::read'),
       ('users::write');

INSERT INTO roles_authorities
VALUES (1, 2),  -- ADMIN -> students::write
       (1, 4),  -- ADMIN -> lessons::write
       (1, 6),  -- ADMIN -> rooms::write
       (1, 8),  -- ADMIN -> courses::write
       (1, 10), -- ADMIN -> professors::write
       (1, 12), -- ADMIN -> groups::write
       (1, 14), -- ADMIN -> users::write
       (2, 1),  -- STUDENT -> students::read
       (2, 3),  -- STUDENT -> lessons::read
       (2, 5),  -- STUDENT -> rooms::read
       (2, 7),  -- STUDENT -> courses::read
       (2, 9),  -- STUDENT -> professors::read
       (2, 11), -- STUDENT -> groups::read
       (3, 1),  -- PROFESSOR -> students::read
       (3, 3),  -- PROFESSOR -> lessons::read
       (3, 5),  -- PROFESSOR -> rooms::read
       (3, 7),  -- PROFESSOR -> courses::read
       (3, 9),  -- PROFESSOR -> professors::read
       (3, 11); -- PROFESSOR -> groups::read

-- add admin user with 'admin' password
INSERT INTO users (first_name, last_name, email, group_id, user_type, password, enabled)
VALUES ('admin', 'admin', 'admin@admin.com', null, null,
        '{bcrypt}$2a$12$x/X094gfH8YLGOw1dHrq9OM6v2T5K0EaQjuT3RxCVgeBqhifc/Jo6', true);

-- assign roles to users
-- if user_type='student' > assign ROLE_STUDENT
-- if user_type='professor' > assign ROLE_PROFESSOR
-- if user_type is null > assign ROLE_ADMIN
INSERT INTO users_roles
SELECT u.id as user_id, (SELECT id from roles WHERE name = 'ROLE_STUDENT') as role_id
from users u
where u.user_type = 'student'
UNION
SELECT u.id as user_id, (SELECT id from roles WHERE name = 'ROLE_PROFESSOR') as role_id
from users u
where u.user_type = 'professor'
UNION
SELECT u.id as user_id, (SELECT id from roles WHERE name = 'ROLE_ADMIN') as role_id
from users u
where u.user_type IS NULL;


COMMIT;