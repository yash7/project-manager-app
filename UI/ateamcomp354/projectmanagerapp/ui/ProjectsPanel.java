package ateamcomp354.projectmanagerapp.ui;

import ateamcomp354.projectmanagerapp.model.Pojos;
import ateamcomp354.projectmanagerapp.model.ProjectInfo;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.ui.gen.GanttChartGen;
import ateamcomp354.projectmanagerapp.services.UserService;
import ateamcomp354.projectmanagerapp.ui.gen.SplitPane1Gen;
import ateamcomp354.projectmanagerapp.ui.gen.US1RightPanelGen;
import ateamcomp354.projectmanagerapp.ui.util.Dialogs;
import ateamcomp354.projectmanagerapp.ui.util.FrameSaver;
import ateamcomp354.projectmanagerapp.ui.util.TwoColumnListCellRenderer;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The projects panel displays a list of all the projects in the system.
 * Users can create new projects, edit, or delete projects.
 * Users can select to view a project's activities which sends them to the activities panel.
 */
public class ProjectsPanel {

	private static final String VIEW_BTN_TXT = "View Activities";
	private static final String EDIT_PROJECT_TITLE_LBL_TXT = "Edit Project";
	private static final String ADD_PROJECT_TITLE_LBL_TXT = "New Project";
	private static final String NEW_PROJECT_NAME_DEFAULT_VALUE = "My new project";
	private static final String NEW_PROJECT_DESC_DEFAULT_VALUE = "";
	private static final String PROJECT_COMPLETION_FMT = "%s%%";

	private final ProjectService projectService;
	private final UserService userService;
	private final SwapInterface swap;

	private final SplitPane1Gen splitPane1Gen;
	private final US1RightPanelGen us1RightPanelGen;

	private final JList<Project> openProjectList;
	private final DefaultListModel<Project> openProjectsModel;
	private final JList<Project> closedProjectList;
	private final DefaultListModel<Project> closedProjectsModel;
	
	private ApplicationContext appCtx;
	private ActivityService ase;

	private int selectedProjectMemberId;
	private List<Integer> projectMemberComboIndexes;
	private int[] projectMemberIndexes;
	
	/**
	 * A cache of project info, mapping a project id to its cached info.
	 * The cache is built every time the list of projects is obtained from projectService.
	 */
	private final Map<Integer,ProjectInfo> projectInfos;

	/**
	 * The object used to fill in the UI with default values when creating a new project.
	 */
	private final Project newProjectTemplate;
	/**
	 * The last user selected project, if empty then a project is being created.
	 */

	private Optional<Project> selectedProject;

	/**
	 * When changing a lists selected item in code a list selection event gets fired.
	 * We use this flag to indicate that the event is caused by code and not by a user UI interaction.
	 */
	private boolean valueIsAdjusting;

	/**
	 * We use this flag when first setting up the view before it is displayed.
	 * Specifically used for dirty checking so that a dialog does not appear
	 * the first time this ProjectsPanel is displayed.
	 */
	private boolean refreshing;

	/**
	 * Indicate a save is in progress so that extra dirty checking is avoided.
	 */
	private boolean saving;

