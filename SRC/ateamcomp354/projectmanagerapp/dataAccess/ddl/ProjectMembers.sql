CREATE TABLE projectMembers (
	id INTEGER PRIMARY KEY, -- a record if for jooq code generation
	project_id INTEGER REFERENCES  project (id) ON DELETE CASCADE,
	user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
	UNIQUE ( project_id, user_id )
);