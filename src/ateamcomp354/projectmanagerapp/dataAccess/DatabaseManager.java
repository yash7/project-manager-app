package ateamcomp354.projectmanagerapp.dataAccess;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Takes care of all database access
 * 
 * @author Anthony-Virgil Bermejo (26982166)
 */
public class DatabaseManager {
	private Connection connection;
	private ResultSet resultSet;
	private Statement statement;
	private String dbName;
	
	public DatabaseManager() {
		this("database.db");
	}

	public DatabaseManager(String dbName) {
		this.dbName = dbName;
		connection = null;
		resultSet = null;
		statement = null;
		openConnection();
		createTables();
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	// opens connection with sqlite
	private void openConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			connection.createStatement().execute("PRAGMA foreign_keys = ON");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// closes connection
	public void closeConnection() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// creates all necessary tables for the database
	private void createTables() {
		try {
			openConnection();
			DatabaseMetaData data = connection.getMetaData();
			statement = connection.createStatement();

			// create users table if doesn't exist already
			resultSet = data.getTables(null, null, "users", null);
			if (!resultSet.next()) {
				String sql = readFile( "ddl/users.sql" );
				statement.execute(sql);
				sql = readFile( "ddl/default-user.sql" );
				statement.execute( sql );
			}
			
			// create project table if doesn't exist already
			resultSet = data.getTables(null, null, "project", null);
			if (!resultSet.next()) {
				String sql = readFile( "ddl/project.sql" );
				statement.execute(sql);
			}
			
			// create activity table if doesn't exist already
			resultSet = data.getTables(null, null, "activity", null);
			if (!resultSet.next()) {
				String sql = readFile( "ddl/activity.sql" );
				statement.execute(sql);
			}
			
			// create user activities table if doesn't exist already
			resultSet = data.getTables(null, null, "userActivities", null);
			if (!resultSet.next()) {
				String sql = readFile( "ddl/userActivities.sql" );
				statement.execute(sql);
			}

			// create activity links table if doesn't exist already
			resultSet = data.getTables(null, null, "activityLinks", null);
			if (!resultSet.next()) {
				String sql = readFile( "ddl/activityLinks.sql" );
				statement.execute(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// reads from a text file into a String
	private String readFile( String name ) {

		StringJoiner str = new StringJoiner("\n");

		Scanner in = new Scanner( getClass().getResourceAsStream(name) );
		while( in.hasNextLine() ) {
			str.add(in.nextLine());
		}

		return str.toString();
	}
}
