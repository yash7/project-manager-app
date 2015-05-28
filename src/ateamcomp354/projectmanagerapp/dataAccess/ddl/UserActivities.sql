CREATE TABLE userActivities (
	id INTEGER PRIMARY KEY, -- a record if for jooq code generation
	activity_id INTEGER REFERENCES activity (id) ON DELETE CASCADE,
	user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
	UNIQUE ( activity_id, user_id )
);