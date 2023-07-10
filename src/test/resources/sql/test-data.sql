INSERT INTO groups (group_name)
VALUES ('GR-1'),
       ('GR-2'),
       ('GR-3');

INSERT INTO users (first_name, last_name, email, group_id, user_type)
VALUES ('John', 'Doe', 'john.doe@example.com', 1, 'student'),
       ('Jane', 'Smith', 'jane.smith@example.com', 1, 'student'),
       ('Robert', 'Johnson', 'robert.johnson@example.com', 2, 'student'),
       ('Michael', 'Williams', 'michael.williams@example.com', null, 'professor'),
       ('John', 'Irvins', 'john.Irvins@example.com', null, 'professor'),
       ('Emily', 'Davis', 'emily.davis@example.com', 3, 'student');

INSERT INTO courses (course_name, course_description)
VALUES ('Mathematics', 'Introduction to mathematics'),
       ('English', 'Basic English grammar'),
       ('Physics', 'Fundamental principles of physics');

INSERT INTO rooms (room_code)
VALUES ('R001'),
       ('R002'),
       ('R003');

INSERT INTO lessons (course_id, group_id, professor_id, room_id, day_of_week, week_number, lesson_order, lesson_type)
VALUES (1, 1, 4, 1, 'MONDAY', 1, 1, 'LECTURE'),
       (1, 1, 4, 1, 'WEDNESDAY', 1, 2, 'SEMINAR'),
       (2, 1, 4, 2, 'TUESDAY', 4, 3, 'LECTURE'),
       (2, 1, 4, 2, 'THURSDAY', 1, 2, 'SEMINAR'),
       (1, 2, 5, 3, 'MONDAY', 1, 1, 'LECTURE'),
       (1, 2, 4, 1, 'WEDNESDAY', 1, 4, 'SEMINAR'),
       (2, 2, 4, 2, 'TUESDAY', 1, 1, 'LECTURE'),
       (2, 2, 4, 2, 'THURSDAY', 1, 4, 'SEMINAR'),
       (3, 3, 4, 3, 'FRIDAY', 1, 1, 'LECTURE'),
       (3, 3, 4, 3, 'SUNDAY', 1, 2, 'SEMINAR');

INSERT INTO users_courses (user_id, course_id)
VALUES (1, 1),
       (2, 1),
       (1, 2),
       (3, 2),
       (5, 3);