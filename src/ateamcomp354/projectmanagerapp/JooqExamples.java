package ateamcomp354.projectmanagerapp;

import ateamcomp354.projectmanagerapp.dataAccess.DatabaseManager;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UsersDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.ateamcomp354.projectmanagerapp.tables.records.UsersRecord;
import org.jooq.impl.DSL;

import java.io.File;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.jooq.ateamcomp354.projectmanagerapp.Tables.USERS;

public class JooqExamples {

	public static void main(String[] args) throws Exception {

		new File( "jooq-examples.db" ).delete();

		DatabaseManager db = new DatabaseManager( "jooq-examples.db" );
		Connection c = db.getOpenConnection();

		// This is just so jooq doesn't print its logo to stderr
		// Disabled for now because it was hard to read this programs output.
		System.setProperty( "org.jooq.no-logo", "true" );

		// The DSLContext is what we use to create sql stmts
		DSLContext create = DSL.using( c, SQLDialect.SQLITE );
		
		approach1( create );
		approach2( create );
		approach3( create );
	}

	private static void approach1( DSLContext create ) {

		System.out.println( "=== This is method 1 of jooq ===" );

		// we assume we are at some point in the program were we have a list of users.
		// for this example lets create that list.
		DumbUser user1 = new DumbUser( null, "Dumb", "John" );
		DumbUser user2 = new DumbUser( null, "DumbDumb", "Smith" );
		List<DumbUser> users = Arrays.asList( user1, user2 );

		System.out.println( "We are inserting users into the db..." );

		for ( DumbUser user : users ) {

			System.out.println( "INSERTING: " + user );

			create.insertInto(USERS)
					.columns(USERS.FIRST_NAME, USERS.LAST_NAME)
					.values(user.getFirstName(), user.getLastName())
					.execute(); // run the insert stmt, user is now in the db!
		}

		// we assume we are at some point in the program were we want to
		// fetch a list of users

		System.out.println( "We are fetching users from the db..." );

		Result<Record> records = create.select()
				.from(USERS)
				.where(USERS.FIRST_NAME.contains("Dumb"))
				.fetch();

		for ( Record record : records ) {

			int id = (Integer) record.getValue( USERS.ID );
			String fname = (String) record.getValue( USERS.FIRST_NAME );
			String lname = (String) record.getValue( USERS.LAST_NAME );

			String userStr = String.format(
					"User id=%d fname=%s lname=%s",
					id, fname, lname
			);

			System.out.println( userStr  );
		}
		System.out.println();
	}

	private static void approach2( DSLContext create ) {

		System.out.println( "=== This is method 2 of jooq ===" );

		// we assume we are at some point in the program were we have a list of users.
		// for this example lets create that list.
		UsersRecord user1 = new UsersRecord( null, "Smart", "Clark", "sclark", "pazzword", 0 );
		UsersRecord user2 = new UsersRecord( null, "SmartyPants", "Debra", "spants-debra", "pazzword", 0 );
		List<UsersRecord> users = Arrays.asList( user1, user2 );

		for ( UsersRecord user : users ) {

			System.out.println( "INSERTING: " + user.getUsername() );

			create.insertInto( USERS )
					.set( user )
					.execute();
		}

		// we assume we are at some point in the program were we want to
		// fetch a list of users

		System.out.println( "We are fetching users from the db..." );

		Result<Record> records = create.select()
				.from(USERS)
				.where(USERS.FIRST_NAME.contains("Smart"))
				.fetch( );

		UsersRecord user = null;
		for ( Record record : records ) {

			user = record.into( USERS );

			String userStr = String.format(
					"User id=%d fname=%s lname=%s",
					user.getId(), user.getFirstName(), user.getLastName()
			);

			System.out.println( userStr  );
		}
		System.out.println();
	}

	private static void approach3( DSLContext create ) {

		System.out.println( "=== This is method 3 of jooq ===" );

		// We need the data access object (DAO) to CRUD to the Users table
		UsersDao dao = new UsersDao( create.configuration() );

		// we assume we are at some point in the program were we have a list of users.
		// for this example lets create that list.
		Users user1 = new Users( null, "Dao", "John", "djhon", "pazzword", 0 );
		Users user2 = new Users( null, "DaoDao", "Smith", "dsmith", "pazzword", 0 );
		List<Users> users = Arrays.asList( user1, user2 );

		System.out.println( "We are inserting users into the db..." );

		System.out.println( "INSERTING: " + user1.getUsername() + " AND " + user2.getUsername() );
		// look at that! we can do without the for loop here!
		dao.insert( users );

		//for ( Users user : users ) {
		//	System.out.println( "INSERTING: " + user.getUsername() );
		//	dao.insert( user );
		//}

		// we assume we are at some point in the program were we want to
		// fetch a list of users

		System.out.println( "We are fetching users from the db..." );

		// I dont know if we can filter using DAOs with wild cards
		// but thats not problem, because usually we show everything to the user
		//
		// List<Users> fetchedUsers = dao.findAll()
		//
		// And later the program comes back with some selected info
		//
		// dao.deleteById( id );
		//
		// OR
		//
		// dao.update( user );
		List<Users> fetchedUsers =  dao.fetchByUsername( user1.getUsername(), user2.getUsername() );

		for ( Users user : fetchedUsers ) {

			String userStr = String.format(
					"User id=%d fname=%s lname=%s",
					user.getId(), user.getFirstName(), user.getLastName()
			);

			System.out.println( userStr  );
		}

		// What?! we need to update the users because there names should
		// actually be "Awesome Dao" instead of just "Dao" ? no problem!

		for ( Users user : fetchedUsers ) {
			user.setFirstName( "Awesome " + user.getFirstName() );
		}

		// YES! you can just pass the same objects you changed to update them in the db!
		dao.update( fetchedUsers );

		System.out.println( "We are fetching users from the db again..." );

		List<Users> fetchedUsers2 =  dao.fetchByUsername( user1.getUsername(), user2.getUsername() );

		for ( Users user : fetchedUsers2 ) {

			String userStr = String.format(
					"User id=%d fname=%s lname=%s",
					user.getId(), user.getFirstName(), user.getLastName()
			);

			System.out.println( userStr  );
		}

		System.out.println();
	}


	// This is an example User class that we would probably create manually.
	private static class DumbUser {

		private Integer id;
		private String firstName;
		private String lastName;

		public DumbUser( Integer id, String firstName, String lastName) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		@Override
		public String toString() {
			return "DumbUser{" +
					"id=" + id +
					", firstName='" + firstName + '\'' +
					", lastName='" + lastName + '\'' +
					'}';
		}
	}
}
