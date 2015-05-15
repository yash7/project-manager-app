package ateamcomp354.projectmanagerapp.dataAccess;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

/**
 * Takes care of all database access
 * 
 * @author Anthony-Virgil Bermejo (26982166)
 */
public class DatabaseManager {
	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;
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
		preparedStatement = null;
		createTables();
	}
	
	public Connection getOpenConnection() throws Exception {	
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection("jdbc:sqlite:" + dbName);
	}
	
	// opens connection with sqlite
	private void openConnection() {
		try {
			connection = getOpenConnection();
			statement = connection.createStatement();
			statement.execute("PRAGMA foreign_keys = ON");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// closes connection
	private void closeConnection() {
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
			
			// checking if tables are created
			DatabaseMetaData data = connection.getMetaData();
			statement = connection.createStatement();

			// create users table
			resultSet = data.getTables(null, null, "users", null);
			if (!resultSet.next()) {
				statement
						.execute("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, "
								+ "first_name TEXT, "
								+ "last_name TEXT, "
								+ "username TEXT UNIQUE);");
			}


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
