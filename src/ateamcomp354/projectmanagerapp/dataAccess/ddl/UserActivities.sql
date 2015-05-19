CREATE TABLE userActivities (
	id INTEGER PRIMARY KEY,
	user_id INTEGER REFERENCES users (id)
);