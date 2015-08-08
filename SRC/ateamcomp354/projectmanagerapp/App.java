package ateamcomp354.projectmanagerapp;

import static ateamcomp354.projectmanagerapp.testing.util.PojoMaker.makeActivity;
import static ateamcomp354.projectmanagerapp.testing.util.PojoMaker.makeDate;

import java.awt.EventQueue;
import java.sql.Connection;

import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.impl.ApplicationContextImpl;
import ateamcomp354.projectmanagerapp.dataAccess.DatabaseManager;
import ateamcomp354.projectmanagerapp.ui.MainFrame;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivityDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivitylinksDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectmembersDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UseractivitiesDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UsersDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Projectmembers;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Useractivities;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.impl.DSL;

/**
 * The project manager app starts here! run this classes main function to launch the swing application.
 */
public class App {

	public static void main(String[] args) throws Exception {

		DatabaseManager db = new DatabaseManager();
		Connection c = db.getConnection();

		ApplicationContext appCtx = getApplicationContext( c );

		if(appCtx.getUserService().getUsers().size() <= 1) {
			sampleData( c, appCtx );
		}

		EventQueue.invokeLater( () -> {
				try {
					new MainFrame( appCtx );
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		Runtime.getRuntime().addShutdownHook( new Thread( db::closeConnection ) );
	}

	/**
	 * Return the ApplicationContext, the object that abstract interacting with the database.
	 *
	 * Tests should use function to the application functionality.
	 */
	public static ApplicationContext getApplicationContext( Connection c ) {

		// This is just so jooq doesn't print its logo to stderr
		// Disabled for now because it was hard to read this programs output.
		System.setProperty( "org.jooq.no-logo", "true" );

		// The DSLContext is what we use to create sql stmts
		DSLContext create = DSL.using(c, SQLDialect.SQLITE);

		return new ApplicationContextImpl( create );
	}

	/**
	 * Fill the database with sample data if not already present.
	 *
	 * This is only for developing the app.
	 */
	private static void sampleData( Connection c, ApplicationContext appCtx ) {

		DSLContext create = DSL.using(c, SQLDialect.SQLITE);

		UsersDao usersDao = new UsersDao( create.configuration() );
		ProjectDao projectDao = new ProjectDao( create.configuration() );
		ActivityDao activityDao = new ActivityDao (create.configuration());
		UseractivitiesDao userActivitiesDao = new UseractivitiesDao (create.configuration() );
		ProjectmembersDao projectMembersDao = new ProjectmembersDao (create.configuration());

		if ( usersDao.fetchByUsername( "jdoe"  ).isEmpty() ) {
			usersDao.insert( new Users( null, "John", "Doe", "jdoe", "top!secret", true ) );
		}

		Users jdoe = usersDao.fetchByUsername( "jdoe"  ).get( 0 );

		if ( usersDao.fetchByUsername( "ssmith"  ).isEmpty() ) {
			usersDao.insert( new Users( null, "Sarah", "Smith", "ssmith", "super_secret", false ) );
		}

		Users ssmith = usersDao.fetchByUsername( "ssmith"  ).get( 0 );
		
		if ( usersDao.fetchByUsername( "cpratt"  ).isEmpty() ) {
			usersDao.insert( new Users( null, "Chris", "Pratt", "cpratt", "password", false ) );
		}

		Users cpratt = usersDao.fetchByUsername( "cpratt"  ).get( 0 );

		if ( projectDao.fetchByProjectName( "The Awesome Project" ).isEmpty() ) {
			projectDao.insert( new Project( null, "The Awesome Project", "This project is awesome.", false, 0, 0) );
			projectDao.insert(new Project( null, "Crit Path & Gantt", "Project Designed to show a Critical Path Chart and a GANTT CHART", false, 0, 0));
			projectDao.insert(new Project( null, "Ev Chart", "Project Designed to show an EV Chart", false, 200, 40));
			projectDao.insert(new Project( null, "PERT Chart", "Project Designed to show a PERT Chart", false, 0, 0));
		}

		Project p1 = projectDao.fetchByProjectName("The Awesome Project").get(0);
		Project p2 = projectDao.fetchByProjectName("Crit Path & Gantt").get(0);
		Project p3 = projectDao.fetchByProjectName("Ev Chart").get(0);
		Project p4 = projectDao.fetchByProjectName("PERT Chart").get(0);
		
		/// DEFAULT PROJECT
		
		ActivityService as1 = appCtx.getActivityService( p1.getId() );
		
		if ( as1.getActivities().isEmpty() ) {
			as1.addActivity(new Activity( null, p1.getId(), Status.NEW, 20150721, 20150721, "The cool activity", 20150722, 20150722, 1, 1, 0, "This activity is for cool people to do.", 0, 0));
			as1.addActivity(new Activity( null, p1.getId(), Status.NEW, 20150722, 20150722, "The ugly activity", 20150724, 20150724, 2, 2, 0, "This activity that no one wants to do.", 0, 0));
		}
		
		Activity as1a1 = activityDao.fetchByLabel("The cool activity").get(0);
		Activity as1a2 = activityDao.fetchByLabel("The ugly activity").get(0);
		
		if (projectMembersDao.findAll().isEmpty()) {
			projectMembersDao.insert(new Projectmembers (null, p1.getId(), ssmith.getId()));
			projectMembersDao.insert(new Projectmembers (null, p1.getId(), cpratt.getId()));
		}
		
		if ( userActivitiesDao.findAll().isEmpty()) {
			userActivitiesDao.insert(new Useractivities ( null, as1a1.getId(), ssmith.getId()));
			userActivitiesDao.insert(new Useractivities ( null, as1a2.getId(), cpratt.getId()));
		}
		
		/// ACTIVITY ON NODE PROJECT & GANTT
		
		ActivityService as2 = appCtx.getActivityService( p2.getId() );
		
		if ( as2.getActivities().isEmpty() ) {
			as2.addActivity(new Activity( null, p2.getId(), Status.RESOLVED, 20150701, 20150704, "Spec", 0, 0, 0, 0, 0, "", 0, 0));
			as2.addActivity(new Activity( null, p2.getId(), Status.RESOLVED, 20150704, 20150708, "D/C A", 0, 0, 0, 0, 0, "", 0, 0));
			as2.addActivity(new Activity( null, p2.getId(), Status.IN_PROGRESS, 20150704, 20150707, "D/C B", 0, 0, 0, 0, 0, "", 0, 0));
			as2.addActivity(new Activity( null, p2.getId(), Status.IN_PROGRESS, 20150704, 20150705, "D/C C", 0, 0, 0, 0, 0, "", 0, 0));
			as2.addActivity(new Activity( null, p2.getId(), Status.NEW, 20150708, 20150712, "UT A&B", 0, 0, 0, 0, 0, "", 0, 0));
			as2.addActivity(new Activity( null, p2.getId(), Status.NEW, 20150708, 20150710, "UT C", 0, 0, 0, 0, 0, "", 0, 0));
			as2.addActivity(new Activity( null, p2.getId(), Status.NEW, 20150712, 20150715, "Plan", 0, 0, 0, 0, 0, "", 0, 0));
			as2.addActivity(new Activity( null, p2.getId(), Status.NEW, 20150715, 20150720, "Implement", 0, 0, 0, 0, 0, "", 0, 0));
		}
		
		Activity as2a1 = activityDao.fetchByLabel("Spec").get(0);
		Activity as2a2 = activityDao.fetchByLabel("D/C A").get(0);
		Activity as2a3 = activityDao.fetchByLabel("D/C B").get(0);
		Activity as2a4 = activityDao.fetchByLabel("D/C C").get(0);
		Activity as2a5 = activityDao.fetchByLabel("UT A&B").get(0);
		Activity as2a6 = activityDao.fetchByLabel("UT C").get(0);
		Activity as2a7 = activityDao.fetchByLabel("Plan").get(0);
		Activity as2a8 = activityDao.fetchByLabel("Implement").get(0);
		
		as2.addDependency(as2a1.getId(), as2a2.getId());
		as2.addDependency(as2a1.getId(), as2a3.getId());
		as2.addDependency(as2a1.getId(), as2a4.getId());
		as2.addDependency(as2a2.getId(), as2a5.getId());
		as2.addDependency(as2a3.getId(), as2a5.getId());
		as2.addDependency(as2a4.getId(), as2a6.getId());
		as2.addDependency(as2a5.getId(), as2a7.getId());
		as2.addDependency(as2a6.getId(), as2a7.getId());
		as2.addDependency(as2a7.getId(), as2a8.getId());
		
		if (projectMembersDao.findAll().isEmpty()) {
			projectMembersDao.insert(new Projectmembers (null, p1.getId(), jdoe.getId()));
		}
		
		if ( userActivitiesDao.findAll().isEmpty()) {
			userActivitiesDao.insert(new Useractivities ( null, as2a1.getId(), jdoe.getId()));
			userActivitiesDao.insert(new Useractivities ( null, as2a2.getId(), jdoe.getId()));
			userActivitiesDao.insert(new Useractivities ( null, as2a3.getId(), jdoe.getId()));
			userActivitiesDao.insert(new Useractivities ( null, as2a4.getId(), jdoe.getId()));
			userActivitiesDao.insert(new Useractivities ( null, as2a5.getId(), jdoe.getId()));
			userActivitiesDao.insert(new Useractivities ( null, as2a6.getId(), jdoe.getId()));
			userActivitiesDao.insert(new Useractivities ( null, as2a7.getId(), jdoe.getId()));
			userActivitiesDao.insert(new Useractivities ( null, as2a8.getId(), jdoe.getId()));
		}
		
		/// EV CHART
		
		ActivityService as3 = appCtx.getActivityService( p3.getId() );
		
		if ( as3.getActivities().isEmpty() ) {
			as3.addActivity(new Activity( null, p3.getId(), Status.RESOLVED, 20150721, 20150724, "A", 0, 0, 0, 0, 0, "", 30, 40));
			as3.addActivity(new Activity( null, p3.getId(), Status.IN_PROGRESS, 20150724, 20150727, "B", 0, 0, 0, 0, 0, "", 20, 0));
			as3.addActivity(new Activity( null, p3.getId(), Status.IN_PROGRESS, 20150724, 20150726, "C", 0, 0, 0, 0, 0, "", 10, 0));
			as3.addActivity(new Activity( null, p3.getId(), Status.NEW, 20150727, 20150729, "D", 0, 0, 0, 0, 0, "", 45, 0));
			as3.addActivity(new Activity( null, p3.getId(), Status.NEW, 20150729, 20150730, "E", 0, 0, 0, 0, 0, "", 95, 0));
		}
		
		Activity as3a1 = activityDao.fetchByLabel("A").get(0);
		Activity as3a2 = activityDao.fetchByLabel("B").get(0);
		Activity as3a3 = activityDao.fetchByLabel("C").get(0);
		Activity as3a4 = activityDao.fetchByLabel("D").get(0);
		Activity as3a5 = activityDao.fetchByLabel("E").get(0);
		
		if (projectMembersDao.findAll().isEmpty()) {
			projectMembersDao.insert(new Projectmembers (null, p3.getId(), ssmith.getId()));
		}
		
		as3.addDependency(as3a1.getId(), as3a2.getId());
		as3.addDependency(as3a1.getId(), as3a3.getId());
		as3.addDependency(as3a2.getId(), as3a4.getId());
		as3.addDependency(as3a3.getId(), as3a4.getId());
		as3.addDependency(as3a4.getId(), as3a5.getId());
	}
}
