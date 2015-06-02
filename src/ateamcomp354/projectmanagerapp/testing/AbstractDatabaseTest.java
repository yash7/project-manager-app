package ateamcomp354.projectmanagerapp.testing;

import ateamcomp354.projectmanagerapp.dataAccess.DatabaseManager;
import org.junit.After;
import org.junit.Before;

/**
 * All tests can extend this class to have a DatabaseManager instance whose lifecycle is managed for them.
 *
 * Simply use "db" instance variable in the subclass's test to access the database.
 */
public abstract class AbstractDatabaseTest {

    protected DatabaseManager db;

    @Before
    public void setup() {
        db = new DatabaseManager(":memory:");
    }

    @After
    public void tearDown() {
        db.closeConnection();
        db = null;
    }
}
