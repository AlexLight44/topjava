TRUNCATE TABLE meals, user_role, users RESTART IDENTITY CASCADE;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2025-05-30 10:00:00', 'Завтрак', 500),
       (100000, '2025-05-30 13:00:00', 'Обед', 1000),
       (100001, '2025-05-30 09:30:00', 'Кофе', 300);
