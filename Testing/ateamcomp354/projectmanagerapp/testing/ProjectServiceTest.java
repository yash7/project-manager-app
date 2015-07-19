package ateamcomp354.projectmanagerapp.testing;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;
import ateamcomp354.projectmanagerapp.services.UserService;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ProjectServiceTest extends AbstractDatabaseTest {

    @Test
    public void testGetProjects_noProjects() throws Exception {

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        List<Project> projectList = projectService.getProjects();
        assertEquals(0, projectList.size());
    }

    @Test
    public void testGetProjects_addedProject() throws Exception {

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = new Project();
        p.setId(0);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);

        List<Project> projectList = projectService.getProjects();
        assertEquals(1, projectList.size());
    }

    @Test
    public void testGetProject_noProjects() throws Exception {

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = projectService.getProject(0);

        assertNull(p);
    }

    @Test
    public void testGetProject_projectExists() throws Exception {

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = new Project();
        p.setId(0);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);

        Project test = projectService.getProject(0);
        
        assertNotNull(test);
        assertEquals("Test project", test.getProjectName());
        assertEquals("Test project", test.getDescription());
        assertEquals((int) test.getId(), 0);
    }

    @Test
  	public void testGetProjectActivitiesCount_noActivities() throws Exception {
    	final int projectID 		= 0;
    	final int activitiesCount	= 0;
    	final String projectInfo 	= "Test project";

    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);

        projectService.addProject(p);
        
        assertEquals(activitiesCount, projectService.getProjectActivitiesCount(projectID));
    }
    
    @Test
  	public void testGetProjectActivitiesCount_properCount() throws Exception {
    	final int projectID 		= 0;
    	final int activitiesCount	= 2;
    	final int activity1ID		= 0;
    	final int activity2ID		= 1;
    	final String projectInfo 	= "Test project";

    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);

        projectService.addProject(p);
        
        ActivityService activityService = appCtx.getActivityService(projectID);
        
        Activity a1 = new Activity();
        Activity a2 = new Activity();
        
        a1.setId(activity1ID);
        a2.setId(activity2ID);
        a1.setProjectId(projectID);
        a2.setProjectId(projectID);

		activityService.addActivity(a1);
		activityService.addActivity(a2);
        
        assertEquals(activitiesCount, projectService.getProjectActivitiesCount(projectID));
    }
    
    @Test
    public void testGetProjectCompletion_halfComplete() throws Exception {

        final int projectId = 0;

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        ActivityService activityService = appCtx.getActivityService(projectId);

        Project p = new Project();
        p.setId(projectId);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);

        Activity a1 = new Activity();
        a1.setProjectId(projectId);
        a1.setLabel("a1");

        Activity a2 = new Activity();
        a2.setProjectId(projectId);
        a2.setLabel("a2");
        a2.setStatus(Status.RESOLVED);

        activityService.addActivity(a1);
        activityService.addActivity(a2);

        assertEquals(
                50,
                projectService.getProjectCompletion(projectId)
        );
    }

    @Test
    public void testGetProjectCompletion_zeroComplete() throws Exception {

        final int projectId = 0;

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        ActivityService activityService = appCtx.getActivityService(projectId);

        Project p = new Project();
        p.setId(projectId);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);

        Activity a1 = new Activity();
        a1.setProjectId(projectId);
        a1.setLabel("a1");

        Activity a2 = new Activity();
        a2.setProjectId(projectId);
        a2.setLabel("a2");

        activityService.addActivity(a1);
        activityService.addActivity(a2);

        assertEquals(
                0,
                projectService.getProjectCompletion(projectId)
        );
    }

    @Test
    public void testGetProjectCompletion_noActivityZeroComplete() throws Exception {

        final int projectId = 0;

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        ActivityService activityService = appCtx.getActivityService(projectId);

        Project p = new Project();
        p.setId(projectId);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);

        assertEquals(
                0,
                projectService.getProjectCompletion(projectId)
        );
    }

    @Test
    public void testGetProjectCompletion_complete() throws Exception {

        final int projectId = 0;

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        ActivityService activityService = appCtx.getActivityService(projectId);

        Project p = new Project();
        p.setId(projectId);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);

        Activity a1 = new Activity();
        a1.setProjectId(projectId);
        a1.setLabel("a1");
        a1.setStatus(Status.RESOLVED);

        Activity a2 = new Activity();
        a2.setProjectId(projectId);
        a2.setLabel("a2");
        a2.setStatus(Status.RESOLVED);

        activityService.addActivity(a1);
        activityService.addActivity(a2);

        assertEquals(
                100,
                projectService.getProjectCompletion(projectId)
        );
    }

    @Test
    public void testAddProject_nullProject() throws Exception {


        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project test = null;

        projectService.addProject(test);


        assertEquals(0, projectService.getProjects().size());

        assertNull(test);
    }

    @Test
    public void testAddProject_newProject() throws Exception {


        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = new Project();
        p.setId(0);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);

        assertEquals(1, projectService.getProjects().size());
    }

    @Test(expected = ServiceFunctionalityException.class)
    public void testAddProject_duplicateProjects() throws Exception {


        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
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


        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        projectService.deleteProject(0);
    }

    @Test
    public void testDeleteProject_projectExists() throws Exception {


        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
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


        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        ActivityService activityService = appCtx.getActivityService(projectId);

        Project p = new Project();
        p.setId(projectId);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);

        Activity a1 = new Activity();
        a1.setProjectId(projectId);

        Activity a2 = new Activity();
        a2.setProjectId(projectId);

        activityService.addActivity(a1);
        activityService.addActivity(a2);

        List<Activity> activityList = activityService.getActivities();

        for (Activity a : activityList) {
            activityService.deleteActivity(a.getId());
        }

        projectService.deleteProject(0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testDeleteProject_activitiesNotDeleted() throws Exception {

        final int projectId = 0;


        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        ActivityService activityService = appCtx.getActivityService(projectId);

        Project p = new Project();
        p.setId(projectId);
        p.setProjectName("Test project");
        p.setDescription("Test project");

        projectService.addProject(p);

        Activity a1 = new Activity();
        a1.setProjectId(projectId);

        Activity a2 = new Activity();
        a2.setProjectId(projectId);

        activityService.addActivity(a1);
        activityService.addActivity(a2);

        projectService.deleteProject(0);
    }

    @Test
    public void testUpdateProject_productExists() throws Exception {


        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
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

        assertEquals(0, (int) finalTest.getId());
        assertEquals("New description", finalTest.getDescription());
        assertEquals("New project name", finalTest.getProjectName());
    }
    
    @Test
    public void testUpdateProjectBudgetAtCompletion() throws Exception {

    	  ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
          ProjectService projectService = appCtx.getProjectService();
         
          
          Project p = new Project();
          p.setId(0);
          p.setProjectName("Test project");
          p.setDescription("Test project");

          projectService.addProject(p);

          ActivityService activityService = appCtx.getActivityService(0);
          
          Activity a = new Activity();
          a.setId(0);
		  a.setProjectId(0);
		  a.setDescription("This was a test");
		  a.setDuration(3);
		  a.setPlannedValue(0);
		  
		  activityService.addActivity(a);
		  
		  projectService.updateProjectBudgetAtCompletion(0);
		  
		  assertEquals(0, (int)projectService.getProject(0).getBudgetAtCompletion());   
		  
		  Activity b = new Activity();
		  
		  b = activityService.getActivity(0);
		  
		  b.setPlannedValue(2);
		  
		  activityService.updateActivity(b);
		  
		  projectService.updateProjectBudgetAtCompletion(0);
		  
		  assertEquals(2, (int)projectService.getProject(0).getBudgetAtCompletion());
    }

    @Test(expected=Exception.class)
    public void testUpdateProjectBudgetAtCompletion_noNegativeBudget() throws Exception {
    	final int projectID 		= 0;
    	final int activityID 		= 0;
    	final int duration			= -3;
    	final int plannedValue		= 0;
        final String projectInfo 	= "Test project";
        final String activityInfo	= "This was a test";
        
    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
       
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);

        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(projectID);
        
        Activity a = new Activity();
        a.setId(activityID);
        a.setProjectId(projectID);
		a.setDescription(activityInfo);
		a.setDuration(duration);
		a.setPlannedValue(plannedValue);
		  
		activityService.addActivity(a);
		  
		projectService.updateProjectBudgetAtCompletion(projectID);
  }

    @Test
    public void testAddUserToProject_properlyAddUser() throws Exception {
    	final int projectID 		= 0;
    	final int memberCount		= 1;
    	final boolean manager		= false;
    	final String firstName		= "test";
    	final String lastName		= "test";
    	final String userName		= "test";
    	final String password		= "test";
    	final String projectInfo 	= "Test project";
    	int userID					= 0;
    	Users u						= null;
    	
    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        UserService userService = appCtx.getUserService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);
        
        projectService.addProject(p);
        
        p = projectService.getProject(projectID);
        
        for(int i = 0; i < memberCount; i++) {
	        u = new Users(userID++, firstName, lastName, userName, password, manager);
	        userService.addUser(u);
	        projectService.addUserToProject(projectID, u);
        }
        
        List<Users> users = projectService.getMembersForProject(projectID);
        assertEquals(memberCount, users.size());
    }
    
    @Test(expected=ServiceFunctionalityException.class)
    public void testAddUserToProject_cantAddUserTwice() throws Exception {
    	final int projectID 		= 0;
    	final int userID			= 0;
    	final boolean manager		= false;
    	final String firstName		= "test";
    	final String lastName		= "test";
    	final String userName		= "test";
    	final String password		= "test";
    	final String projectInfo 	= "Test project";
    	
    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        UserService userService = appCtx.getUserService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);
        
        projectService.addProject(p);
        
        p = projectService.getProject(projectID);
        
        Users u = new Users(userID, firstName, lastName, userName, password, manager);
        userService.addUser(u);
        
        projectService.addUserToProject(projectID, u);
        projectService.addUserToProject(projectID, u);
    }
    
    @Test
    public void testGetUnassignedMembersForProject_getUnassignedMember() throws Exception {
    	final int projectID 		= 0;
    	final int membersCount 		= 1;
    	final boolean manager		= false;
    	final String projectInfo 	= "Test project";
    	final String firstName		= "test";
    	final String lastName		= "test";
    	final String userName		= "test";
    	final String password		= "test";	
    	int userID					= 0;
    	Users u						= null;
    	
    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        UserService userService = appCtx.getUserService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);
        
        projectService.addProject(p);
        
        for (int i = 0; i < membersCount; i++) {
        	 u = new Users(userID++, firstName, lastName, userName, password, manager);
        	 userService.addUser(u);
        }
          
        List<Users> users = projectService.getUnassignedMembersForProject(projectID);
        
        assertEquals(membersCount, users.size());
    }
    
    @Test
    public void testGetUnassignedMembersForProject_noUnassignedMember() throws Exception {
    	final int projectID 		= 0;
    	final int membersCount 		= 0;
    	final String projectInfo 	= "Test project";
    	
    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);
        
        projectService.addProject(p);
          
        List<Users> users = projectService.getUnassignedMembersForProject(projectID);
        
        assertEquals(membersCount, users.size());
    }

    @Test(expected=Exception.class)
    public void testDeleteUserFromProject_userNotInProject() throws Exception {
    	final int projectID 		= 0;
    	final int membersCount 		= 1;
    	final boolean manager		= false;
    	final String projectInfo 	= "Test project";
    	final String firstName		= "test";
    	final String lastName		= "test";
    	final String userName		= "test";
    	final String password		= "test";
    	int userID					= 0;
    	Users u						= null;
    	
    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        UserService userService = appCtx.getUserService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);
        
        projectService.addProject(p);
        
        for (int i = 0; i < membersCount; i++) {
        	 u = new Users(userID++, firstName, lastName, userName, password, manager);
        	 userService.addUser(u);
        }
        
        projectService.deleteUserFromProject(projectID, u);
    }
    
    @Test
    public void testDeleteUserFromProject_properlyDeletes() throws Exception {
    	final int projectID 		= 0;
    	final int membersCount 		= 1;
    	final boolean manager		= false;
    	final String projectInfo 	= "Test project";
    	final String firstName		= "test";
    	final String lastName		= "test";
    	final String userName		= "test";
    	final String password		= "test";
    	int userID					= 0;
    	Users u						= null;
    	
    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        UserService userService = appCtx.getUserService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);
        
        projectService.addProject(p);
        
        for (int i = 0; i < membersCount; i++) {
        	 u = new Users(userID++, firstName, lastName, userName, password, manager);
        	 userService.addUser(u);
        	 projectService.addUserToProject(projectID, u);
        }
        
        projectService.deleteUserFromProject(projectID, u);
        
        List<Users> users = projectService.getMembersForProject(projectID);
        
        assertEquals(membersCount - 1, users.size());
    }
    
    @Test
    public void testGetMembersForProject_noMembers() throws Exception {
    	final int projectID 		= 0;
    	final int membersCount		= 0;
    	final String projectInfo 	= "Test project";
    	
    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);
        
        projectService.addProject(p);
        
        List<Users> users = projectService.getMembersForProject(projectID);
        
        assertEquals(membersCount, users.size());
    }

    @Test
    public void testGetMembersForProject_properMembers() throws Exception {
    	final int projectID 		= 0;
    	final int membersCount		= 1;
    	final boolean manager		= false;
    	final String projectInfo 	= "Test project";
    	final String firstName		= "test";
    	final String lastName		= "test";
    	final String userName		= "test";
    	final String password		= "test";
    	int userID					= 0;
    	Users u						= null;
    	
    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        UserService userService = appCtx.getUserService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);
        
        projectService.addProject(p);
        
        for (int i = 0; i < membersCount; i++) {
       	 u = new Users(userID++, firstName, lastName, userName, password, manager);
       	 userService.addUser(u);
       	 projectService.addUserToProject(projectID, u);
       }
        
        List<Users> users = projectService.getMembersForProject(projectID);
        
        assertEquals(membersCount, users.size());
    }

    @Test
    public void testGetMembersForProject_properMembersAfterDeletion() throws Exception {
    	final int projectID 		= 0;
    	final String projectInfo 	= "Test project";
    	final int membersCount		= 2;
    	int userID					= 0;
    	final String firstName		= "test";
    	final String lastName		= "test";
    	final String userName		= "test";
    	final String password		= "test";
    	final boolean manager		= false;
    	Users u						= null;
    	
    	ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();
        UserService userService = appCtx.getUserService();
        
        Project p = new Project();
        p.setId(projectID);
        p.setProjectName(projectInfo);
        p.setDescription(projectInfo);
        
        projectService.addProject(p);
        
        for (int i = 0; i < membersCount; i++) {
       	 	u = new Users(userID++, firstName + userID, lastName+ userID, userName + userID, password, manager);
       	 	userService.addUser(u);
       	 	projectService.addUserToProject(projectID, u);
        }
        
        projectService.deleteUserFromProject(projectID, u);
        
        List<Users> users = projectService.getMembersForProject(projectID);
        
        assertEquals(membersCount - 1, users.size());
    }
}
