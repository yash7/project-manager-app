package ateamcomp354.projectmanagerapp.testing;

/**
 * Takes care of all database access
 * 
 * @author Brendan Blencowe (26985455)
 */

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.junit.Before;
import org.junit.Test;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;

public class ActivityServiceTest extends AbstractDatabaseTest {

	private ActivityService ase;
	private ProjectService pjs;
	private ApplicationContext appCtx;
	
	
	@Test
	public void testAddActivitySuccess()  {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		List<Activity> acts = ase.getActivities();
		
		assertEquals(1, acts.size());
		
	}
	
	
	@Test
	public void testGetActivities_noActivities()   {

		assertEquals(0, ase.getActivities().size());
	}
	
	@Test
	public void testGetActivities()   {

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
	public void testGetProject() {

		assertEquals("Test Project", ase.getProject().getProjectName());
	}
	
	@Test
	public void testGetActivity() {
		
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
	public void testGetProject_noProject() {

		pjs.deleteProject(0);
		Project p = ase.getProject();
		
		assertNull(p);
	}
	
	@Test
	public void testDeleteActivity() {

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
	public void testGetActivitiesWithIDs() {

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
	
	@Test
	public void testgetAssigneesForActivity() {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		Users u = new Users();
		u.setId(2);
		u.setUsername("ttt");
		u.setPassword("ttt");
		u.setFirstName("Tester");
		u.setLastName("Testington");
		
		appCtx.getUserService().addUser(u);
		
		pjs.addUserToProject(ase.getProject().getId(), u);

		assertEquals("Tester", ase.getProjectMember(u.getId()).getFirstName());
		
		ase.addUserToActivity(a.getId(), u);
		
		assertEquals(1, ase.getAssigneesForActivity(a.getId()).size());
	}
	
	@Test
	public void testgetAssigneesForActivity_notinproject() {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		Users u = new Users();
		u.setId(2);
		u.setUsername("ttt");
		u.setPassword("ttt");
		u.setFirstName("Tester");
		u.setLastName("Testington");
		
		appCtx.getUserService().addUser(u);
		
		assertEquals(0, ase.getProjectMembers().size());

		assertEquals(0, ase.getAssigneesForActivity(a.getId()).size());
		
		try { //should throw error, catch and ignore
			ase.addUserToActivity(a.getId(), u);
		}
		catch(IllegalStateException e) {}
		
		assertEquals(0, ase.getAssigneesForActivity(a.getId()).size());
	}
	
	@Test
	public void testgetUnassignedMembersForActivity() {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		Users u = new Users();
		u.setId(2);
		u.setUsername("ttt");
		u.setPassword("ttt");
		u.setFirstName("Tester");
		u.setLastName("Testington");
		
		appCtx.getUserService().addUser(u);
			
		assertEquals(0, ase.getUnassignedMembersForActivity(a.getId()).size());
		
		pjs.addUserToProject(ase.getProject().getId(), u);
	
		assertEquals(1, ase.getUnassignedMembersForActivity(a.getId()).size());
	}
	
	@Test
	public void testAddUserToActivity() {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		Users u = new Users();
		u.setId(2);
		u.setUsername("ttt");
		u.setPassword("ttt");
		u.setFirstName("Tester");
		u.setLastName("Testington");
		
		appCtx.getUserService().addUser(u);
		pjs.addUserToProject(ase.getProject().getId(), u);
		
		assertEquals(0, ase.getAssigneesForActivity(a.getId()).size());
		
		ase.addUserToActivity(0, u);

		assertEquals(1, ase.getAssigneesForActivity(a.getId()).size());
	}
	@Test
	public void testDeleteUserFromActivity()   {

		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(ase.getProject().getId());
		ase.addActivity(a);
		
		Users u = new Users();
		u.setId(2);
		u.setUsername("ttt");
		u.setPassword("ttt");
		u.setFirstName("Tester");
		u.setLastName("Testington");
		
		appCtx.getUserService().addUser(u);
		ase.deleteUserFromActivity(0, u);
		
		assertEquals(0, ase.getAssigneesForActivity(a.getId()).size());
	}
	
	@Test(expected=ServiceFunctionalityException.class)
	public void testAddActivity_wrongProject() {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(1);
		ase.addActivity(a);
	}
	
	
	@Test(expected=IllegalStateException.class)
	public void testAddActivity_projectComplete() {	
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
	
	@Test
	public void testUpdateActivity() {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(0);
		a.setDescription("This was a test");
		a.setDuration(3);
		
		ase.addActivity(a);
		
		a.setDescription("Update Success");
		a.setDuration(4);
		
		ase.updateActivity(a);
		assertEquals(new Integer(4), ase.getActivity(0).getDuration());
		assertEquals("Update Success", ase.getActivity(0).getDescription());
	}
	
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
		
		ase.addDependency(0, 1);
		ase.addDependency(0, 2);		
		
		assertEquals(2,ase.getDependents(0).size());
		assertEquals(1,ase.getDependencies(1).size());
	}
	
	@Test(expected=ServiceFunctionalityException.class)
	public void testAddDependency_noCircular(){
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
		
		ase.addDependency(0, 1);
		ase.addDependency(1, 2);		
		
		assertEquals(1,ase.getDependents(1).size());
		assertEquals(1,ase.getDependencies(2).size());
		
		ase.addDependency(2, 0); //circular dependency
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
		ase.addDependency(0, 1);
		ase.addDependency(0, 2);
		ase.deleteDependency(0, 1);
		
		assertEquals(1,ase.getDependents(0).size());
		assertEquals(0,ase.getDependencies(1).size());
	}
	
	@Test
	public void testgetProjectMember() {
		Users u = new Users();
		u.setId(2);
		u.setUsername("ttt");
		u.setPassword("ttt");
		u.setFirstName("Tester");
		u.setLastName("Testington");
		
		Users u2 = new Users();
		u2.setId(3);
		u2.setUsername("ttt2");
		u2.setPassword("ttt2");
		u2.setFirstName("Tester2");
		u2.setLastName("Testington2");
		
		appCtx.getUserService().addUser(u);
		appCtx.getUserService().addUser(u2);
		
		pjs.addUserToProject(0, u);
		pjs.addUserToProject(0, u2);
		
		assertEquals("ttt", ase.getProjectMember(2).getUsername());
		assertEquals("ttt2", ase.getProjectMember(3).getUsername());
	}
	
	@Test
	public void testgetProjectMembers() {
		Users u = new Users();
		u.setId(2);
		u.setUsername("ttt");
		u.setPassword("ttt");
		u.setFirstName("Tester");
		u.setLastName("Testington");
		
		Users u2 = new Users();
		u2.setId(3);
		u2.setUsername("ttt2");
		u2.setPassword("ttt2");
		u2.setFirstName("Tester");
		u2.setLastName("Testington");
		
		appCtx.getUserService().addUser(u);
		appCtx.getUserService().addUser(u2);
		
		pjs.addUserToProject(0, u);
		pjs.addUserToProject(0, u2);
		
		assertEquals(2, ase.getProjectMembers().size());
	}

	@Test
	public void testGetDependencies_properDependency() {
		final int linkCount	= 1;
		int activityFromID 	= 0;
		int activityToID	= 1;
		Activity to, from	= null;
		
		for (int i = 0; i < linkCount; i++) {
			activityFromID += 2;
			activityToID += 2;
			
			to = new Activity();
			to.setId(activityFromID);
			
			from = new Activity();
			from.setId(activityToID);

			this.ase.addActivity(from);
			this.ase.addActivity(to);
			this.ase.addDependency(activityFromID, activityToID);
		}
		
		List<Integer> dependencies = this.ase.getDependencies(activityToID);
		
		assertEquals(linkCount, dependencies.size());
	}
	
	@Test
	public void testGetDependencies_noDependency() {
		final int linkCount		= 0;
		final int activityID 	= 0;
		Activity a				= null;
		
		a = new Activity();
		a.setId(activityID);
			
		this.ase.addActivity(a);
		
		List<Integer> dependencies = this.ase.getDependencies(activityID);
		
		assertEquals(linkCount, dependencies.size());
	}
	
	@Test
	public void testGetDependencies_properlyRemovesDependency() {
		int linkCount				= 1;
		int activityFromID 			= 0;
		int activityToID			= 1;
		Activity to, from			= null;
		List<Integer> dependencies	= null;

		for (int i = 0; i < linkCount; i++) {
			activityFromID += 2;
			activityToID += 2;
			
			to = new Activity();
			to.setId(activityFromID);
			
			from = new Activity();
			from.setId(activityToID);

			this.ase.addActivity(from);
			this.ase.addActivity(to);
			this.ase.addDependency(activityFromID, activityToID);
		}
		
		dependencies = this.ase.getDependencies(activityToID);
		
		assertEquals(linkCount, dependencies.size());
		
		this.ase.deleteDependency(activityFromID, activityToID);
		linkCount--;
		
		dependencies = this.ase.getDependencies(activityToID);
		
		assertEquals(linkCount, dependencies.size());
	}

	@Test
	public void testGetDependents_properDependents() {
		final int linkCount	= 1;
		int activityFromID 	= 0;
		int activityToID	= 1;
		Activity to, from	= null;
		
		for (int i = 0; i < linkCount; i++) {
			activityFromID += 2;
			activityToID += 2;
			
			to = new Activity();
			to.setId(activityFromID);
			
			from = new Activity();
			from.setId(activityToID);

			this.ase.addActivity(from);
			this.ase.addActivity(to);
			this.ase.addDependency(activityFromID, activityToID);
		}
		
		List<Integer> dependents = this.ase.getDependents(activityFromID);
		
		assertEquals(linkCount, dependents.size());
	}
	
	@Test
	public void testGetDependents_noDependents() {
		final int linkCount		= 0;
		final int activityID 	= 0;
		Activity a				= null;
		
		a = new Activity();
		a.setId(activityID);
			
		this.ase.addActivity(a);
		
		List<Integer> dependents = this.ase.getDependents(activityID);
		
		assertEquals(linkCount, dependents.size());
	}
	
	@Test
	public void testGetDependents_properlyRemovesDependency() {
		int linkCount				= 1;
		int activityFromID 			= 0;
		int activityToID			= 1;
		Activity to, from			= null;
		List<Integer> dependents	= null;

		for (int i = 0; i < linkCount; i++) {
			activityFromID += 2;
			activityToID += 2;
			
			to = new Activity();
			to.setId(activityFromID);
			
			from = new Activity();
			from.setId(activityToID);

			this.ase.addActivity(from);
			this.ase.addActivity(to);
			this.ase.addDependency(activityFromID, activityToID);
		}
		
		dependents = this.ase.getDependents(activityFromID);
		
		assertEquals(linkCount, dependents.size());
		
		this.ase.deleteDependency(activityFromID, activityToID);
		linkCount--;
		
		dependents = this.ase.getDependents(activityFromID);
		
		assertEquals(linkCount, dependents.size());
	}
	
	@Before
	public void initial(){

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
