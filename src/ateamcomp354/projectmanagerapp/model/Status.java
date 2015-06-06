package ateamcomp354.projectmanagerapp.model;

/**
 * The status of an activity. The value in the database is the ordinal of a status enum.
 */
public enum Status {

    NEW( "Open" ),
    IN_PROGRESS( "In Progress" ),
    RESOLVED( "Resolved" );

    private final String pretty;

    Status(String pretty) {
        this.pretty = pretty;
    }

    public String getPrettyString() {
        return pretty;
    }
}
