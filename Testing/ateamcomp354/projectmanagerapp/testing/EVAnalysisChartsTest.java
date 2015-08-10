package ateamcomp354.projectmanagerapp.testing;


import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.model.Status;
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

import org.junit.Before;

import static ateamcomp354.projectmanagerapp.testing.util.PojoMaker.*;
import static ateamcomp354.projectmanagerapp.testing.util.ActivityServiceUtil.*;

public class EVAnalysisChartsTest extends AbstractDatabaseTest{
	
	@Test
	public void testEmptyProject()	{
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection() );
		
		ProjectService pjs = appCtx.getProjectService();
		
		Project p = new Project();
		p.setId(0);
		p.setProjectName("Test Project");
		p.setDescription("Test Project");
		
		pjs.addProject(p);
		
		ActivityService ase = appCtx.getActivityService(0);
		
		ase.getActivities().clear();
		
		calculateAllParams(appCtx.getActivityService(0));
		
		Charts.viewEVAnalysisChart(appCtx, pjs.getProject(0));
		
		assertDisplayedProperly("testEmptyProject", "Message dialog informing there are no activities in the project.");
	}
	
	@Test
	public void testProjectWithMixedA()	{
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection() );
		
		ProjectService pjs = appCtx.getProjectService();
		
		Project p = new Project();
		p.setId(0);
		p.setProjectName("Test Project");
		p.setDescription("Test Project");
		
		pjs.addProject(p);
		
		ActivityService ase = appCtx.getActivityService(0);
		
		ase.getActivities().clear();
		
		Activity S = makeActivity(0, "S", makeDate(2015, 1, 1), makeDate(2015, 1, 10));
		Activity DCA = makeActivity(0, "DCA", makeDate(2015, 1, 10), makeDate(2015, 1, 20));
		Activity DCB = makeActivity(0, "DCB", makeDate(2015, 1, 10), makeDate(2015, 1, 20));
		Activity DCC = makeActivity(0, "DCC", makeDate(2015, 1, 20), makeDate(2015, 1, 25));
		
		S.setActualCost(40);
		DCA.setActualCost(100);
		
		S.setPlannedValue(30);
		DCA.setPlannedValue(20);
		DCB.setPlannedValue(20);
		DCC.setPlannedValue(50);
		
		S.setStatus(Status.RESOLVED);
		DCA.setStatus(Status.RESOLVED);
		DCB.setStatus(Status.IN_PROGRESS);
		DCC.setStatus(Status.IN_PROGRESS);
		
		ase.addActivity(S);
		ase.addActivity(DCA);
		ase.addActivity(DCB);
		ase.addActivity(DCC);
		
		ase.addDependency(S.getId(), DCA.getId());
		ase.addDependency(S.getId(), DCB.getId());
		ase.addDependency(DCA.getId(), DCC.getId());
		ase.addDependency(DCB.getId(), DCC.getId());
		
		calculateAllParams(appCtx.getActivityService(0));
		
		Charts.viewEVAnalysisChart(appCtx, pjs.getProject(0));
		
		assertDisplayedProperly("testProjectWithMixedA", "Project with all mixed status activities (resolved prior).");
	}
	
	@Test
	public void testProjectWithMixedB()	{
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection() );
		
		ProjectService pjs = appCtx.getProjectService();
		
		Project p = new Project();
		p.setId(0);
		p.setProjectName("Test Project");
		p.setDescription("Test Project");
		
		pjs.addProject(p);
		
		ActivityService ase = appCtx.getActivityService(0);
		
		ase.getActivities().clear();
		
		Activity S = makeActivity(0, "S", makeDate(2015, 1, 1), makeDate(2015, 1, 10));
		Activity DCA = makeActivity(0, "DCA", makeDate(2015, 1, 10), makeDate(2015, 1, 20));
		Activity DCB = makeActivity(0, "DCB", makeDate(2015, 1, 10), makeDate(2015, 1, 20));
		Activity DCC = makeActivity(0, "DCC", makeDate(2015, 1, 20), makeDate(2015, 1, 25));
		
		S.setActualCost(40);
		DCA.setActualCost(100);
		
		S.setPlannedValue(30);
		DCA.setPlannedValue(20);
		DCB.setPlannedValue(20);
		DCC.setPlannedValue(50);
		
		S.setStatus(Status.IN_PROGRESS);
		DCA.setStatus(Status.IN_PROGRESS);
		DCB.setStatus(Status.RESOLVED);
		DCC.setStatus(Status.RESOLVED);
		
		ase.addActivity(S);
		ase.addActivity(DCA);
		ase.addActivity(DCB);
		ase.addActivity(DCC);
		
		ase.addDependency(S.getId(), DCA.getId());
		ase.addDependency(S.getId(), DCB.getId());
		ase.addDependency(DCA.getId(), DCC.getId());
		ase.addDependency(DCB.getId(), DCC.getId());
		
		calculateAllParams(appCtx.getActivityService(0));
		
		Charts.viewEVAnalysisChart(appCtx, pjs.getProject(0));
		
		assertDisplayedProperly("testProjectWithMixedB", "Project with all mixed status activities (resolved post).");
	}
	
	@Test
	public void testProjectWithAllNew()	{
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection() );
		
		ProjectService pjs = appCtx.getProjectService();
		
		Project p = new Project();
		p.setId(0);
		p.setProjectName("Test Project");
		p.setDescription("Test Project");
		
		pjs.addProject(p);
		
		ActivityService ase = appCtx.getActivityService(0);
		
		ase.getActivities().clear();
		
		Activity S = makeActivity(0, "S", makeDate(2015, 1, 1), makeDate(2015, 1, 10));
		Activity DCA = makeActivity(0, "DCA", makeDate(2015, 1, 10), makeDate(2015, 1, 20));
		Activity DCB = makeActivity(0, "DCB", makeDate(2015, 1, 10), makeDate(2015, 1, 20));
		Activity DCC = makeActivity(0, "DCC", makeDate(2015, 1, 20), makeDate(2015, 1, 25));
		
		S.setActualCost(40);
		DCA.setActualCost(100);
		
		S.setPlannedValue(30);
		DCA.setPlannedValue(20);
		DCB.setPlannedValue(100);
		DCC.setPlannedValue(50);
		
		ase.addActivity(S);
		ase.addActivity(DCA);
		ase.addActivity(DCB);
		ase.addActivity(DCC);
		
		ase.addDependency(S.getId(), DCA.getId());
		ase.addDependency(S.getId(), DCB.getId());
		ase.addDependency(DCA.getId(), DCC.getId());
		ase.addDependency(DCB.getId(), DCC.getId());
		
		calculateAllParams(appCtx.getActivityService(0));
		
		Charts.viewEVAnalysisChart(appCtx, pjs.getProject(0));
		
		assertDisplayedProperly("testProjectWithAllNew", "Project with all activities new.");
	}
	
	@Test
	public void testProjectWithAllInProgress()	{
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection() );
		
		ProjectService pjs = appCtx.getProjectService();
		
		Project p = new Project();
		p.setId(0);
		p.setProjectName("Test Project");
		p.setDescription("Test Project");
		
		pjs.addProject(p);
		
		ActivityService ase = appCtx.getActivityService(0);
		
		ase.getActivities().clear();
		
		Activity S = makeActivity(0, "S", makeDate(2015, 1, 1), makeDate(2015, 1, 10));
		Activity DCA = makeActivity(0, "DCA", makeDate(2015, 1, 10), makeDate(2015, 1, 20));
		Activity DCB = makeActivity(0, "DCB", makeDate(2015, 1, 10), makeDate(2015, 1, 20));
		Activity DCC = makeActivity(0, "DCC", makeDate(2015, 1, 20), makeDate(2015, 1, 25));
		
		S.setActualCost(40);
		DCA.setActualCost(100);
		
		S.setPlannedValue(30);
		DCA.setPlannedValue(20);
		DCB.setPlannedValue(100);
		DCC.setPlannedValue(50);
		
		S.setStatus(Status.IN_PROGRESS);
		DCA.setStatus(Status.IN_PROGRESS);
		DCB.setStatus(Status.IN_PROGRESS);
		DCC.setStatus(Status.IN_PROGRESS);
		
		ase.addActivity(S);
		ase.addActivity(DCA);
		ase.addActivity(DCB);
		ase.addActivity(DCC);
		
		ase.addDependency(S.getId(), DCA.getId());
		ase.addDependency(S.getId(), DCB.getId());
		ase.addDependency(DCA.getId(), DCC.getId());
		ase.addDependency(DCB.getId(), DCC.getId());
		
		calculateAllParams(appCtx.getActivityService(0));
		
		Charts.viewEVAnalysisChart(appCtx, pjs.getProject(0));
		
		assertDisplayedProperly("testProjectWithAllInProgress", "Project with all activities in progress.");
	}
	
	@Test
	public void testProjectWithAllResolved()	{
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection() );
		
		ProjectService pjs = appCtx.getProjectService();
		
		Project p = new Project();
		p.setId(0);
		p.setProjectName("Test Project");
		p.setDescription("Test Project");
		
