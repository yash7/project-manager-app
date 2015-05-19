CREATE TABLE memberList (
	member_list_id INTEGER REFERENCES project (member_list_id),
	user_id INTEGER REFERENCES users (id)
);