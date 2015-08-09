package ateamcomp354.projectmanagerapp.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.ActivityService;

import ateamcomp354.projectmanagerapp.ui.util.Charts;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import static ateamcomp354.projectmanagerapp.testing.util.PojoMaker.*;

public class PERTChartTest extends AbstractDatabaseTest{

	@Test
	public void testSimpleProject() {
		ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
		
		ProjectService pjs = appCtx.getProjectService();
		
		Project p = new Project();
		p.setId(0);
		p.setProjectName("Test Project");
		p.setDescription("Test Project");
		
		pjs.addProject(p);
		
		ActivityService ase = appCtx.getActivityService(0);
		
		ase.getActivities().clear();
		
		Activity A = makeActivity(0, "A", makeDate(2015, 1, 1), makeDate(2015, 1, 4));
		Activity B = makeActivity(0, "B", makeDate(2015, 1, 4), makeDate(2015, 1, 7));
		Activity C = makeActivity(0, "C", makeDate(2015, 1, 7), makeDate(2015, 1, 10));
		
		A.setDuration(3);
		B.setDuration(3);
		C.setDuration(3);
		
		ase.addActivity(A);
		ase.addActivity(B);
		ase.addActivity(C);
		
		ase.addDependency(A.getId(), B.getId());
		ase.addDependency(B.getId(), C.getId());
		
		Charts.viewPERTChart(appCtx, pjs.getProject(0));
	}

}
