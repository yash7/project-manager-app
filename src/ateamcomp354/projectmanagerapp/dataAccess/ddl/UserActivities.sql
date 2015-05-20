CREATE TABLE userActivities (
	activity_id INTEGER REFERENCES activity (id),
	user_id INTEGER REFERENCES users (id)
);