	public ProjectsPanel( ApplicationContext appCtx, SwapInterface swap )
	{
		this.appCtx = appCtx;
		projectService = appCtx.getProjectService();
		userService = appCtx.getUserService();
		this.swap = swap;

		projectInfos = new HashMap<>();

		splitPane1Gen = new SplitPane1Gen();
		splitPane1Gen.getBtnChart().setBounds(44, 345, 116, 30);

		us1RightPanelGen = new US1RightPanelGen();

		us1RightPanelGen.getBudgetAtCompletionLabel().setSize(200, 16);
		us1RightPanelGen.getBudgetAtCompletionLabel().setLocation(141, 103);

		splitPane1Gen.getSplitPane().setRightComponent( us1RightPanelGen );

		openProjectsModel = new DefaultListModel<>();
		closedProjectsModel = new DefaultListModel<>();

		//splitPane1Gen.getLogoutButton().addActionListener( __ -> this.swap.showLoginView() );

		splitPane1Gen.getBackBtn().setVisible( false );

		splitPane1Gen.getTopLabel().setVisible( false );

		splitPane1Gen.getAddButton().addActionListener( __ -> addProjectClicked() );
		splitPane1Gen.getDeleteButton().addActionListener( __ -> deleteProjectClicked() );
		splitPane1Gen.getChartButton().addActionListener(__-> viewProgressClicked());
		splitPane1Gen.getBtnCriticalPath().addActionListener(__ -> viewCriticalPath());
		
		TwoColumnListCellRenderer<Project> renderer = new TwoColumnListCellRenderer<>(
				Project::getProjectName,
				p -> {
					ProjectInfo pi = projectInfos.get( p.getId() );
					return String.format(PROJECT_COMPLETION_FMT, pi == null ? "0" : Integer.toString(pi.getCompletion()));
				}
		);

		openProjectList = new JList<>(openProjectsModel);
		openProjectList.setCellRenderer( renderer );
		openProjectList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		openProjectList.getSelectionModel().addListSelectionListener( this::openProjectSelected );
		splitPane1Gen.getListScrollPane().setViewportView(openProjectList);

		closedProjectList = new JList<>(closedProjectsModel);
		closedProjectList.setCellRenderer( renderer );
		closedProjectList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		closedProjectList.getSelectionModel().addListSelectionListener( this::closedProjectSelected );
		splitPane1Gen.getCompletedScrollPane().setViewportView( closedProjectList );
		splitPane1Gen.getBtnView().setText(VIEW_BTN_TXT);
		splitPane1Gen.getBtnView().addActionListener( __ -> viewActivitiesClicked() );

		
		
		us1RightPanelGen.getSaveButton().addActionListener( __ -> saveProjectClicked() );

		newProjectTemplate = new Project();
		newProjectTemplate.setProjectName( NEW_PROJECT_NAME_DEFAULT_VALUE );
		newProjectTemplate.setDescription( NEW_PROJECT_DESC_DEFAULT_VALUE);
		newProjectTemplate.setCompleted( false );
		
		splitPane1Gen.getListScrollPane().setColumnHeaderView(new JLabel("Open Projects"));
		splitPane1Gen.getCompletedScrollPane().setColumnHeaderView(new JLabel("Completed Projects"));
		
		us1RightPanelGen.getAddButton().addActionListener ( __ -> addProjectMember() );
		us1RightPanelGen.getDeleteButton().addActionListener( __ -> removeProjectMember() );

		selectedProject = Optional.empty();
		displayProject();
	}

	public JComponent getComponent()
	{
		return splitPane1Gen;
	}

	/**
	 * To be called before switching to the projects view.
	 * See also refresh( int projectId ).
	 */
	public void refresh() {
		refreshImpl( null );
	}

	/**
	 * Before switching to the project view inform ProjectsPanel
	 * what projectId to preselect.
	 * See also refresh().
	 */
	public void refresh( int projectId ) {
		refreshImpl( projectId );
	}

	private void refreshImpl( Integer projectId ) {

		refreshing = true;

		fillProjectLists();

		boolean select = projectId != null;

		if ( select ) {
			select = !selectProject( projectId );
		}

		if ( (projectId == null | select) && !openProjectsModel.isEmpty() ) {
			openProjectList.setSelectedIndex( 0 );
		}

		refreshing = false;
	}

	private boolean selectProject( int projectId ) {

		boolean r = selectProject( openProjectList, projectId );

		if ( !r) {
			r = !selectProject( closedProjectList, projectId );
		}

		return r;
	}

	// Try to select the project for projectId in the list
	private boolean selectProject( JList<Project> list, int projectId ) {

		ListModel<Project> model = list.getModel();

		int len = model.getSize();
		for ( int i = 0; i < len; i++ ) {
			if ( projectId == model.getElementAt(i).getId() ) {
				list.setSelectedIndex( i );
				return true;
			}
		}
		return false;
	}

	// Returns the current selected project. If getProject().getId() is null then
	// the current selected project is a new project being created.
	private Project getProject() {
		return selectedProject.orElse( newProjectTemplate );
	}

	// Get all the projects from ProjectService and display them.
	// Open projects in the open list, closed projects in the closed list,
	// and cache every project's ProjectInfo object.
	private void fillProjectLists() {

		List<Project> projects = projectService.getProjects();

		openProjectsModel.clear();
		closedProjectsModel.clear();
		projectInfos.clear();

		for ( Project p : projects ) {

			if ( p.getCompleted() ) {
				closedProjectsModel.addElement( p );
			}
			else {
				openProjectsModel.addElement( p );
			}

			int id = p.getId();

			int completion = projectService.getProjectCompletion( id );
			int activitiesCount = projectService.getProjectActivitiesCount( id );

			projectInfos.put(id, new ProjectInfo(completion, activitiesCount));
		}
	}

