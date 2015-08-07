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

import static ateamcomp354.projectmanagerapp.testing.util.PojoMaker.*;

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
	
	@Test
	public void testCalculateSizeOfChain_singleChain()	{
		Activity A = makeActivity(0, "A", makeDate(2015, 1, 1), makeDate(2015, 1, 2));
		Activity B = makeActivity(0, "B", makeDate(2015, 1, 3), makeDate(2015, 1, 4));
		Activity C = makeActivity(0, "C", makeDate(2015, 1, 5), makeDate(2015, 1, 6));
		
		ase.addActivity(A);
		ase.addActivity(B);
		ase.addActivity(C);
		
		ase.addDependency(A.getId(), B.getId());
		ase.addDependency(B.getId(), C.getId());
		
		assertEquals(3, ase.calculateSizeOfChain(new ArrayList<Integer>(), 2).size());
		// Also used to test no chains
	}
	
	@Test
	public void testCalculateSizeOfChain_multipleChains()	{
		Activity A = makeActivity(0, "A", makeDate(2015, 1, 1), makeDate(2015, 1, 2));
		Activity B = makeActivity(0, "B", makeDate(2015, 1, 3), makeDate(2015, 1, 4));
		Activity C = makeActivity(0, "C", makeDate(2015, 1, 5), makeDate(2015, 1, 6));
		
		Activity D = makeActivity(0, "D", makeDate(2015, 1, 1), makeDate(2015, 1, 2));
		Activity E = makeActivity(0, "E", makeDate(2015, 1, 3), makeDate(2015, 1, 4));
		
		ase.addActivity(A);
		ase.addActivity(B);
		ase.addActivity(C);
		ase.addActivity(D);
		ase.addActivity(E);
		
		ase.addDependency(A.getId(), B.getId());
		ase.addDependency(B.getId(), C.getId());
		
//		ase.addDependency(D.getId(), E.getId());
		
		assertEquals(3, ase.calculateSizeOfChain(new ArrayList<Integer>(), 2).size());
		assertEquals(1, ase.calculateSizeOfChain(new ArrayList<Integer>(), 4).size());
		// Also used to test chains of one element 
	}
	
	private void testCalculateAllParamsOfChain_setup()	{
		ase.getActivities().clear();
		
		Activity S = makeActivity(0, "S", makeDate(2015, 1, 1), makeDate(2015, 1, 15));
//		Activity AS = makeActivity(0, "AS", makeDate(2015, 1, 1), makeDate(2015, 1, 11));
		Activity DCA = makeActivity(0, "DCA", makeDate(2015, 1, 15), makeDate(2015, 1, 20));
		Activity DCB = makeActivity(0, "DCB", makeDate(2015, 1, 15), makeDate(2015, 1, 25));
		Activity DCC = makeActivity(0, "DCC", makeDate(2015, 1, 15), makeDate(2015, 1, 25));
		Activity UTAB = makeActivity(0, "UTAB", makeDate(2015, 1, 25), makeDate(2015, 2, 8));
		Activity UTC = makeActivity(0, "UTC", makeDate(2015, 1, 25), makeDate(2015, 2, 2));
		Activity P = makeActivity(0, "P", makeDate(2015, 2, 8), makeDate(2015, 2, 18));
		Activity IST = makeActivity(0, "IST", makeDate(2015, 2, 18), makeDate(2015, 2, 24));
//		Activity AF = makeActivity(0, "B", makeDate(2015, 2, 18), makeDate(2015, 3, 5));
		
		ase.addActivity(S);
//		ase.addActivity(AS);
		ase.addActivity(DCA);
		ase.addActivity(DCB);
		ase.addActivity(DCC);
		ase.addActivity(UTAB);
		ase.addActivity(UTC);
		ase.addActivity(P);
		ase.addActivity(IST);
//		ase.addActivity(AF);
		
		ase.addDependency(S.getId(), DCA.getId());
		ase.addDependency(S.getId(), DCB.getId());
		ase.addDependency(S.getId(), DCC.getId());
//		ase.addDependency(AS.getId(), DCC.getId());
		ase.addDependency(DCA.getId(), UTAB.getId());
		ase.addDependency(DCB.getId(), UTAB.getId());
		ase.addDependency(DCC.getId(), UTC.getId());
		ase.addDependency(UTAB.getId(), P.getId());
		ase.addDependency(UTC.getId(), P.getId());
		ase.addDependency(P.getId(), IST.getId());
//		ase.addDependency(P.getId(), AF.getId());

		List<Integer> startNodes = ase.calculateNumberOfStartingNodes(new ArrayList<Integer>(), ase.getActivities().get(4).getId());
		List<Integer> endNodes = ase.calculateNumberOfEndingNodes(new ArrayList<Integer>(), ase.getActivities().get(4).getId());
		
		ase.calculateAllParamsOfChain(startNodes.get(0), endNodes.get(0));
	}
	
	@Test
	public void testCalculateAllParamsOfChain_testDurations()	{
		testCalculateAllParamsOfChain_setup();
		
//		Visual test
//		for (int i = 0; i < ase.getActivities().size(); i++)
//		{
//			Activity tempActivity = ase.getActivity(i);
//			System.out.println(tempActivity.getLabel() + " " + tempActivity.getDuration().toString());
//		}
		
//		Autoboxing causes ambiguity, Double.valueOf(double) needed; assertEquals(ase.getActivities().get(0).getDuration(), 14) not accepted 
		assertEquals(Double.valueOf(14), Double.valueOf(ase.getActivities().get(0).getDuration()));
		assertEquals(Double.valueOf(5), Double.valueOf(ase.getActivities().get(1).getDuration()));
		assertEquals(Double.valueOf(10), Double.valueOf(ase.getActivities().get(2).getDuration()));
		assertEquals(Double.valueOf(10), Double.valueOf(ase.getActivities().get(3).getDuration()));
		assertEquals(Double.valueOf(14), Double.valueOf(ase.getActivities().get(4).getDuration()));
		assertEquals(Double.valueOf(8), Double.valueOf(ase.getActivities().get(5).getDuration()));
		assertEquals(Double.valueOf(10), Double.valueOf(ase.getActivities().get(6).getDuration()));
		assertEquals(Double.valueOf(6), Double.valueOf(ase.getActivities().get(7).getDuration()));
	}
	
	@Test
	public void testCalculateAllParamsOfChain_testFloats()	{
		testCalculateAllParamsOfChain_setup();
		
//		Visual test
//		for (int i = 0; i < ase.getActivities().size(); i++)
//		{
//			Activity tempActivity = ase.getActivity(i);
//			System.out.println(tempActivity.getLabel() + " " + tempActivity.getFloat().toString());
//		}
		
//		Autoboxing causes ambiguity, Double.valueOf(double) needed; assertEquals(ase.getActivities().get(0).getFloat(), 0) not accepted 
		assertEquals(Double.valueOf(0), Double.valueOf(ase.getActivities().get(0).getFloat()));
		assertEquals(Double.valueOf(5), Double.valueOf(ase.getActivities().get(1).getFloat()));
		assertEquals(Double.valueOf(0), Double.valueOf(ase.getActivities().get(2).getFloat()));
		assertEquals(Double.valueOf(6), Double.valueOf(ase.getActivities().get(3).getFloat()));
		assertEquals(Double.valueOf(0), Double.valueOf(ase.getActivities().get(4).getFloat()));
		assertEquals(Double.valueOf(6), Double.valueOf(ase.getActivities().get(5).getFloat()));
		assertEquals(Double.valueOf(0), Double.valueOf(ase.getActivities().get(6).getFloat()));
		assertEquals(Double.valueOf(0), Double.valueOf(ase.getActivities().get(7).getFloat()));
	}
	
	@Test
	public void testCalculateAllParamsOfChain_testMaxDurations()	{
		testCalculateAllParamsOfChain_setup();
		
//		Visual test
//		for (int i = 0; i < ase.getActivities().size(); i++)
//		{
//			Activity tempActivity = ase.getActivity(i);
//			System.out.println(tempActivity.getLabel() + " " + tempActivity.getMaxDuration().toString());
//		}

//		Autoboxing causes ambiguity, Double.valueOf(double) needed; assertEquals(ase.getActivities().get(0).getMaxDuration(), 14) not accepted
		assertEquals(Double.valueOf(14), Double.valueOf(ase.getActivities().get(0).getMaxDuration()));
		assertEquals(Double.valueOf(10), Double.valueOf(ase.getActivities().get(1).getMaxDuration()));
		assertEquals(Double.valueOf(10), Double.valueOf(ase.getActivities().get(2).getMaxDuration()));
		assertEquals(Double.valueOf(16), Double.valueOf(ase.getActivities().get(3).getMaxDuration()));
		assertEquals(Double.valueOf(14), Double.valueOf(ase.getActivities().get(4).getMaxDuration()));
		assertEquals(Double.valueOf(14), Double.valueOf(ase.getActivities().get(5).getMaxDuration()));
		assertEquals(Double.valueOf(10), Double.valueOf(ase.getActivities().get(6).getMaxDuration()));
		assertEquals(Double.valueOf(6), Double.valueOf(ase.getActivities().get(7).getMaxDuration()));
	}
	
	@Test
	public void testCalculateAllParamsOfChain_testEarliestStarts()	{
		testCalculateAllParamsOfChain_setup();
		
//		Visual test
//		for (int i = 0; i < ase.getActivities().size(); i++)
//		{
//			Activity tempActivity = ase.getActivity(i);
//			System.out.println(tempActivity.getLabel() + " " + tempActivity.getEarliestStart().toString());
//		}

//		Autoboxing causes ambiguity, Double.valueOf(double) needed; assertEquals(ase.getActivities().get(0).getEarliestStart(), 120150101) not accepted
		assertEquals(Double.valueOf(20150101), Double.valueOf(ase.getActivities().get(0).getEarliestStart()));
		assertEquals(Double.valueOf(20150115), Double.valueOf(ase.getActivities().get(1).getEarliestStart()));
		assertEquals(Double.valueOf(20150115), Double.valueOf(ase.getActivities().get(2).getEarliestStart()));
		assertEquals(Double.valueOf(20150115), Double.valueOf(ase.getActivities().get(3).getEarliestStart()));
		assertEquals(Double.valueOf(20150125), Double.valueOf(ase.getActivities().get(4).getEarliestStart()));
		assertEquals(Double.valueOf(20150125), Double.valueOf(ase.getActivities().get(5).getEarliestStart()));
		assertEquals(Double.valueOf(20150208), Double.valueOf(ase.getActivities().get(6).getEarliestStart()));
		assertEquals(Double.valueOf(20150218), Double.valueOf(ase.getActivities().get(7).getEarliestStart()));
	}
	
	@Test
	public void testCalculateAllParamsOfChain_testEarliestFinishes()	{
		testCalculateAllParamsOfChain_setup();
		
//		Visual test
//		for (int i = 0; i < ase.getActivities().size(); i++)
//		{
//			Activity tempActivity = ase.getActivity(i);
//			System.out.println(tempActivity.getLabel() + " " + tempActivity.getEarliestFinish().toString());
//		}

//		Autoboxing causes ambiguity, Double.valueOf(double) needed; assertEquals(ase.getActivities().get(0).getEarliestFinish(), 20150115) not accepted
		assertEquals(Double.valueOf(20150115), Double.valueOf(ase.getActivities().get(0).getEarliestFinish()));
		assertEquals(Double.valueOf(20150120), Double.valueOf(ase.getActivities().get(1).getEarliestFinish()));
		assertEquals(Double.valueOf(20150125), Double.valueOf(ase.getActivities().get(2).getEarliestFinish()));
		assertEquals(Double.valueOf(20150125), Double.valueOf(ase.getActivities().get(3).getEarliestFinish()));
		assertEquals(Double.valueOf(20150208), Double.valueOf(ase.getActivities().get(4).getEarliestFinish()));
		assertEquals(Double.valueOf(20150202), Double.valueOf(ase.getActivities().get(5).getEarliestFinish()));
		assertEquals(Double.valueOf(20150218), Double.valueOf(ase.getActivities().get(6).getEarliestFinish()));
		assertEquals(Double.valueOf(20150224), Double.valueOf(ase.getActivities().get(7).getEarliestFinish()));
	}

	@Test
	public void testCalculateAllParamsOfChain_testLatestStarts()	{
		testCalculateAllParamsOfChain_setup();
		
//		Visual test
//		for (int i = 0; i < ase.getActivities().size(); i++)
//		{
//			Activity tempActivity = ase.getActivity(i);
//			System.out.println(tempActivity.getLabel() + " " + tempActivity.getLatestStart().toString());
//		}

//		Autoboxing causes ambiguity, Double.valueOf(double) needed; assertEquals(ase.getActivities().get(0).getLatestStart(), 20150101) not accepted
		assertEquals(Double.valueOf(20150101), Double.valueOf(ase.getActivities().get(0).getLatestStart()));
		assertEquals(Double.valueOf(20150120), Double.valueOf(ase.getActivities().get(1).getLatestStart()));
		assertEquals(Double.valueOf(20150115), Double.valueOf(ase.getActivities().get(2).getLatestStart()));
		assertEquals(Double.valueOf(20150121), Double.valueOf(ase.getActivities().get(3).getLatestStart()));
		assertEquals(Double.valueOf(20150125), Double.valueOf(ase.getActivities().get(4).getLatestStart()));
		assertEquals(Double.valueOf(20150131), Double.valueOf(ase.getActivities().get(5).getLatestStart()));
		assertEquals(Double.valueOf(20150208), Double.valueOf(ase.getActivities().get(6).getLatestStart()));
		assertEquals(Double.valueOf(20150218), Double.valueOf(ase.getActivities().get(7).getLatestStart()));
	}	
	
	@Test
	public void testCalculateAllParamsOfChain_testLatestFinishes()	{
		testCalculateAllParamsOfChain_setup();
		
//		Visual test
//		for (int i = 0; i < ase.getActivities().size(); i++)
//		{
//			Activity tempActivity = ase.getActivity(i);
//			System.out.println(tempActivity.getLabel() + " " + tempActivity.getLatestFinish().toString());
//		}

//		Autoboxing causes ambiguity, Double.valueOf(double) needed; assertEquals(ase.getActivities().get(0).getLatestFinish(), 20150115) not accepted
		assertEquals(Double.valueOf(20150115), Double.valueOf(ase.getActivities().get(0).getLatestFinish()));
		assertEquals(Double.valueOf(20150125), Double.valueOf(ase.getActivities().get(1).getLatestFinish()));
		assertEquals(Double.valueOf(20150125), Double.valueOf(ase.getActivities().get(2).getLatestFinish()));
		assertEquals(Double.valueOf(20150131), Double.valueOf(ase.getActivities().get(3).getLatestFinish()));
		assertEquals(Double.valueOf(20150208), Double.valueOf(ase.getActivities().get(4).getLatestFinish()));
		assertEquals(Double.valueOf(20150208), Double.valueOf(ase.getActivities().get(5).getLatestFinish()));
		assertEquals(Double.valueOf(20150218), Double.valueOf(ase.getActivities().get(6).getLatestFinish()));
		assertEquals(Double.valueOf(20150224), Double.valueOf(ase.getActivities().get(7).getLatestFinish()));
	}
	
	@Test
	public void testCalculateNumberOfStartingNodes_oneStartNode() {
		ase.getActivities().clear();

		Activity S = makeActivity(0, "S", makeDate(2015, 1, 1),makeDate(2015, 1, 15));
		// Activity AS = makeActivity(0, "AS", makeDate(2015, 1, 1), makeDate(2015, 1, 11));
		Activity DCA = makeActivity(0, "DCA", makeDate(2015, 1, 15),makeDate(2015, 1, 20));
		Activity DCB = makeActivity(0, "DCB", makeDate(2015, 1, 15),makeDate(2015, 1, 25));
		Activity DCC = makeActivity(0, "DCC", makeDate(2015, 1, 15),makeDate(2015, 1, 25));
		Activity UTAB = makeActivity(0, "UTAB", makeDate(2015, 1, 25),makeDate(2015, 2, 8));
		Activity UTC = makeActivity(0, "UTC", makeDate(2015, 1, 25),makeDate(2015, 2, 2));
		Activity P = makeActivity(0, "P", makeDate(2015, 2, 8),makeDate(2015, 2, 18));
		Activity IST = makeActivity(0, "IST", makeDate(2015, 2, 18),makeDate(2015, 2, 24));
		// Activity AF = makeActivity(0, "B", makeDate(2015, 2, 18),makeDate(2015, 3, 5));

		ase.addActivity(S);
		// ase.addActivity(AS);
		ase.addActivity(DCA);
		ase.addActivity(DCB);
		ase.addActivity(DCC);
		ase.addActivity(UTAB);
		ase.addActivity(UTC);
		ase.addActivity(P);
		ase.addActivity(IST);
		// ase.addActivity(AF);

		ase.addDependency(S.getId(), DCA.getId());
		ase.addDependency(S.getId(), DCB.getId());
		ase.addDependency(S.getId(), DCC.getId());
		// ase.addDependency(AS.getId(), DCC.getId());
		ase.addDependency(DCA.getId(), UTAB.getId());
		ase.addDependency(DCB.getId(), UTAB.getId());
		ase.addDependency(DCC.getId(), UTC.getId());
		ase.addDependency(UTAB.getId(), P.getId());
		ase.addDependency(UTC.getId(), P.getId());
		ase.addDependency(P.getId(), IST.getId());
		// ase.addDependency(P.getId(), AF.getId());

		List<Integer> startNodes = ase.calculateNumberOfStartingNodes(new ArrayList<Integer>(), ase.getActivities().get(5).getId());
		assertEquals(startNodes.size(), 1);
	}
	
	@Test
	public void testCalculateNumberOfStartingNodes_multipleStartNodes() {
		ase.getActivities().clear();

		Activity S = makeActivity(0, "S", makeDate(2015, 1, 1),makeDate(2015, 1, 15));
		Activity SA = makeActivity(0, "SA", makeDate(2015, 1, 1),makeDate(2015, 1, 15));
		Activity DCA = makeActivity(0, "DCA", makeDate(2015, 1, 15),makeDate(2015, 1, 20));
		Activity DCB = makeActivity(0, "DCB", makeDate(2015, 1, 15),makeDate(2015, 1, 25));
		Activity DCC = makeActivity(0, "DCC", makeDate(2015, 1, 15),makeDate(2015, 1, 25));
		Activity UTAB = makeActivity(0, "UTAB", makeDate(2015, 1, 25),makeDate(2015, 2, 8));
		Activity UTC = makeActivity(0, "UTC", makeDate(2015, 1, 25),makeDate(2015, 2, 2));
		Activity P = makeActivity(0, "P", makeDate(2015, 2, 8),makeDate(2015, 2, 18));
		Activity IST = makeActivity(0, "IST", makeDate(2015, 2, 18),makeDate(2015, 2, 24));

		ase.addActivity(S);
		ase.addActivity(SA);
		ase.addActivity(DCA);
		ase.addActivity(DCB);
		ase.addActivity(DCC);
		ase.addActivity(UTAB);
		ase.addActivity(UTC);
		ase.addActivity(P);
		ase.addActivity(IST);

		ase.addDependency(S.getId(), DCC.getId());
		ase.addDependency(SA.getId(), DCC.getId());
		ase.addDependency(DCA.getId(), UTAB.getId());
		ase.addDependency(DCB.getId(), UTAB.getId());
		ase.addDependency(DCC.getId(), UTAB.getId());
		ase.addDependency(DCC.getId(), UTC.getId());
		ase.addDependency(UTAB.getId(), P.getId());
		ase.addDependency(UTC.getId(), P.getId());
		ase.addDependency(P.getId(), IST.getId());

		List<Integer> startNodes = ase.calculateNumberOfStartingNodes(new ArrayList<Integer>(), ase.getActivities().get(5).getId());
		assertEquals(startNodes.size(), 4);
		
		List<Integer> endNodes = ase.calculateNumberOfEndingNodes(new ArrayList<Integer>(), ase.getActivities().get(4).getId());
	}
	
	@Test
	public void testCalculateNumberOfEndNodes_convergeOnOneEndNode() {
		ase.getActivities().clear();

		Activity S = makeActivity(0, "S", makeDate(2015, 1, 1),makeDate(2015, 1, 15));
		Activity SA = makeActivity(0, "SA", makeDate(2015, 1, 1),makeDate(2015, 1, 15));
		Activity DCA = makeActivity(0, "DCA", makeDate(2015, 1, 15),makeDate(2015, 1, 20));
		Activity DCB = makeActivity(0, "DCB", makeDate(2015, 1, 15),makeDate(2015, 1, 25));
		Activity DCC = makeActivity(0, "DCC", makeDate(2015, 1, 15),makeDate(2015, 1, 25));
		Activity UTAB = makeActivity(0, "UTAB", makeDate(2015, 1, 25),makeDate(2015, 2, 8));
		Activity UTC = makeActivity(0, "UTC", makeDate(2015, 1, 25),makeDate(2015, 2, 2));
		Activity P = makeActivity(0, "P", makeDate(2015, 2, 8),makeDate(2015, 2, 18));
		Activity IST = makeActivity(0, "IST", makeDate(2015, 2, 18),makeDate(2015, 2, 24));

		ase.addActivity(S);
		ase.addActivity(SA);
		ase.addActivity(DCA);
		ase.addActivity(DCB);
		ase.addActivity(DCC);
		ase.addActivity(UTAB);
		ase.addActivity(UTC);
		ase.addActivity(P);
		ase.addActivity(IST);

		ase.addDependency(S.getId(), DCC.getId());
		ase.addDependency(SA.getId(), DCC.getId());
		ase.addDependency(DCA.getId(), UTAB.getId());
		ase.addDependency(DCB.getId(), UTAB.getId());
		ase.addDependency(DCC.getId(), UTAB.getId());
		ase.addDependency(DCC.getId(), UTC.getId());
		ase.addDependency(UTAB.getId(), P.getId());
		ase.addDependency(UTC.getId(), P.getId());
		ase.addDependency(P.getId(), IST.getId());
		
		Integer endId = IST.getId();

		//test from several starting points. all should converge on one node
		//S
		List<Integer> endNodes = ase.calculateNumberOfEndingNodes(new ArrayList<Integer>(), ase.getActivities().get(0).getId());
		assertEquals(endNodes.size(), 1);
		assertEquals(endNodes.get(0), endId);
		
		//SA
		endNodes = ase.calculateNumberOfEndingNodes(new ArrayList<Integer>(), ase.getActivities().get(1).getId());
		assertEquals(endNodes.size(), 1);
		assertEquals(endNodes.get(0), endId);
		
		//DCA
		endNodes = ase.calculateNumberOfEndingNodes(new ArrayList<Integer>(), ase.getActivities().get(2).getId());
		assertEquals(endNodes.size(), 1);
		assertEquals(endNodes.get(0), endId);
		
		//one middle node
		endNodes = ase.calculateNumberOfEndingNodes(new ArrayList<Integer>(), ase.getActivities().get(6).getId());
		assertEquals(endNodes.size(), 1);
		assertEquals(endNodes.get(0), endId);
		
		//end node
		endNodes = ase.calculateNumberOfEndingNodes(new ArrayList<Integer>(), ase.getActivities().get(8).getId());
		assertEquals(endNodes.size(), 1);
		assertEquals(endNodes.get(0), endId);
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
