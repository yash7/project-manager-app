package ateamcomp354.projectmanagerapp.testing;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.ui.util.Charts;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.fail;

public class ActivityOnNodeGraphUITest extends AbstractDatabaseTest {

    @Test
    public void testExample() {

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());

        // setup a project scenario to test, ex:
        // project with no activities,
        // project with simple critical path
        // project with many critical paths
        // project with critical path and many other activities with float > 0
        // ...

        Project p = appCtx.getProjectService().getProject(0);
        Charts.viewCriticalPathsChart(appCtx, p);

        int r = JOptionPane.showConfirmDialog(null, "Did the chart display properly?", "testExample", JOptionPane.YES_NO_OPTION);

        if ( r != JOptionPane.YES_OPTION ) {
            fail("Chart for testExample did not display properly.");
        }
    }
}
