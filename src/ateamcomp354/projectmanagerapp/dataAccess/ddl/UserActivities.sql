CREATE TABLE userActivities (
	id INTEGER PRIMARY KEY, -- a record if for jooq code generation
	activity_id INTEGER REFERENCES activity (id),
	user_id INTEGER REFERENCES users (id),
	UNIQUE ( activity_id, user_id )
);