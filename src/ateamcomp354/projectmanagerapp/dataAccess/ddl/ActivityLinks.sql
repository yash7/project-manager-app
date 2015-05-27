-- This table represents edges in a graph of activities.
CREATE TABLE activityLinks (
    from_activity_id INTEGER REFERENCES activity (id),
    to_activity_id INTEGER REFERENCES activity (id)
);