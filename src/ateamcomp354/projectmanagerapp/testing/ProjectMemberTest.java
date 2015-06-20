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
		as.addActivity(new Activity(null, as.getProject().getId(), Status.NEW, 0, 5, "Label", 0, 5, 5, 5, "Desc"));
		
		assertEquals(1, as.getActivities().size());
		assertEquals(0, as.getAssigneesForActivity(1).size());
		
		as.addUserToActivity(1, appCtx.getUserService().getUser(2));
		
		assertEquals(1, as.getAssigneesForActivity(1).size());
	}
	
	@Test
	public void testgetAssignedProjects()  {
		assertEquals(0, pms.getAssignedProjects().size());
		
		ps.addProject(new Project(null, "testProject", "", false));
		
		Project p = ps.getProject(0);
			
		//TODO once we can add "members" to a project
		
		assertEquals(1, pms.getAssignedProjects().size());
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
