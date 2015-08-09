package ateamcomp354.projectmanagerapp.testing;

import static org.junit.Assert.*;

import javax.swing.JOptionPane;

import org.junit.Test;

import java.util.stream.Stream;

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
		
		A.setId(1);
		B.setId(2);
		C.setId(3);
		
		ase.addActivity(A);
		ase.addActivity(B);
		ase.addActivity(C);
		
		ase.addDependency(A.getId(), B.getId());
		ase.addDependency(B.getId(), C.getId());
		
		Charts.viewPERTChart(appCtx, pjs.getProject(0));
		
		assertDisplayedProperly("testSimpleProject", "Single Path Project");
	}
	
	// test empty project
	
	// Test example project from lecture
	@Test
	public void testLectureProject()	{
		ApplicationContext appCtx = App.getApplicationContext(db.getConnection());
		
		ProjectService pjs = appCtx.getProjectService();
		
		Project p = new Project();
		p.setId(0);
		p.setProjectName("Test Project");
		p.setDescription("Test Project");
		
		pjs.addProject(p);
		
		ActivityService ase = appCtx.getActivityService(0);
		
		ase.getActivities().clear();
		
		Activity A = makeActivity(0, "A", makeDate(2015, 1, 1), makeDate(2015, 1, 7));
		Activity B = makeActivity(0, "B", makeDate(2015, 1, 1), makeDate(2015, 1, 5));
		Activity C = makeActivity(0, "C", makeDate(2015, 1, 7), makeDate(2015, 1, 10));
//		Activity D = makeActivity(0, "D", makeDate(2015, 1, 5), makeDate(2015, 1, 9));
//		Activity E = makeActivity(0, "E", makeDate(2015, 1, 5), makeDate(2015, 1, 8));
//		Activity F = makeActivity(0, "F", makeDate(2015, 1, 1), makeDate(2015, 1, 11));
//		Activity G = makeActivity(0, "G", makeDate(2015, 1, 11), makeDate(2015, 1, 14));
//		Activity H = makeActivity(0, "H", makeDate(2015, 1, 10), makeDate(2015, 1, 12));
		
		A.setId(1);
		B.setId(2);
		C.setId(3);
//		D.setId(4);
//		E.setId(5);
//		F.setId(6);
//		G.setId(7);
//		H.setId(8);
		
//		Stream.of(A, B, C, D, E, F, G, H).forEach(ase::addActivity);
		Stream.of(A, B, C).forEach(ase::addActivity);

		System.out.println(ase.getActivities().size());
		
		ase.addDependency(A.getId(), C.getId());
		ase.addDependency(B.getId(), C.getId());
//		ase.addDependency(B.getId(), D.getId());
//		ase.addDependency(B.getId(), E.getId());
//		ase.addDependency(E.getId(), G.getId());
//		ase.addDependency(F.getId(), G.getId());
//		ase.addDependency(C.getId(), H.getId());
//		ase.addDependency(D.getId(), H.getId());
		
		Charts.viewPERTChart(appCtx, pjs.getProject(0));
		
//		assertDisplayedProperly
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
