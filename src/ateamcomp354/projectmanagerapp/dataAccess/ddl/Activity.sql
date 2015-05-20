CREATE TABLE activity (
	id INTEGER PRIMARY KEY,
	project_id INTEGER REFERENCES project (id),
	earliest_start INTEGER,
	earliest_finish INTEGER,
	label TEXT,
	latest_start INTEGER,
	latest_finish INTEGER,
	duration INTEGER,
	max_duration INTEGER,
	description TEXT
);