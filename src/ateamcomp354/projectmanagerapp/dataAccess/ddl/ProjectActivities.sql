CREATE TABLE projectActivities (
	project_activities_id INTEGER REFERENCES project (project_activities_id),
	project_id INTEGER REFERENCES project (id),
	activity_id INTEGER REFERENCES activity (id)
);