CREATE TABLE users (
	id INTEGER PRIMARY KEY,  
	first_name TEXT NOT NULL, 
	last_name TEXT NOT NULL, 
	username TEXT UNIQUE NOT NULL,
	password TEXT NOT NULL,
	manager_role INTEGER DEFAULT 0
);
