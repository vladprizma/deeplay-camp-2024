CREATE TABLE "users" (
     "id" SERIAL PRIMARY KEY,
     "username" VARCHAR(50) UNIQUE NOT NULL,
     "password" VARCHAR(255) NOT NULL,
     "userPhoto" VARCHAR(255) NOT NULL,
     "rating" INT DEFAULT 0,
     "matches" INT DEFAULT 0
);

CREATE TABLE "gamesessions" (
    "id" SERIAL PRIMARY KEY,
    "player1_id" INT,
    "result" VARCHAR(30) NOT NULL,
    "log" TEXT,
    "player2_id" INT,
    "session_chat" TEXT
);

CREATE TABLE "chat_messages" (
    "id" SERIAL PRIMARY KEY,
    "user_id" INT,
    "message" TEXT,
    "timestamp" TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "tokens" (
    "id" SERIAL PRIMARY KEY,
    "user_id" INT UNIQUE,
    "refresh_token" VARCHAR(255) UNIQUE NOT NULL,
    "update_token" VARCHAR(255) UNIQUE NOT NULL
);

ALTER TABLE "gamesessions" ADD FOREIGN KEY ("player1_id") REFERENCES "users" ("id");

ALTER TABLE "gamesessions" ADD FOREIGN KEY ("player2_id") REFERENCES "users" ("id");

ALTER TABLE "chat_messages" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "tokens" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
