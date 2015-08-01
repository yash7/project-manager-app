package ateamcomp354.projectmanagerapp;

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
		}

		Project project = projectDao.fetchByProjectName( "The Awesome Project" ).get( 0 );

		ActivityService activityService = appCtx.getActivityService( project.getId() );

		if ( activityService.getActivities().isEmpty() ) {
			activityService.addActivity(new Activity( null, project.getId(), Status.NEW, 20150721, 20150721, "The cool activity", 20150722, 20150722, 1, 1, 0, "This activity is for cool people to do.", 0, 0));
			activityService.addActivity(new Activity( null, project.getId(), Status.NEW, 20150722, 20150722, "The ugly activity", 20150724, 20150724, 2, 2, 0, "This activity that no one wants to do.", 0, 0));
		}
		
		Activity a1 = activityDao.fetchByLabel("The cool activity").get(0);
		Activity a2 = activityDao.fetchByLabel("The ugly activity").get(0);
		
		if ( userActivitiesDao.findAll().isEmpty()) {
			userActivitiesDao.insert(new Useractivities ( null, a1.getId(), ssmith.getId()));
			userActivitiesDao.insert(new Useractivities ( null, a1.getId(), cpratt.getId()));
		}
		
		if (projectMembersDao.findAll().isEmpty()) {
			projectMembersDao.insert(new Projectmembers (null, project.getId(), ssmith.getId()));
			projectMembersDao.insert(new Projectmembers (null, project.getId(), cpratt.getId()));
		}
		
	}
}
