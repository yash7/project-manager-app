CREATE TABLE activity (
	id INTEGER PRIMARY KEY,
	project_id INTEGER REFERENCES project (id) ON DELETE CASCADE,
	status INTEGER,
	earliest_start INTEGER,
	earliest_finish INTEGER,
	label TEXT,
	latest_start INTEGER,
	latest_finish INTEGER,
	duration INTEGER,
	max_duration INTEGER,
	float INTEGER,
	description TEXT,
	planned_value INTEGER,
	actual_cost INTEGER
);