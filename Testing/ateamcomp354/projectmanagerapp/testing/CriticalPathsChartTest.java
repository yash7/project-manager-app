package ateamcomp354.projectmanagerapp.testing;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.ui.util.Charts;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.junit.Test;

import javax.swing.*;

import java.util.stream.Stream;

import static org.junit.Assert.fail;
import static ateamcomp354.projectmanagerapp.testing.util.PojoMaker.*;

public class CriticalPathsChartTest extends AbstractDatabaseTest {

    @Test
    public void testProjectWithNoActivities() {

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService =  appCtx.getProjectService();

        Project p = new Project(0, "No activities project", "This project has no activities", false, 10000);
        projectService.addProject( p );

        Charts.viewCriticalPathsChart(appCtx, p);

        assertDisplayedProperly("testProjectWithNoActivities", "Expected a popup that says \"Need activities in the project ...\"");
    }

    /**
     * Test that activity dependencies such as
     *
     * A -> B -> C -> ... -> N
     *
     * displays properly.
     */
    @Test
    public void testProjectWithStraightLineActivities() {

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService =  appCtx.getProjectService();

        Project p = new Project(0, "Straight line activities", "This project has a straight line of dependencies", false, 10000);
        projectService.addProject( p );

        ActivityService activityService = appCtx.getActivityService( p.getId() );

        Activity A = makeActivity(0, "A", makeDate(2015, 1, 1), makeDate(2015, 1, 2));
        Activity B = makeActivity(0, "B", makeDate(2015, 1, 2), makeDate(2015, 1, 5));
        Activity C = makeActivity(0, "C", makeDate(2015, 1, 5), makeDate(2015, 1, 6));
        Activity D = makeActivity(0, "D", makeDate(2015, 1, 6), makeDate(2015, 1, 8));

        Stream.of(A, B, C, D).forEach(activityService::addActivity);

        activityService.addDependency(A.getId(), B.getId());
        activityService.addDependency(B.getId(), C.getId());
        activityService.addDependency(C.getId(), D.getId());

        Charts.viewCriticalPathsChart(appCtx, p);

        assertDisplayedProperly("testProjectWithStraightLineActivities", "Expected chart A -> B -> C -> D");
    }

    /**
     * Test that 2 critical paths display properly
     *
     *  / B \
     * A     -> D
     *  \ C /
     *
     * Both paths A,B,D and A,C,D will have 0 float
     */
    @Test
    public void testProjectWith2CriticalPaths() {

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService =  appCtx.getProjectService();

        Project p = new Project(0, "Two critical paths", "This project has 2 critical paths", false, 10000);
        projectService.addProject( p );

        ActivityService activityService = appCtx.getActivityService( p.getId() );

        Activity A = makeActivity(0, "A", makeDate(2015, 1, 1), makeDate(2015, 1, 2));
        Activity B = makeActivity(0, "B", makeDate(2015, 1, 2), makeDate(2015, 1, 5));
        Activity C = makeActivity(0, "C", makeDate(2015, 1, 2), makeDate(2015, 1, 5));
        Activity D = makeActivity(0, "D", makeDate(2015, 1, 5), makeDate(2015, 1, 8));

        Stream.of(A, B, C, D).forEach(activityService::addActivity);

        activityService.addDependency(A.getId(), B.getId());
        activityService.addDependency(A.getId(), C.getId());
        activityService.addDependency(B.getId(), D.getId());
        activityService.addDependency(C.getId(), D.getId());

        Charts.viewCriticalPathsChart(appCtx, p);

        assertDisplayedProperly("testProjectWith2CriticalPaths", "Expected 2 critical paths, looking like a diamond");
    }

    /**
     * Test that branches display properly when there is 1 critical path.
     *
     *  / B \      / E \
     * A     -> D -     -> G
     *  \ C /      \ F /
     */
    @Test
    public void testProjectWithBranches() {

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService =  appCtx.getProjectService();

        Project p = new Project(0, "", "", false, 10000);
        projectService.addProject( p );

        ActivityService activityService = appCtx.getActivityService( p.getId() );

        Activity A = makeActivity(0, "A", makeDate(2015, 1, 1), makeDate(2015, 1, 2));
        Activity B = makeActivity(0, "B", makeDate(2015, 1, 2), makeDate(2015, 1, 7));
        Activity C = makeActivity(0, "C", makeDate(2015, 1, 2), makeDate(2015, 1, 6));
        Activity D = makeActivity(0, "D", makeDate(2015, 1, 7), makeDate(2015, 1, 10));
        Activity E = makeActivity(0, "E", makeDate(2015, 1, 10), makeDate(2015, 1, 17));
        Activity F = makeActivity(0, "F", makeDate(2015, 1, 10), makeDate(2015, 1, 14));
        Activity G = makeActivity(0, "G", makeDate(2015, 1, 17), makeDate(2015, 1, 23));

        Stream.of(A, B, C, D, E, F, G).forEach(activityService::addActivity);

        activityService.addDependency(A.getId(), B.getId());
        activityService.addDependency(A.getId(), C.getId());
        activityService.addDependency(B.getId(), D.getId());
        activityService.addDependency(C.getId(), D.getId());
        activityService.addDependency(D.getId(), E.getId());
        activityService.addDependency(D.getId(), F.getId());
        activityService.addDependency(E.getId(), G.getId());
        activityService.addDependency(F.getId(), G.getId());

        Charts.viewCriticalPathsChart(appCtx, p);

        assertDisplayedProperly("testProjectWithBranches", "");
    }

    private void assertDisplayedProperly(String testName, String expectedDisplay) {

        int r = JOptionPane.showConfirmDialog(
                null,
                expectedDisplay +
                "\n" +
                "Did the chart display properly?",
                testName,
                JOptionPane.YES_NO_OPTION
        );

        if ( r != JOptionPane.YES_OPTION ) {
            fail("Chart for testExample did not display properly.");
        }
    }
}
