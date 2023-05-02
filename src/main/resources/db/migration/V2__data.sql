INSERT INTO groups (group_name)
VALUES ('GR-1'),
       ('GR-2'),
       ('GR-3'),
       ('GR-4'),
       ('GR-5'),
       ('GR-6'),
       ('GR-7'),
       ('GR-8'),
       ('GR-9'),
       ('GR-10');

INSERT INTO rooms (room_code)
VALUES ('R101'),
       ('R102'),
       ('R103'),
       ('R104'),
       ('R105'),
       ('R106'),
       ('R107'),
       ('R108'),
       ('R109'),
       ('R110');

INSERT INTO users (first_name, last_name, email, group_id, user_type)
VALUES ('Maison', 'Moses', 'Maison.Moses@test.com', 1, 'student'),
       ('Nieve', 'Brady', 'Nieve.Brady@test.com', 2, 'student'),
       ('Jana', 'Guerra', 'Jana.Guerra@test.com', 3, 'student'),
       ('Stanley', 'Decker', 'Stanley.Decker@test.com', 4, 'student'),
       ('Aleksander', 'Fitzpatrick', 'Aleksander.Fitzpatrick@test.com', 5, 'student'),
       ('Aryan', 'Stephens', 'Aryan.Stephens@test.com', 5, 'student'),
       ('Alicia', 'Bloggs', 'Alicia.Bloggs@test.com', 2, 'student'),
       ('John', 'Doe', 'john.doe@test.com', null, 'professor'),
       ('Damien', 'Callahan', 'Damien.Callahan@test.com', null, 'professor'),
       ('Jayden', 'Larson', 'Jayden.Larson@test.com', null, 'professor'),
       ('Irvin', 'Arias', 'Irvin.Arias@test.com', null, 'professor'),
       ('Michael', 'Blankenship', 'Michael.Blankenship@test.com', null, 'professor'),
       ('Jeffrey', 'Meyers', 'Jeffrey.Meyers@test.com', null, 'professor'),
       ('Christen', 'Goodwin', 'Christen.Goodwin@test.com', null, 'professor');

INSERT INTO courses (course_name, course_description, user_id)
VALUES ('Math', 'Math Description', 8),
       ('Biology', 'Biology Description', 9),
       ('Programming', 'Programming Description', 10),
       ('System Design', 'System Design Description', 11),
       ('Philosophy', 'Philosophy Description', 12),
       ('Game Design', 'Game Design Description', 13),
       ('Software Testing', 'Software Testing Description', 14);

INSERT INTO users_courses (user_id, course_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 3),
       (4, 4),
       (5, 6),
       (5, 7),
       (6, 6),
       (7, 7),
       (3, 1),
       (4, 5),
       (3, 6),
       (6, 3),
       (7, 2),
       (7, 5),
       (1, 4),
       (2, 1),
       (2, 6),
       (6, 2);

INSERT INTO lessons (course_id, user_id, room_id, day_of_week, week_number, lesson_order, lesson_type)
VALUES (1, 8, 1, 'TUESDAY', 13, 1, 'LECTURE'),
       (2, 9, 2, 'WEDNESDAY', 23, 1, 'LECTURE'),
       (3, 10, 3, 'FRIDAY', 1, 1, 'LECTURE'),
       (4, 11, 4, 'FRIDAY', 5, 1, 'SEMINAR'),
       (5, 12, 5, 'MONDAY', 23, 1, 'LECTURE'),
       (6, 13, 6, 'TUESDAY', 15, 1, 'LECTURE'),
       (7, 14, 1, 'WEDNESDAY', 7, 1, 'LECTURE'),
       (5, 8, 2, 'THURSDAY', 2, 1, 'SEMINAR'),
       (1, 9, 3, 'FRIDAY', 28, 1, 'LECTURE'),
       (2, 10, 4, 'MONDAY', 15, 1, 'LECTURE'),
       (3, 11, 5, 'TUESDAY', 9, 1, 'SEMINAR'),
       (4, 14, 6, 'WEDNESDAY', 4, 1, 'LECTURE');


INSERT INTO users_lessons (user_id, lesson_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 2),
       (5, 2),
       (6, 3),
       (7, 4);
