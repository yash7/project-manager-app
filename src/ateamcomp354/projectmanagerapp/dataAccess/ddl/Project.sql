CREATE TABLE project (
	id INTEGER PRIMARY KEY,
	project_name TEXT,
	project_manager_id INTEGER REFERENCES users (id),
	description TEXT,
	completed INTEGER DEFAULT 0
);