	private void openProjectSelected( ListSelectionEvent e ) {
		projectSelected( e, openProjectList, closedProjectList );
	}

	private void closedProjectSelected( ListSelectionEvent e ) {
		projectSelected( e, closedProjectList, openProjectList );
	}

	// A project in either the open or closed list has been selected.
	// Make sure to unselect any previous choice from the other list.
	// Make sure to prompt the user if they made changes before but did
	// not click the save button.
	// If they choose cancel then we need to undo the selection and reselect
	// the old project.
	private void projectSelected( ListSelectionEvent e, JList<Project> list1, JList<Project> list2 ) {
		if ( valueIsAdjusting ) {
			return;
		}

		int i = list1.getSelectedIndex();
		if ( i == -1 ) {
			splitPane1Gen.getDeleteButton().setEnabled( false );
			splitPane1Gen.getBtnView().setEnabled( false );
			
			return;
		}

		valueIsAdjusting = true;
		list2.clearSelection();
		valueIsAdjusting = false;

		Project project = list1.getModel().getElementAt( i );

		int r = checkDirty();
		if ( r == JOptionPane.YES_OPTION ) {
			valueIsAdjusting = true;
			selectProject( project.getId() );
			valueIsAdjusting = false;
		}
		else if ( r == JOptionPane.CANCEL_OPTION ) {
			return;
		}

		ProjectInfo projectInfo = projectInfos.get( project.getId() );
		int activitiesCount = projectInfo == null ? Integer.MAX_VALUE : projectInfo.getActivitesCount();

		selectedProject = Optional.of( project );

		splitPane1Gen.getDeleteButton().setEnabled( project.getCompleted() || activitiesCount == 0 );
		splitPane1Gen.getBtnView().setEnabled( true );

		displayProject();
	
	}

	// If the form input is dirty then prompt the user if they want to save the changes.
	// Take appropriate actions if user says yes, no, or cancel.
	private int checkDirty( ) {

		if ( saving ) {
			return JOptionPane.NO_OPTION;
		}

		if ( isDirty() ) {

			int r = Dialogs.dirty(getComponent(), us1RightPanelGen.getProjectNameField().getText());

			if ( r == JOptionPane.YES_OPTION ) {

				valueIsAdjusting = true;
				saveProjectClicked();
				valueIsAdjusting = false;
			} else if ( r == JOptionPane.CANCEL_OPTION ) {

				valueIsAdjusting = true;

				Integer oldId = getProject().getId();
				if ( oldId == null ) {
					openProjectList.clearSelection();
					closedProjectList.clearSelection();
				} else {
					selectProject( oldId );
				}

				valueIsAdjusting = false;
			}

			return r;
		}

		return JOptionPane.NO_OPTION;
	}

	// When the user wants to view the activities of a selected project.
	private void viewActivitiesClicked() {

		int r = checkDirty();
		if ( r == JOptionPane.CANCEL_OPTION ) { return; }

		Optional.ofNullable( getProject().getId() )
				.ifPresent( swap::showActivitiesView );
		
		FrameSaver activityFrame = new FrameSaver();
		activityFrame.setFirstID(getProject().getId()); // Saves the project's ID
		activityFrame.setFrameName("ACTIVITIES_PANEL");
		swap.saveFrame(activityFrame); // Saves the frame next frame
	}
	
	private void viewProgressClicked(){
			List<Activity> acts = appCtx.getActivityService(getProject().getId()).getActivities();
					
			if(acts.size()>0){
			GanttChartGen chart = new GanttChartGen(getProject().getProjectName()
					 + " Progress",acts); 
			JOptionPane.showMessageDialog (null, chart, "Project", JOptionPane.PLAIN_MESSAGE);
			}
			else
				JOptionPane.showMessageDialog (null, "There are no activities for this project"
						, "No Report", JOptionPane.PLAIN_MESSAGE);
	}
	
