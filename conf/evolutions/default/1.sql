create schema public;
create sequence hibernate_sequence;

CREATE TABLE persons (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  name varchar(255) NOT NULL
);

