CREATE TABLE "users" (
                id SERIAL PRIMARY KEY,
                username VARCHAR(255) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL
);

CREATE TABLE "user_roles" (
user_id INTEGER NOT NULL,
role VARCHAR(255) NOT NULL,
CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "users" (id) ON DELETE CASCADE
);