CREATE TABLE project (
	id INTEGER PRIMARY KEY,
	project_name TEXT,
	description TEXT,
	completed INTEGER DEFAULT 0,
	budget_at_completion INTEGER DEFAULT 0,
	actual_cost_at_completion INTEGER DEFAULT 0
);