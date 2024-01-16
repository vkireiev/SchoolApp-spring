TRUNCATE student_course;
TRUNCATE students RESTART IDENTITY CASCADE;
TRUNCATE courses RESTART IDENTITY CASCADE;
TRUNCATE groups RESTART IDENTITY CASCADE;
INSERT INTO groups(id, name) VALUES (1, 'EM-32');
INSERT INTO groups(id, name) VALUES (2, 'UJ-33');
INSERT INTO groups(id, name) VALUES (3, 'FE-22');
INSERT INTO groups(id, name) VALUES (4, 'NU-88');
INSERT INTO groups(id, name) VALUES (5, 'CI-37');
INSERT INTO groups(id, name) VALUES (6, 'AR-49');
INSERT INTO groups(id, name) VALUES (7, 'KK-73');
INSERT INTO groups(id, name) VALUES (8, 'QR-95');
INSERT INTO groups(id, name) VALUES (9, 'RI-47');
INSERT INTO groups(id, name) VALUES (10, 'IC-83');
ALTER SEQUENCE groups_id_seq RESTART WITH 25;
INSERT INTO students(id, group_id, last_name, first_name)  VALUES (1, 2, 'Henris', 'Mia');
INSERT INTO students(id, group_id, last_name, first_name)  VALUES (2, null, 'Green', 'Victoria');
INSERT INTO students(id, group_id, last_name, first_name)  VALUES (3, 7, 'Hoig', 'Adrian');
INSERT INTO students(id, group_id, last_name, first_name)  VALUES (4, 1, 'Johnson', 'Jack');
INSERT INTO students(id, group_id, last_name, first_name)  VALUES (5, 3, 'Walker', 'Logan');
ALTER SEQUENCE students_id_seq RESTART WITH 25;