	private void viewCriticalPath() {
		ase = appCtx.getActivityService(getProject().getId());
		
		List<Activity> acts = appCtx.getActivityService(getProject().getId()).getActivities();
		
		if(acts.size() > 0) {
			List<Integer> x = ase.calculateNumberOfStartingNodes(new ArrayList<Integer>(), acts.get(0).getId());
			if(x.size() == 1) {
				List<Integer> y = ase.calculateNumberOfEndingNodes(new ArrayList<Integer>(), acts.get(0).getId());
				if(y.size() == 1) {
					List<Integer> sizeArray = ase.calculateSizeOfChain(new ArrayList<Integer>(), acts.get(0).getId());
					if(acts.size() == sizeArray.size()) {
						System.out.println("Good to calculate all params");
						ase.calculateAllParamsOfChain(x.get(0), y.get(0));
					}
					else {
						JOptionPane.showMessageDialog(null, "There are loose activities not linked to any others, all activities must be linked in order to create a working Critical Path Analysis");
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "There are multiple ending activities (or dangles), there must be only a single end activity to create a working Critical Path Analysis");
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "There are multiple starting activities, there must be only a single start activity to create a working Critical Path Analysis");
			}
		}
		else {
			JOptionPane.showMessageDialog (null, "There are no activities for this project", "No Report", JOptionPane.PLAIN_MESSAGE);
		}
		
		ase = null;
	}

	// Btn to create new project is clicked, clear list selections and
	// fill the form with the default project template.
	private void addProjectClicked() {

		int r = checkDirty();
		if ( r == JOptionPane.CANCEL_OPTION ) { return; }

		openProjectList.clearSelection();
		closedProjectList.clearSelection();

		selectedProject = Optional.empty();

		displayProject();

		us1RightPanelGen.getProjectNameField().requestFocus();
	}

	// delete the currently selected project
	private void deleteProjectClicked() {

		int i = openProjectList.getSelectedIndex();
		int j = closedProjectList.getSelectedIndex();

		Optional.ofNullable( getProject().getId() )
				.ifPresent( projectService::deleteProject );

		refresh();

		selectNextProject( openProjectList, i);
		selectNextProject( closedProjectList, j );
	}

	// a delete removes the selected project, so to be user friendly select the
	// next project in the list for them.
	private void selectNextProject( JList<Project> list, int oldIndex ) {

		if ( oldIndex == -1 ) { return; }

		int newIndex = Math.max( 0, oldIndex - 1 );

		if ( list.getModel().getSize() > 0 ) {
			list.setSelectedIndex( newIndex );
		}
	}

	// Btn save is clicked, save changes filled in the form.
	private void saveProjectClicked() {

		Project p = buildProject();
		String errorString = "";
		
		if(p.getProjectName().trim().equals("")) {
			errorString += "Project Name may not be empty\n";
		}
		
		
		if(errorString.equals("")) {
			saving = true;

			if ( p.getId() == null ) {
				projectService.addProject( p );
				refresh();
				openProjectList.setSelectedIndex( openProjectsModel.getSize() - 1);
			} else {
				projectService.updateProject( p );
				refresh( p.getId() );
			}

			saving = false;
		}
		else {
			JOptionPane.showMessageDialog(null, errorString);
		}
	}

	// fill in the form with the currently selected project ( or newProjectTemplate if creating )
	private void displayProject() {
		Project p = getProject();
		
		us1RightPanelGen.getTitleLabel().setText( p.getId() == null ? ADD_PROJECT_TITLE_LBL_TXT : EDIT_PROJECT_TITLE_LBL_TXT );
		us1RightPanelGen.getCompletedCheckBox().setVisible(p.getId() != null);
		us1RightPanelGen.getCompletedCheckBox().setSelected(p.getCompleted());
		us1RightPanelGen.getProjectNameField().setEnabled(!p.getCompleted());
		us1RightPanelGen.getProjectNameField().setText(p.getProjectName());
		us1RightPanelGen.getDescriptionArea().setEnabled( !p.getCompleted() );
		us1RightPanelGen.getDescriptionArea().setText( p.getDescription() );
		
		
		if(p.getBudgetAtCompletion() != null) {
			us1RightPanelGen.getBudgetAtCompletionLabel().setText(p.getBudgetAtCompletion().toString());
		}
		else {
			us1RightPanelGen.getBudgetAtCompletionLabel().setText("0");
		}
		
		
		if (p.getId() != null) {
			us1RightPanelGen.getProjectMembersScrollPane().setVisible(true);
			us1RightPanelGen.getProjectMembersComboBox().setVisible(true);
			us1RightPanelGen.getProjectMembersLabel().setVisible(true);
			us1RightPanelGen.getAddButton().setVisible(true);
			us1RightPanelGen.getDeleteButton().setVisible(true);
			
			showProjectMembers(p.getId());
		}
		else {
			us1RightPanelGen.getProjectMembersScrollPane().setVisible(false);
			us1RightPanelGen.getProjectMembersComboBox().setVisible(false);
			us1RightPanelGen.getProjectMembersLabel().setVisible(false);
			us1RightPanelGen.getAddButton().setVisible(false);
			us1RightPanelGen.getDeleteButton().setVisible(false);
			
			us1RightPanelGen.getProjectMembersScrollPane().setViewportView(new JList<String>());
		}
	}

