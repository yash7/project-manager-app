package ateamcomp354.projectmanagerapp.ui;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.CreateUserService;
import ateamcomp354.projectmanagerapp.services.UserService;
import ateamcomp354.projectmanagerapp.ui.util.FrameSaver;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import javax.swing.*;

import java.awt.*;
import java.util.Stack;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame implements SwapInterface{

	private static final String LOGIN_PANEL = "LOGIN_PANEL";
	private static final String PROJECTS_PANEL = "PROJECTS_PANEL";
	private static final String ACTIVITIES_PANEL = "ACTIVITIES_PANEL";
	private static final String MEMBERPROJECT_PANEL = "MEMBERPROJECT_PANEL";
	private static final String MEMBERACTIVITY_PANEL = "MEMBERACTIVITY_PANEL";
	private static final String CREATE_USER_PANEL = "CREATE_USER_PANEL";
	private static final String EDIT_USER_PANEL = "EDIT_USER_PANEL";
	private static final String EDIT_USER_LIST_PANEL = "EDIT_USER_LIST_PANEL";

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 800;


	private CardLayout cardLayout;
	
	private LoginPanel loginPanel;
	private ProjectsPanel projectsPanel;
	private ActivitiesPanel activitiesPanel;
	private MemberProjectPanel memberProjectPanel;
	private MemberActivityPanel memberActivityPanel;
	private CreateUserPanel createUserPanel;
	private EditUserPanel editUserPanel;
	private EditUserListPanel editUserListPanel;
	
	private static Stack<FrameSaver> menuFrameSaver= new Stack<FrameSaver>();;
	
	public MainFrame( ApplicationContext appCtx ) {
		init();

		cardLayout = new CardLayout();
		getContentPane().setLayout( cardLayout );

		setResizable(false);

		this.setTitle("A Team - Project Management System");
		
		loginPanel = new LoginPanel( appCtx , MainFrame.this);
		projectsPanel = new ProjectsPanel( appCtx , MainFrame.this );
		activitiesPanel = new ActivitiesPanel( appCtx , MainFrame.this );
		memberProjectPanel = new MemberProjectPanel(appCtx, MainFrame.this );
		memberActivityPanel = new MemberActivityPanel (appCtx, MainFrame.this);
		createUserPanel = new CreateUserPanel(appCtx, MainFrame.this);
		editUserPanel = new EditUserPanel(appCtx, MainFrame.this);
		editUserListPanel = new EditUserListPanel(appCtx, MainFrame.this);
		
		//USE THIS TO ENABLE QUICK AND EASY ACCESS TO VIEWS
		//buildMenuBar();
		managerMenuBar();

		getContentPane().add(loginPanel.getComponent(), LOGIN_PANEL);
		getContentPane().add(projectsPanel.getComponent(), PROJECTS_PANEL);
		getContentPane().add(activitiesPanel.getComponent(), ACTIVITIES_PANEL);
		getContentPane().add(memberProjectPanel.getComponent(), MEMBERPROJECT_PANEL);
		getContentPane().add(memberActivityPanel.getComponent(), MEMBERACTIVITY_PANEL);
		getContentPane().add(createUserPanel.getComponent(), CREATE_USER_PANEL);
		getContentPane().add(editUserPanel.getComponent(), EDIT_USER_PANEL);
		getContentPane().add(editUserListPanel.getComponent(), EDIT_USER_LIST_PANEL);
	}

	private void init() {

		setSize( WIDTH, HEIGHT );
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * @deprecated - This is only for developing, should not be in release build.
	 */
	private void buildMenuBar() {

		JMenuItem loginItm = new JMenuItem( "Login Panel" );
		loginItm.addActionListener(__ -> showLoginView());

		JMenuItem projectsItm = new JMenuItem( "Projects Panel" );
		projectsItm.addActionListener(__ -> showProjectsView());

		JMenuItem activitiesItm = new JMenuItem( "Activities Panel" );
		activitiesItm.addActionListener( __ -> showActivitiesView( 0 ) );

		JMenuItem createUserItm = new JMenuItem( "Create User Panel" );
		createUserItm.addActionListener( __ -> showCreateUserView(  ) );

		JMenu viewMenu = new JMenu( "View" );
		viewMenu.add( loginItm );
		viewMenu.add( projectsItm );
		viewMenu.add( activitiesItm );
		viewMenu.add( createUserItm );

		JMenuBar menuBar = new JMenuBar();
		menuBar.add( viewMenu );
	
		setJMenuBar(menuBar);
	}
	
	@Override
	public void frameSwitch()
	{
		FrameSaver previousFrame = getSaveFrame();
		String previousFrameName = previousFrame.getFrameName();

		switch(previousFrameName)
		{
		case LOGIN_PANEL:
			showLoginView();
			break;
		case PROJECTS_PANEL:
			showProjectsView();
			break;
		case ACTIVITIES_PANEL:		
			showActivitiesView( previousFrame.getFirstID());
			break;
		case MEMBERPROJECT_PANEL:
			showMemberProjectsView(previousFrame.getFirstID());
			break;
		case MEMBERACTIVITY_PANEL:
			showMemberActivitiesView(previousFrame.getFirstID(), previousFrame.getSecondID());
			break;
		case CREATE_USER_PANEL:
			showCreateUserView();
			break;
		case EDIT_USER_PANEL:
			showEditUserView(previousFrame.getFirstID());
			break;
		case EDIT_USER_LIST_PANEL:
			showEditUserListView();
			break;
		}	
	}
	
	// Menu bar designed for managers to be able to access contents
	// that are made available only for them
	public void managerMenuBar()
	{
		JMenuItem createUserItm = new JMenuItem( "Create A New User" );
		createUserItm.setFont(new Font("Tahoma", Font.PLAIN, 16));
		createUserItm.addActionListener( __->showCreateUserView());
		
		JMenuItem editUserItm = new JMenuItem( "Edit A User" );
		editUserItm.setFont(new Font("Tahoma", Font.PLAIN, 16));
		editUserItm.addActionListener( __->showEditUserListView());
		
		JMenuItem logoutItm = new JMenuItem( "Logout" );
		logoutItm.setFont(new Font("Tahoma", Font.PLAIN, 16));
		logoutItm.addActionListener( __ -> showLoginView() );

		JMenu viewMenu = new JMenu( "File" );
		viewMenu.setFont(new Font("Tahoma", Font.PLAIN, 16));
		viewMenu.add(createUserItm );
		viewMenu.add(editUserItm);
		
		JSeparator separator = new JSeparator();
		viewMenu.add(separator);
		viewMenu.add(logoutItm);
		
		
		
		//viewMenu.setBackground(new Color(135, 206, 250));
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Tahoma", Font.PLAIN, 22));
		menuBar.add( viewMenu );
		//menuBar.setBackground(new Color(135, 206, 250));
		setJMenuBar(menuBar);
		
		setMenuBarNotVisible();

	}
	
	class viewActionListener implements ActionListener{

		public void actionPerformed(ActionEvent e){

			showCreateUserView();
		}
	}

	
	@Override
	public void saveFrame (FrameSaver savedFrame )
	{
		menuFrameSaver.push(savedFrame);
	}
	
	@Override
	public FrameSaver removeSaveFrame()
	{
		return menuFrameSaver.pop();
	}
	
	@Override
	public FrameSaver getSaveFrame()
	{
		return menuFrameSaver.peek();
	}
	

	public void showView( String name ) {
		cardLayout.show( getContentPane(), name);
	}

	@Override
	public void showLoginView() {
		loginPanel.logout();
		cardLayout.show( getContentPane(), LOGIN_PANEL);
		setMenuBarNotVisible();
		menuFrameSaver.clear();
	}

	@Override
	public void showProjectsView() {
		projectsPanel.refresh();
		showView( PROJECTS_PANEL );
		setMenuBarVisible();
	}

	@Override
	public void showProjectsView( int preferredProjectId ) {
		projectsPanel.refresh( preferredProjectId );
		cardLayout.show( getContentPane(),PROJECTS_PANEL);
		setMenuBarVisible();
		
	}

	@Override
	public void showActivitiesView( int projectId ) {
		activitiesPanel.setProjectId( projectId );
		showView(ACTIVITIES_PANEL);
	}

	@Override
	public void showMemberActivitiesView(int projectId, int userId) {
		memberActivityPanel.setUserId(userId);
		memberActivityPanel.setProjectId(projectId);
		showView(MEMBERACTIVITY_PANEL);
	}

	@Override
	public void showMemberProjectsView(int userId) {
		memberProjectPanel.setUserId(userId);
		showView( MEMBERPROJECT_PANEL );
		setMenuBarNotVisible();
	}

	@Override
	public void showCreateUserView()
	{
		createUserPanel.resetComponents();
		showView(CREATE_USER_PANEL);
	}
	
	@Override
	public void showEditUserView(int userId) {
		editUserPanel.resetComponents();
		editUserPanel.setUserId(userId);
		showView(EDIT_USER_PANEL);
	}
	
	@Override
	public void showEditUserListView() {
		showView(EDIT_USER_LIST_PANEL);
	}

	public static int getAppWidth() {
		return WIDTH;
	}

	public static int getAppHeight() {
		return HEIGHT;
	}

	public Users getLoggedInUser() {
		return loginPanel.getLoggedInUser();
	}

	@Override
	public void setMenuBarVisible()
	{
		getJMenuBar().setVisible(true);	
	}
	
	@Override
	public void setMenuBarNotVisible()
	{
		getJMenuBar().setVisible(false);
	}


}
