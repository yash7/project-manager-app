package ateamcomp354.projectmanagerapp;

import java.awt.EventQueue;
import java.sql.Connection;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.impl.ApplicationContextImpl;
import ateamcomp354.projectmanagerapp.dataAccess.DatabaseManager;
import ateamcomp354.projectmanagerapp.ui.MainFrame;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class App {

	public static void main(String[] args) throws Exception {

		DatabaseManager db = new DatabaseManager();
		Connection c = db.getOpenConnection();

		ApplicationContext appCtx = getApplicationContext( c );

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
}
