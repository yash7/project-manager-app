package ateamcomp354.projectmanagerapp.testing;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.dataAccess.DatabaseManager;
import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class ProjectServiceTest {

	@Test
	public void testGetProjects_noProjects() throws Exception {
		
        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        
        List<Project> projectList = projectService.getProjects();
        assertEquals(0, projectList.size());
	}
	
	@Test
	public void testGetProjects_addedProject() throws Exception {
		
        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = new Project();
        p.setId( 0 );
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject( p );
        
        List<Project> projectList = projectService.getProjects();
        assertEquals(1, projectList.size());
	}
	
	@Test
	public void testGetProject_noProjects() throws Exception {
		
        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = projectService.getProject(0);
        
        assertNull(p);
	}
	
	@Test
	public void testGetProject_projectExists() throws Exception {
		
        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = new Project();
        p.setId(0);
        p.setProjectName("Test project");
        p.setDescription("Test project");
        
        projectService.addProject( p );

        Project test = projectService.getProject(0);
        
        assertNotNull(test);
        assertEquals("Test project", test.getProjectName());
        assertEquals("Test project", test.getDescription());
        assertEquals((int)test.getId(), 0);
	}
	
    @Test
    public void testGetProjectCompletion_halfComplete() throws Exception {

        final int projectId = 0;

        File file = Files.createTempFile( "test", "db" ).toFile();

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
        
        file.delete();
    }
    
    @Test
    public void testGetProjectCompletion_zeroComplete() throws Exception {

        final int projectId = 0;

        File file = Files.createTempFile( "test", "db" ).toFile();

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

        activityService.addActivity( a1 );
        activityService.addActivity( a2 );

        assertEquals(
                0,
                projectService.getProjectCompletion( projectId )
        );
        
        file.delete();
    }
    
    @Test
    public void testGetProjectCompletion_noActivityZeroComplete() throws Exception {

        final int projectId = 0;

        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        ActivityService activityService = appCtx.getActivityService( projectId );

        Project p = new Project();
        p.setId( projectId );
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject( p );

        assertEquals(
                0,
                projectService.getProjectCompletion( projectId )
        );
        
        file.delete();
    }
    
    @Test
    public void testGetProjectCompletion_complete() throws Exception {

        final int projectId = 0;

        File file = Files.createTempFile( "test", "db" ).toFile();

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
        a1.setStatus(Status.RESOLVED);

        Activity a2 = new Activity();
        a2.setProjectId( projectId );
        a2.setLabel( "a2" );
        a2.setStatus(Status.RESOLVED);

        activityService.addActivity( a1 );
        activityService.addActivity( a2 );

        assertEquals(
                100,
                projectService.getProjectCompletion( projectId )
        );
        
        file.delete();
    }
    
	@Test
	public void testAddProject_nullProject() throws Exception {
		
        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        
        Project test = null;
        
        projectService.addProject( test );
        
        
        assertEquals(0, projectService.getProjects().size());

        assertNull(test);
	}
	
	@Test
	public void testAddProject_newProject() throws Exception {
		
        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = new Project();
        p.setId(0);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        
        projectService.addProject(p);
        
        assertEquals(1, projectService.getProjects().size());
	}
	
	@Test (expected = ServiceFunctionalityException.class)
	public void testAddProject_duplicateProjects() throws Exception {
		
        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = new Project();
        p.setId(0);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        
        projectService.addProject(p);
        
        Project p2 = new Project();
        p2.setId(0);
        p2.setProjectName("Test project 2");
        p2.setDescription("Test project 2");

        projectService.addProject(p2);
	}
	
	@Test
	public void testDeleteProject_noProductExistsWithID() throws Exception {
		
        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        
        projectService.deleteProject(0);
        
	}
	
	@Test
	public void testDeleteProject_projectExists() throws Exception {
		
        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = new Project();
        p.setId(0);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);
        
        assertEquals(1, projectService.getProjects().size());
        
        projectService.deleteProject(0);
        
        assertEquals(0, projectService.getProjects().size());
        
	}
	
    @Test
    public void testDeleteProject_activitiesDeleted() throws Exception {

        final int projectId = 0;

        File file = Files.createTempFile( "test", "db" ).toFile();

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

        Activity a2 = new Activity();
        a2.setProjectId( projectId );

        activityService.addActivity( a1 );
        activityService.addActivity( a2 );
        
        List<Activity> activityList = activityService.getActivities();
        
        for (Activity a : activityList) {
        	activityService.deleteActivity(a.getId());
        }
        
        projectService.deleteProject(0);
        
        file.delete();
    }
    
    @Test 
    public void testDeleteProject_activitiesNotDeleted() throws Exception {

        final int projectId = 0;

        File file = Files.createTempFile( "test", "db" ).toFile();

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

        Activity a2 = new Activity();
        a2.setProjectId( projectId );

        activityService.addActivity( a1 );
        activityService.addActivity( a2 );
        
        projectService.deleteProject(0);
        
        file.delete();
    }
	
	@Test
	public void testUpdateProject_productExists() throws Exception {
		
        File file = Files.createTempFile( "test", "db" ).toFile();

        DatabaseManager db = new DatabaseManager( file.getName() );
        ApplicationContext appCtx = App.getApplicationContext( db.getOpenConnection() );
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = new Project();
        p.setId(0);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);
        
        Project test = projectService.getProject(0);
        
        test.setDescription("New description");
        test.setProjectName("New project name");
        
        projectService.updateProject(test);
        
        Project finalTest = projectService.getProject(0);
        
        assertEquals(0, (int)finalTest.getId());
        assertEquals("New description", finalTest.getDescription());
        assertEquals("New project name", finalTest.getProjectName());
	}
}
