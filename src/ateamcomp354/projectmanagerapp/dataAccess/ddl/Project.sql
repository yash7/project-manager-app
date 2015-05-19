CREATE TABLE project (
	id INTEGER PRIMARY KEY,
	project_name TEXT,
	project_manager_id INTEGER REFERENCES users (id),
	member_list_id INTEGER,
	project_activities_id INTEGER
);