	// gather the form input into a project object
	private Project buildProject() {

		Project p = new Project();
		p.setId( getProject().getId() );
		p.setProjectName( us1RightPanelGen.getProjectNameField().getText() );
		p.setDescription( us1RightPanelGen.getDescriptionArea().getText() );
		p.setCompleted( p.getId() != null && us1RightPanelGen.getCompletedCheckBox().isSelected() );
		p.setBudgetAtCompletion( Integer.valueOf(us1RightPanelGen.getBudgetAtCompletionLabel().getText()));
		return p;
	}
	
	private void showProjectMembers(int projectId) {
		
		List<Users> projectMembers = projectService.getMembersForProject(projectId);
		
		selectedProjectMemberId = 0;
		String projectMemberNames[] = new String[projectMembers.size()];
		projectMemberIndexes = new int[projectMembers.size()];
		
		for (int i = 0; i < projectMembers.size(); i++)
		{
			projectMemberNames[i] = projectMembers.get(i).getFirstName() + " " + projectMembers.get(i).getLastName();
			projectMemberIndexes[i] = projectMembers.get(i).getId();
		}
		
		JList<String> projectMemberList = new JList<String>(projectMemberNames);
		projectMemberList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		us1RightPanelGen.getProjectMembersScrollPane().setViewportView(projectMemberList);
		us1RightPanelGen.getProjectMembersScrollPane().validate();
		
		fillProjectMembersComboBox();
		
		projectMemberList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if (projectMemberList.locationToIndex(e.getPoint()) == -1) return;
				selectedProjectMemberId = projectMemberIndexes[projectMemberList.locationToIndex(e.getPoint())];
			}
		});
	}
	
	private void fillProjectMembersComboBox() {
		us1RightPanelGen.getProjectMembersComboBox().removeAllItems();
		projectMemberComboIndexes = new ArrayList<Integer>();
		projectMemberComboIndexes.clear();
		
		int projectId = getProject().getId();
		
		List<Users> notAssigned = projectService.getUnassignedMembersForProject(projectId);
		
		for (int i = 0; i < notAssigned.size(); i++) {
			us1RightPanelGen.getProjectMembersComboBox().addItem(notAssigned.get(i).getFirstName() + " " + notAssigned.get(i).getLastName());
			projectMemberComboIndexes.add(notAssigned.get(i).getId());
		}
	}
	
	private void addProjectMember()
	{	
		if (us1RightPanelGen.getProjectMembersComboBox().getSelectedIndex() == -1) return;
		
		int projectId = getProject().getId();
		
		int toAdd = projectMemberComboIndexes.get(us1RightPanelGen.getProjectMembersComboBox().getSelectedIndex());
		projectService.addUserToProject(projectId, userService.getUser(toAdd));
		showProjectMembers(projectId);
	}
	
	private void removeProjectMember()
	{
		if (selectedProjectMemberId == 0) return;
		
		int projectId = getProject().getId();
		
		projectService.deleteUserFromProject(projectId, userService.getUser(selectedProjectMemberId));
		selectedProjectMemberId = 0;
		showProjectMembers(projectId);
	}

	// the form is dirty if the selected project does not equal the form inputs values.
	// Also a new project is consider dirty. This makes it so that clicking add Project btn
	// and then selected a existing project will prompt the user before the existing project is displayed.
	// Finally, the refresh flag is used so that a dialog is not displayed when the ProjectPanel is first
	// displayed and the user never interacted with the view.
	private boolean isDirty() {
		Project p = getProject();
		return !refreshing && ( p.getId() == null || !Pojos.projectsEqual( p, buildProject() ) );
	}
}
