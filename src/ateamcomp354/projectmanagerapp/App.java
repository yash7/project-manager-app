package ateamcomp354.projectmanagerapp;

import java.awt.EventQueue;
import java.sql.Connection;
import java.util.List;

import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.impl.ApplicationContextImpl;
import ateamcomp354.projectmanagerapp.dataAccess.DatabaseManager;
import ateamcomp354.projectmanagerapp.ui.MainFrame;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UsersDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.impl.DSL;

public class App {

	public static void main(String[] args) throws Exception {

		DatabaseManager db = new DatabaseManager();
		Connection c = db.getOpenConnection();

		ApplicationContext appCtx = getApplicationContext( c );

		sampleData( c, appCtx );

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrame( appCtx );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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

		if ( usersDao.fetchByUsername( "jdoe"  ).isEmpty() ) {
			usersDao.insert( new Users( null, "John", "Doe", "jdoe", "top!secret", 1 ) );
		}

		Users jdoe = usersDao.fetchByUsername( "jdoe"  ).get( 0 );

		if ( usersDao.fetchByUsername( "ssmith"  ).isEmpty() ) {
			usersDao.insert( new Users( null, "Sarah", "Smith", "ssmith", "super_secret", 0 ) );
		}

		Users ssmith = usersDao.fetchByUsername( "ssmith"  ).get( 0 );

		if ( projectDao.fetchByProjectName( "The Awesome Project" ).isEmpty() ) {
			projectDao.insert( new Project( null, "The Awesome Project", jdoe.getId(), "This project is awesome." ) );
		}

		Project project = projectDao.fetchByProjectName( "The Awesome Project" ).get( 0 );

		ActivityService activityService = appCtx.getActivityService( project.getId() );

		if ( activityService.getActivities().isEmpty() ) {

			Activity a1 = new Activity( null, project.getId(), Status.NEW.ordinal(), 0, 0, "The cool activity", 0, 0, 0, 0, "This activity is for cool people to do." );
			Activity a2 = new Activity( null, project.getId(), Status.NEW.ordinal(), 0, 0, "The ugly activity", 0, 0, 0, 0, "This activity that no one wants to do." );

			activityService.addActivity( a1 );
			activityService.addActivity( a2 );
		}
	}
}
