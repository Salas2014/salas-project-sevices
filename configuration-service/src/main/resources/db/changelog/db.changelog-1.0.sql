--liquibase formatted sql

--changeset hasporian:1
CREATE TABLE IF NOT EXISTS users
(
    id
    SERIAL
    PRIMARY
    KEY,
    name
    VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE
    )

--changeset hasporian:2
ALTER TABLE users
    ADD COLUMN phone BIGINT;

--changeset hasporian:3
INSERT INTO users (name, email, phone)
VALUES ('Alice', 'alice@example.com', 380991112233),
       ('Bob', 'bob@example.com', 380992223344),
       ('Charlie', 'charlie@example.com', 380993334455),
       ('Diana', 'diana@example.com', 380994445566),
       ('Ethan', 'ethan@example.com', 380995556677),
       ('Fiona', 'fiona@example.com', 380996667788),
       ('George', 'george@example.com', 380997778899),
       ('Hannah', 'hannah@example.com', 380998889900),
       ('Ivan', 'ivan@example.com', 380999990011),
       ('Julia', 'julia@example.com', 380990001122);
