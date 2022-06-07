CREATE TYPE ROLE AS ENUM ('admin', 'client', 'user');

CREATE TABLE IF NOT EXISTS users
(
    uuid     UUID PRIMARY KEY,
    name     VARCHAR        NOT NULL,
    email    VARCHAR UNIQUE NOT NULL,
    phone    VARCHAR UNIQUE NOT NULL,
    password VARCHAR        NOT NULL,
    role     ROLE           NOT NULL DEFAULT 'user'
);

CREATE TABLE IF NOT EXISTS stadiums
(
    uuid    UUID PRIMARY KEY,
    address VARCHAR NOT NULL,
    owner   VARCHAR NOT NULL,
    tel     VARCHAR NOT NULL,
    price   NUMERIC NOT NULL
);

CREATE TABLE IF NOT EXISTS matches
(
    uuid       UUID PRIMARY KEY,
    user_id     UUID      NOT NULL
        CONSTRAINT fk_user_id REFERENCES users (uuid) ON UPDATE NO ACTION ON DELETE NO ACTION,
    stadium_id UUID      NOT NULL
        CONSTRAINT fk_stadiums_id REFERENCES stadiums (uuid) ON UPDATE NO ACTION ON DELETE NO ACTION,
    start_time TIMESTAMP NOT NULL,
    end_time   TIMESTAMP NOT NULL

);
