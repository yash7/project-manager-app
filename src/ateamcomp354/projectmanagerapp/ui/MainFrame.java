package ateamcomp354.projectmanagerapp.ui;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

	private static final String LOGIN_PANEL = "LOGIN_PANEL";
	private static final String SPLIT_PANE_1 = "SPLIT_PANE_1";

	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;

	private final ApplicationContext appCtx;

	private CardLayout cardLayout;

	private LoginPanel loginPanel;
	private SplitPane1 splitPane1;
	
	public MainFrame( ApplicationContext appCtx ) {

		this.appCtx = appCtx;

		init();

		cardLayout = new CardLayout();
		setLayout( cardLayout );

		loginPanel = new LoginPanel();
		splitPane1 = new SplitPane1();
		
		buildMenuBar();

		add(loginPanel.getComponent(), LOGIN_PANEL);
		add(splitPane1.getComponent(), SPLIT_PANE_1);
	}

	private void init() {

		setSize( WIDTH, HEIGHT );
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible( true );
	}

	private void buildMenuBar() {

		JMenuItem view1Itm = new JMenuItem( "Login Panel" );
		view1Itm.addActionListener( __ -> showView( LOGIN_PANEL ) );

		JMenuItem view2Itm = new JMenuItem( "Split Pane 1" );
		view2Itm.addActionListener( __ -> showView( SPLIT_PANE_1 ) );

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
	
	public static int getAppWidth()
	{
		return WIDTH;
	}
	
	public static int getAppHeight()
	{
		return HEIGHT;
	}
}
