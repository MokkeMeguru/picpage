CREATE TABLE user_tokens (
  id SERIAL PRIMARY KEY,
  user_id integer REFERENCES users(id),
  token VARCHAR(255) NOT NULL,
  created_at TIMESTAMP default CURRENT_TIMESTAMP
);
