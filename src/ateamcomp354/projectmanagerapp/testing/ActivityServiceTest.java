package ateamcomp354.projectmanagerapp.testing;

/**
 * Takes care of all database access
 * 
 * @author Brendan Blencowe (26985455)
 */

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.exception.DataAccessException;
import org.junit.Before;
import org.junit.Test;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.dataAccess.DatabaseManager;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;

public class ActivityServiceTest {

	private ActivityService ase;
	private ProjectService pjs;
	private DatabaseManager db;
	private ApplicationContext appCtx;
	
	
	@Test
	public void testAddActivitySuccess() throws Exception {
		
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		List<Activity> acts = ase.getActivities();
		
		assertEquals(1, acts.size());
		
	}
	
	
	@Test
	public void testGetActivities_noActivities() throws Exception  {
		assertEquals(0, ase.getActivities().size());
	}
	
	@Test
	public void testGetActivities() throws Exception  {
        
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		a = new Activity();
		a.setId(1);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		List<Activity> acts = ase.getActivities();
		
		assertEquals(2, acts.size());
	}
	
	@Test
	public void testGetProject() throws Exception  {
		
		assertEquals("Test Project", ase.getProject().getProjectName());
	}
	
	@Test
	public void testGetActivity() throws Exception  {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		a = new Activity();
		a.setId(1);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		Activity ra = ase.getActivity(1);
		
		assertNotNull(ra);
		assertEquals(1, (int)ra.getId());
	}
	
	
	@Test
	public void testGetProject_noProject() throws Exception  {
		pjs.deleteProject(0);
		Project p = ase.getProject();
		
		assertNull(p);
	}
	
	@Test
	public void testDeleteActivity() throws Exception  {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		a = new Activity();
		a.setId(1);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		ase.deleteActivity(0);
		
		assertEquals(1,ase.getActivities().size());
	}
	
	@Test
	public void testGetActivitiesWithIDs() throws Exception  {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		a = new Activity();
		a.setId(3);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(0);
		ids.add(3);
		List<Activity> acts = ase.getActivities(ids);
		
		assertEquals(2, acts.size());
	}
	
	@Test (expected = UnsupportedOperationException.class)
	public void testAddUserToActivity() throws Exception  {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		Users u = new Users();
		u.setFirstName("Tester");
		
		ase.addUserToActivity(0, u);
		
		
	}
	@Test(expected = UnsupportedOperationException.class)
	public void testDeleteUserFromActivity() throws Exception  {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		Users u = new Users();
		u.setFirstName("Tester");

		ase.deleteUserFromActivity(0, u);
	}
	
	@Test(expected=ServiceFunctionalityException.class)
	public void testAddActivity_wrongProject() throws Exception{
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(1);
		ase.addActivity(a);
	}
	
	
	/**
	 * This test failed, can still add an activity to completed project
	 * @throws Exception
	 */
	@Test
	public void testAddActivity_projectComplete() throws Exception{
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(1);
		
		Project p = new Project();
        p.setId(1);
        p.setCompleted(true);
        p.setProjectName("Test Project Complete");
        p.setDescription("Test Project Complete");
        
        pjs.addProject(p);
        
        ase = appCtx.getActivityService(1);
        
        ase.addActivity(a);

        assertEquals(0,ase.getActivities().size());
	}
	
	/**
	 * Test failed, getDependents() and getDepende
	 */
	@Test
	public void testAddDependency(){
		Activity a = new Activity();
		Activity b = new Activity();
		Activity c = new Activity();
		
		a.setId(0);
		a.setProjectId(0);
		b.setId(1);
		b.setProjectId(0);
		c.setId(2);
		c.setProjectId(0);
		
		ase.addActivity(a);
		ase.addActivity(b);
		ase.addActivity(c);
		
		ase.addDependency(1, 0);
		ase.addDependency(2, 0);
		
		System.out.println("Activites: " + ase.getActivities().size());
		System.out.println("Dependents of Activity 0: " + ase.getDependents(0).size());
		
		assertEquals(2,ase.getDependents(0).size());
		assertEquals(1,ase.getDependencies(1).size());
	}
	
	/**
	 * Test failed also because of getDependents() returning 0
	 */
	@Test 
	public void testDeleteDependency(){
		Activity a = new Activity();
		Activity b = new Activity();
		Activity c = new Activity();
		
		a.setId(0);
		a.setProjectId(0);
		b.setId(1);
		b.setProjectId(0);
		c.setId(2);
		c.setProjectId(0);
		
		ase.addActivity(a);
		ase.addActivity(b);
		ase.addActivity(c);
		ase.addDependency(1, 0);
		ase.addDependency(2, 0);
		ase.deleteDependency(1, 0);
		
		assertEquals(1,ase.getDependents(0).size());
		assertEquals(0,ase.getDependencies(1).size());
	}
	
	@Before
	public void initial()throws Exception{
		File file = Files.createTempFile( "act_test", "db" ).toFile();

		db = new DatabaseManager( file.getName() );
		appCtx = App.getApplicationContext( db.getConnection() );
		
		pjs = appCtx.getProjectService();
		
		Project p = new Project();
        p.setId(0);
        p.setProjectName("Test Project");
        p.setDescription("Test Project");
        pjs.addProject(p);
		
		ase = appCtx.getActivityService(0);
	}
	
	

}