//		p.setActualCostAtCompletion(40);
//		p.setBudgetAtCompletion(30);
		
		pjs.addProject(p);
		
		ActivityService ase = appCtx.getActivityService(0);
		
		ase.getActivities().clear();
		
		Activity S = makeActivity(0, "S", makeDate(2015, 1, 1), makeDate(2015, 1, 10));
		Activity DCA = makeActivity(0, "DCA", makeDate(2015, 1, 10), makeDate(2015, 1, 20));
		Activity DCB = makeActivity(0, "DCB", makeDate(2015, 1, 10), makeDate(2015, 1, 20));
		Activity DCC = makeActivity(0, "DCC", makeDate(2015, 1, 20), makeDate(2015, 1, 25));
		
		S.setActualCost(40);
		DCA.setActualCost(100);
		DCB.setActualCost(20);
		DCC.setActualCost(30);
		
		S.setPlannedValue(30);
		DCA.setPlannedValue(20);
		DCB.setPlannedValue(100);
		DCC.setPlannedValue(50);
		
		S.setStatus(Status.RESOLVED);
		DCA.setStatus(Status.RESOLVED);
		DCB.setStatus(Status.RESOLVED);
		DCC.setStatus(Status.RESOLVED);
		
		ase.addActivity(S);
		ase.addActivity(DCA);
		ase.addActivity(DCB);
		ase.addActivity(DCC);
		
		ase.addDependency(S.getId(), DCA.getId());
		ase.addDependency(S.getId(), DCB.getId());
		ase.addDependency(DCA.getId(), DCC.getId());
		ase.addDependency(DCB.getId(), DCC.getId());
	
		calculateAllParams(appCtx.getActivityService(0));
		
		Charts.viewEVAnalysisChart(appCtx, pjs.getProject(0));
		
		assertDisplayedProperly("testProjectWithAllResolved", "Project with all activities resolved.");
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