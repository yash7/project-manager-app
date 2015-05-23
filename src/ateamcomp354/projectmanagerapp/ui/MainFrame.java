package ateamcomp354.projectmanagerapp.ui;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

	private static final String VIEW_1 = "VIEW_1";
	private static final String VIEW_2 = "VIEW_2";

	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;

	private final ApplicationContext appCtx;

	private CardLayout cardLayout;

	private HelloPanel helloPanel1;
	private HelloPanel helloPanel2;
	private LoginPanel loginPanel;
	private SplitPane1 splitPane1;
	
	public MainFrame( ApplicationContext appCtx ) {

		this.appCtx = appCtx;

		init();

		cardLayout = new CardLayout();
		setLayout( cardLayout );

		helloPanel1 = new HelloPanel( "Hello View 1");
		helloPanel2 = new HelloPanel( "Hello View 2");
		loginPanel = new LoginPanel();
		splitPane1 = new SplitPane1();
		

		buildMenuBar();

		// add(helloPanel1.getComponent(), VIEW_1);
		add(loginPanel.getComponent(), VIEW_1);
		// add(helloPanel2.getComponent(), VIEW_2);
		add(splitPane1.getComponent(), VIEW_2);
	}

	private void init() {

		setSize( WIDTH, HEIGHT );
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible( true );
	}

	private void buildMenuBar() {

		JMenuItem view1Itm = new JMenuItem( "View 1" );
		view1Itm.addActionListener( __ -> showView( VIEW_1 ) );

		JMenuItem view2Itm = new JMenuItem( "View 2" );
		view2Itm.addActionListener( __ -> showView( VIEW_2 ) );

		JMenu viewMenu = new JMenu( "View" );
		viewMenu.add( view1Itm );
		viewMenu.add( view2Itm );

		JMenuBar menuBar = new JMenuBar();
		menuBar.add( viewMenu );

		setJMenuBar( menuBar );
	}

	private void showView( String name ) {
		cardLayout.show( getContentPane(), name);
	}
}
