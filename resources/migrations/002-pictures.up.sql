CREATE TABLE pictures (
  id SERIAL PRIMARY KEY,
  user_id integer REFERENCES users(id),
  type VARCHAR(20) NOT NULL,
  title VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  path VARCHAR(255) NOT NULL UNIQUE,
  is_deleted BOOLEAN NOT NULL default FALSE,
  UNIQUE (user_id, title)
);
