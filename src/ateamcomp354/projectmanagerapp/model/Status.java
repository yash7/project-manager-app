package ateamcomp354.projectmanagerapp.model;

/**
 * The status of an activity. The value in the database is the ordinal of a status enum.
 * If an invalid value is given then it shoudl be considered as the default value ( ordinal 0 )
 */
public enum Status {

    NEW,
    IN_PROGRESS,
    RESOLVED;

    public Status tryFromOrdinal( int ordinal ) {

        Status statuses[] = Status.values();

        int sanitizedOrdinal = ordinal >= 0 && ordinal < statuses.length ? ordinal : 0;

        return statuses[sanitizedOrdinal];
    }
}
