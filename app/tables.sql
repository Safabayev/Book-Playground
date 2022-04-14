CREATE TYPE GENDER AS ENUM ('male', 'female');
CREATE TYPE ROLE AS ENUM ('admin', 'user');

CREATE TABLE IF NOT EXISTS stadiums (
  uuid UUID PRIMARY KEY,
  address VARCHAR NOT NULL,
  owner VARCHAR NOT NULL,
  tel VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS teams (
  uuid UUID PRIMARY KEY,
  name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS matches (
  uuid UUID PRIMARY KEY,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  stadium_id UUID NOT NULL CONSTRAINT fk_stadiums_id REFERENCES stadiums (uuid) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS users (
  uuid UUID PRIMARY KEY,
  name VARCHAR NOT NULL,
  email VARCHAR UNIQUE NOT NULL,
  gender GENDER NOT NULL,
  password VARCHAR NOT NULL,
  role ROLE NOT NULL DEFAULT 'user'
);

CREATE TABLE IF NOT EXISTS player_teams (
  player_id UUID NOT NULL CONSTRAINT fk_user_id REFERENCES users (uuid) ON UPDATE NO ACTION ON DELETE NO ACTION,
  team_id UUID NOT NULL CONSTRAINT fk_teams_id REFERENCES teams (uuid) ON UPDATE NO ACTION ON DELETE NO ACTION
);