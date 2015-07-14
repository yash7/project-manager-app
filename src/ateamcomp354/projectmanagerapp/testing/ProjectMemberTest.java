package ateamcomp354.projectmanagerapp.testing;

import static org.junit.Assert.*;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.junit.Before;
import org.junit.Test;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectMemberService;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.UserService;

public class ProjectMemberTest extends AbstractDatabaseTest {

	private ProjectMemberService pms;
	private ProjectService ps;
	private ApplicationContext appCtx;
	private ActivityService as;

	@Test
	public void testgetAssignedActivities()  {
		as.addActivity(new Activity(null, as.getProject().getId(), Status.NEW, 0, 5, "Label", 0, 5, 5, 5, "Desc", 0));
		
		assertEquals(1, as.getActivities().size());
		assertEquals(0, as.getAssigneesForActivity(1).size());
		
		ps.addUserToProject(as.getProject().getId(), appCtx.getUserService().getUser(2));
		
		as.addUserToActivity(1, appCtx.getUserService().getUser(2));
		
		assertEquals(1, as.getAssigneesForActivity(1).size());
	}
	
	@Test
	public void testgetAssignedProjects()  {
		assertEquals(0, pms.getAssignedProjects().size());
		
		Project p = new Project();
        p.setProjectName("Test Project 2");
        p.setDescription("Test Project 2");
        ps.addProject(p);
		
        ActivityService as2 = appCtx.getActivityService(1);

        as.addActivity(new Activity(null, as.getProject().getId(), Status.NEW, 0, 5, "Label", 0, 5, 5, 5, "Desc", 0));
        as2.addActivity(new Activity(null, as2.getProject().getId(), Status.NEW, 0, 35, "Label3", 0, 35, 35, 35, "Desc3", 0));
		
        
        ps.addUserToProject(as.getProject().getId(), appCtx.getUserService().getUser(2));
        as.addUserToActivity(1, appCtx.getUserService().getUser(2));

        assertEquals(1, pms.getAssignedProjects().size());
        
        ps.addUserToProject(as2.getProject().getId(), appCtx.getUserService().getUser(2));
        as2.addUserToActivity(2, appCtx.getUserService().getUser(2));
		
		assertEquals(2, pms.getAssignedProjects().size());
	}
	
	@Test
	public void testgetOtherAssigneesForActivity()  {
		Activity a = new Activity();
		a.setId(0);
		a.setProjectId(as.getProject().getId());
		as.addActivity(a);

		Users u = new Users();
		u.setId(3);
		u.setUsername("ttt");
		u.setPassword("ttt");
		u.setFirstName("Tester");
		u.setLastName("Testington");
		
		appCtx.getUserService().addUser(u);
		
		Users u2 = new Users();
		u2.setId(4);
		u2.setUsername("tts");
		u2.setPassword("tttt");
		u2.setFirstName("Tester 2");
		u2.setLastName("Testington 2");
		
		appCtx.getUserService().addUser(u2);
		
		ps.addUserToProject(as.getProject().getId(), u);
		ps.addUserToProject(as.getProject().getId(), u2);
		
		as.addUserToActivity(a.getId(), u);

		assertEquals(1, pms.getOtherAssigneesForActivity(0).size());
		
		as.addUserToActivity(a.getId(), u2);
		
		assertEquals(2, pms.getOtherAssigneesForActivity(0).size());
	}
	
	@Before
	public void initial() {
		appCtx = App.getApplicationContext( db.getConnection() );
		
		appCtx.getUserService().addUser( new Users( null, "Dwayne", "Carter", "dcarter", "password1", false ) );	
		pms = appCtx.getProjectMemberService(2);
		ps = appCtx.getProjectService();
				
		Project p = new Project();
        p.setId(0);
        p.setProjectName("Test Project");
        p.setDescription("Test Project");
        ps.addProject(p);	
        
        as = appCtx.getActivityService(0);
	}

}
