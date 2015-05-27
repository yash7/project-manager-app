package ateamcomp354.projectmanagerapp.testing;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.dataAccess.DatabaseManager;
import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class ProjectServiceTest {

    /**
     * Just to make sure the JOOQ select statements work as expected.
     */
    @Test
    public void testGetProjectCompletion_DoesItWork() throws Exception {

        final int projectId = 0;

        File file = Files.createTempFile( "test", "db" ).toFile();
        file.deleteOnExit();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        ActivityService activityService = appCtx.getActivityService( projectId );

        Project p = new Project();
        p.setId( projectId );
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject( p );

        Activity a1 = new Activity();
        a1.setProjectId( projectId );
        a1.setLabel( "a1" );

        Activity a2 = new Activity();
        a2.setProjectId( projectId );
        a2.setLabel( "a2" );
        a2.setStatus( Status.RESOLVED );

        activityService.addActivity( a1 );
        activityService.addActivity( a2 );

        assertEquals(
                50,
                projectService.getProjectCompletion( projectId )
        );
    }
}
