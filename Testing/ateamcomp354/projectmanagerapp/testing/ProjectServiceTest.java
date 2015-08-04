package ateamcomp354.projectmanagerapp.testing;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;
import ateamcomp354.projectmanagerapp.services.UserService;

import ateamcomp354.projectmanagerapp.testing.util.ActivityServiceUtil;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static ateamcomp354.projectmanagerapp.testing.util.PojoMaker.makeActivity;
import static ateamcomp354.projectmanagerapp.testing.util.PojoMaker.makeDate;
import static ateamcomp354.projectmanagerapp.testing.util.PojoMaker.makeProject;
import static java.util.stream.Collectors.toList;
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

    @Test
    public void testUpdateActualCostAtCompletion_NoActivities(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "No activities");
        projectService.addProject(p);

        projectService.updateActualCostAtCompletion(p.getId());

        assertEquals(Integer.valueOf(0), projectService.getProject(p.getId()).getActualCostAtCompletion());
    }

    @Test
    public void testUpdateActualCostAtCompletion_NoResolvedActivities(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "No resolved activities");
        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(p.getId());

        Activity A = makeActivity(p.getId(), "A", 100);
        Activity B = makeActivity(p.getId(), "B", 200);
        Activity C = makeActivity(p.getId(), "C", 300);

        Stream.of(A, B, C).forEach( activityService::addActivity );

        projectService.updateActualCostAtCompletion(p.getId());

        assertEquals(Integer.valueOf(0), projectService.getProject(p.getId()).getActualCostAtCompletion());
    }

    @Test
    public void testUpdateActualCostAtCompletion_WithSomeResolvedActivities(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "Some resolved activities");
        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(p.getId());

        Activity A = makeActivity(p.getId(), "A", 100);
        Activity B = makeActivity(p.getId(), "B", 200);
        Activity C = makeActivity(p.getId(), "C", 300);
        Activity D = makeActivity(p.getId(), "D", 400);

        Stream.of(A, D).forEach( a -> a.setStatus(Status.RESOLVED) );
        int actualCostAtCompletion = Stream.of(A, D).mapToInt( Activity::getActualCost ).sum();

        Stream.of(A, B, C, D).forEach( activityService::addActivity );

        projectService.updateActualCostAtCompletion(p.getId());

        assertEquals(Integer.valueOf(actualCostAtCompletion), projectService.getProject(p.getId()).getActualCostAtCompletion());
    }

    @Test
    public void testUpdateActualCostAtCompletion_WithAllResolvedActivities(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "All resolved activities");
        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(p.getId());

        Activity A = makeActivity(p.getId(), "A", 100);
        Activity B = makeActivity(p.getId(), "B", 200);
        Activity C = makeActivity(p.getId(), "C", 300);
        Activity D = makeActivity(p.getId(), "D", 400);
        Activity E = makeActivity(p.getId(), "E", 500);

        Stream.of(A, B, C, D, E).forEach(a -> a.setStatus(Status.RESOLVED));
        int actualCostAtCompletion = Stream.of(A, B, C, D, E).mapToInt( Activity::getActualCost ).sum();

        Stream.of(A, B, C, D, E).forEach( activityService::addActivity );

        projectService.updateActualCostAtCompletion(p.getId());

        assertEquals(Integer.valueOf(actualCostAtCompletion), projectService.getProject(p.getId()).getActualCostAtCompletion());
    }

    @Test
    public void testUpdateActualCostAtCompletion_WithCompletedProject(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "Completed project");
        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(p.getId());

        Activity A = makeActivity(p.getId(), "A", 100);
        Activity B = makeActivity(p.getId(), "B", 200);
        Activity C = makeActivity(p.getId(), "C", 300);

        Stream.of(A, B, C).forEach(a -> a.setStatus(Status.RESOLVED));
        int actualCostAtCompletion = Stream.of(A, B, C).mapToInt( Activity::getActualCost ).sum();

        Stream.of(A, B, C).forEach( activityService::addActivity );

        p.setCompleted(true);
        projectService.updateProject(p);

        projectService.updateActualCostAtCompletion(p.getId());

        assertEquals(Integer.valueOf(actualCostAtCompletion), projectService.getProject(p.getId()).getActualCostAtCompletion());
    }

    @Test
    public void testEVactivitiesByEarliestStart_NoActivities(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "No activities");
        projectService.addProject(p);

        assertEquals(Collections.emptyList(), projectService.EVactivitiesByEarliestStart(p.getId()));
    }

    @Test
    public void testEVactivitiesByEarliestStart_WithUniqueStartDates(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "No activities");
        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(p.getId());

        Activity A = makeActivity(0, "A", makeDate(2015, 1, 1), makeDate(2015, 1, 2));
        Activity B = makeActivity(0, "B", makeDate(2015, 1, 2), makeDate(2015, 1, 4));
        Activity C = makeActivity(0, "C", makeDate(2015, 1, 4), makeDate(2015, 1, 7));
        Activity D = makeActivity(0, "D", makeDate(2015, 1, 7), makeDate(2015, 1, 11));

        Stream.of(A, B, C, D).forEach( activityService::addActivity );

        List<Integer> validSort = Arrays.asList(A, B, C, D).stream()
                .map(Activity::getId)
                .collect(toList());

        List<Integer> questionableSort = projectService.EVactivitiesByEarliestStart(p.getId()).stream()
                .map(Activity::getId)
                .collect(toList());

        assertEquals(validSort, questionableSort);
    }

    @Test
    public void testEVactivitiesByEarliestStart_WithSomeSameStartDates(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "No activities");
        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(p.getId());

        Activity A = makeActivity(0, "A", makeDate(2015, 1, 1), makeDate(2015, 1, 2));
        Activity B = makeActivity(0, "B", makeDate(2015, 1, 2), makeDate(2015, 1, 4));
        Activity C = makeActivity(0, "C", makeDate(2015, 1, 2), makeDate(2015, 1, 7));
        Activity D = makeActivity(0, "D", makeDate(2015, 1, 7), makeDate(2015, 1, 11));

        Stream.of(A, B, C, D).forEach( activityService::addActivity );

        List<Integer> validSort1 =  Arrays.asList(A, B, C, D).stream()
                                            .map(Activity::getId)
                                            .collect(toList());
        List<Integer> validSort2 =  Arrays.asList(A, C, B, D).stream()
                                            .map(Activity::getId)
                                            .collect(toList());

        List<Integer> questionableSort = projectService.EVactivitiesByEarliestStart(p.getId()).stream()
                .map(Activity::getId)
                .collect(toList());

        boolean isSort1 = validSort1.equals( questionableSort );
        boolean isSort2 = validSort2.equals( questionableSort );

        assertTrue( "Not sort1 or sort2", isSort1 || isSort2 );
    }

    @Test
    public void testEVStartDate_NoActivities(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "One activity");
        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(p.getId());

        ActivityServiceUtil.calculateAllParams( activityService );

        List<Object> evStartDate = projectService.EVStartDate(p.getId());

        assertEquals( null, evStartDate );
    }

    @Test
    public void testEVStartDate_OneActivity(){

        Calendar startDate = makeDate(2015, 1, 1);
        Calendar endDate = makeDate(2015, 1, 2);

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "One activity");
        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(p.getId());

        Activity A = makeActivity(0, "A", startDate, endDate);

        Stream.of(A).forEach( activityService::addActivity );

        ActivityServiceUtil.calculateAllParams( activityService );

        List<Object> evStartDate = projectService.EVStartDate(p.getId());

        assertEquals( Arrays.asList(startDate.getTime(), 1), evStartDate.get(0) );
    }

    @Test
    public void testEVStartDate_OverTwoWeeks(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "Over two weeks");
        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(p.getId());

        Activity A = makeActivity(0, "A", makeDate(2015,1,5), makeDate(2015,1,6));
        Activity B = makeActivity(0, "B", makeDate(2015,1,6), makeDate(2015,1,8));
        Activity C = makeActivity(0, "C", makeDate(2015,1,8), makeDate(2015,1,12));

        Stream.of(A, B, C).forEach( activityService::addActivity );

        activityService.addDependency(A.getId(), B.getId());
        activityService.addDependency(B.getId(), C.getId());

        ActivityServiceUtil.calculateAllParams( activityService );

        List<Object> evStartDate = projectService.EVStartDate(p.getId());

        assertEquals( Arrays.asList(makeDate(2015,1,5).getTime(), 2), evStartDate );
    }

    @Test
    public void testEVStartDate_OverThreeWeeks(){

        ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
        ProjectService projectService = appCtx.getProjectService();

        Project p = makeProject(0, "Over three weeks");
        projectService.addProject(p);

        ActivityService activityService = appCtx.getActivityService(p.getId());

        Activity A = makeActivity(0, "A", makeDate(2015,1,7), makeDate(2015,1,14));
        Activity B = makeActivity(0, "B", makeDate(2015,1,14), makeDate(2015,1,21));

        Stream.of(A, B).forEach( activityService::addActivity );

        activityService.addDependency(A.getId(), B.getId());

        ActivityServiceUtil.calculateAllParams( activityService );

        List<Object> evStartDate = projectService.EVStartDate(p.getId());

        assertEquals( Arrays.asList(makeDate(2015,1,7).getTime(), 3), evStartDate );
    }
}
