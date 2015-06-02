package ateamcomp354.projectmanagerapp.ui;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements SwapInterface{

	private static final String LOGIN_PANEL = "LOGIN_PANEL";
	private static final String PROJECTS_PANEL = "PROJECTS_PANEL";
	private static final String ACTIVITIES_PANEL = "ACTIVITIES_PANEL";

	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;

	private final ApplicationContext appCtx;

	private CardLayout cardLayout;

	private LoginPanel loginPanel;
	private ProjectsPanel projectsPanel;
	private ActivitiesPanel activitiesPanel;
	
	public MainFrame( ApplicationContext appCtx ) {

		this.appCtx = appCtx;

		init();

		cardLayout = new CardLayout();
		setLayout( cardLayout );

		loginPanel = new LoginPanel( appCtx , MainFrame.this );
		projectsPanel = new ProjectsPanel( appCtx , MainFrame.this );
		activitiesPanel = new ActivitiesPanel( appCtx );
		
		buildMenuBar();

		add(loginPanel.getComponent(), LOGIN_PANEL);
		add(projectsPanel.getComponent(), PROJECTS_PANEL);
		add(activitiesPanel.getComponent(), ACTIVITIES_PANEL);
	}

	private void init() {

		setSize( WIDTH, HEIGHT );
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible( true );
	}

	private void buildMenuBar() {

		JMenuItem loginItm = new JMenuItem( "Login Panel" );
		loginItm.addActionListener(__ -> showView(LOGIN_PANEL));

		JMenuItem projectsItm = new JMenuItem( "Projects Panel" );
		projectsItm.addActionListener(__ -> showView(PROJECTS_PANEL));

		JMenuItem activitiesItm = new JMenuItem( "Activities Panel" );
		activitiesItm.addActionListener( __ -> showView( ACTIVITIES_PANEL ) );

		JMenu viewMenu = new JMenu( "View" );
		viewMenu.add( loginItm );
		viewMenu.add( projectsItm );
		viewMenu.add( activitiesItm );

		JMenuBar menuBar = new JMenuBar();
		menuBar.add( viewMenu );

		setJMenuBar( menuBar );
	}

	@Override
	public void showView( String name ) {
		cardLayout.show( getContentPane(), name);
		
	}
	
	public static int getAppWidth(){
		return WIDTH;
	}
	
	public static int getAppHeight(){
		return HEIGHT;
	}
}
