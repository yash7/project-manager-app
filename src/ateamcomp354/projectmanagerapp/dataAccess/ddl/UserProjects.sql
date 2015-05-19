CREATE TABLE userProjects (
	user_id INTEGER REFERENCES users (id),
	project_id INTEGER REFERENCES project (id)
);