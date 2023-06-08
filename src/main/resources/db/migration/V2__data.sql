-- Insert test data into groups table
INSERT INTO groups (group_name)
VALUES ('Group A'),
       ('Group B'),
       ('Group C'),
       ('Group D'),
       ('Group E');

-- Insert test data into users table
INSERT INTO users (first_name, last_name, email, group_id, user_type)
VALUES ('John', 'Doe', 'john.doe@example.com', 1, 'Student'),
       ('Jane', 'Smith', 'jane.smith@example.com', null, 'Teacher'),
       ('Bob', 'Johnson', 'bob.johnson@example.com', 1, 'Student'),
       ('Alice', 'Brown', 'alice.brown@example.com', 3, 'Student'),
       ('Michael', 'Wilson', 'michael.wilson@example.com', null, 'Teacher'),
       ('Emily', 'Taylor', 'emily.taylor@example.com', 2, 'Student'),
       ('David', 'Anderson', 'david.anderson@example.com', 5, 'Student'),
       ('Sarah', 'Clark', 'sarah.clark@example.com', null, 'Teacher'),
       ('Daniel', 'Walker', 'daniel.walker@example.com', 3, 'Student'),
       ('Olivia', 'Harris', 'olivia.harris@example.com', 4, 'Student'),
       ('James', 'Lewis', 'james.lewis@example.com', null, 'Teacher'),
       ('Sophia', 'Martin', 'sophia.martin@example.com', 4, 'Student'),
       ('William', 'Lee', 'william.lee@example.com', 2, 'Student'),
       ('Emma', 'Hall', 'emma.hall@example.com', null, 'Teacher'),
       ('Ryan', 'Green', 'ryan.green@example.com', 5, 'Student'),
       ('Grace', 'Adams', 'grace.adams@example.com', 1, 'Student'),
       ('Andrew', 'Baker', 'andrew.baker@example.com', null, 'Teacher'),
       ('Mia', 'Turner', 'mia.turner@example.com', 2, 'Student'),
       ('Benjamin', 'Parker', 'benjamin.parker@example.com', 3, 'Student'),
       ('Ava', 'Young', 'ava.young@example.com', null, 'Teacher'),
       ('Henry', 'Kelly', 'henry.kelly@example.com', 4, 'Student'),
       ('Elizabeth', 'Campbell', 'elizabeth.campbell@example.com', 4, 'Student'),
       ('Christopher', 'Mitchell', 'christopher.mitchell@example.com', null, 'Teacher'),
       ('Abigail', 'Price', 'abigail.price@example.com', 1, 'Student');

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

INSERT INTO courses (course_name, course_description)
VALUES ('Math', 'Math Description'),
       ('Biology', 'Biology Description'),
       ('Programming', 'Programming Description'),
       ('System Design', 'System Design Description'),
       ('Philosophy', 'Philosophy Description'),
       ('Game Design', 'Game Design Description'),
       ('Software Testing', 'Software Testing Description');

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
       (6, 2),
       (8, 2);

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
