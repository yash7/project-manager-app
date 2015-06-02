-- This table represents edges in a graph of activities.
CREATE TABLE activityLinks (
    id INTEGER PRIMARY KEY, -- a record id for jooq code generation
    from_activity_id INTEGER REFERENCES activity (id) ON DELETE CASCADE,
    to_activity_id INTEGER REFERENCES activity (id) ON DELETE CASCADE,
    UNIQUE( from_activity_id, to_activity_id )
);