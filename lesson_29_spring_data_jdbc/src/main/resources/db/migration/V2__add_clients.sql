-- Добавляет начальные записи

INSERT INTO web_server_clients (name, login, pwd)
VALUES ("first", "user1", "111")
    ON CONFLICT (nickname) DO NOTHING;

INSERT INTO web_server_clients (name, login, pwd)
VALUES ("second", "user2", "111")
    ON CONFLICT (nickname) DO NOTHING;

INSERT INTO web_server_clients (name, login, pwd)
VALUES ("third", "user3", "111")
    ON CONFLICT (nickname) DO NOTHING;