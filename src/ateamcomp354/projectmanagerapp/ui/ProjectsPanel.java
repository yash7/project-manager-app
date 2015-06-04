package ateamcomp354.projectmanagerapp.ui;

import ateamcomp354.projectmanagerapp.model.Pojos;
import ateamcomp354.projectmanagerapp.model.ProjectInfo;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.ui.gen.SplitPane1Gen;
import ateamcomp354.projectmanagerapp.ui.gen.US1RightPanelGen;
import ateamcomp354.projectmanagerapp.ui.util.Dialogs;
import ateamcomp354.projectmanagerapp.ui.util.TwoColumnListCellRenderer;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
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
	private final SwapInterface swap;

	private final SplitPane1Gen splitPane1Gen;
	private final US1RightPanelGen us1RightPanelGen;

	private final JList<Project> openProjectList;
	private final DefaultListModel<Project> openProjectsModel;
	private final JList<Project> closedProjectList;
	private final DefaultListModel<Project> closedProjectsModel;

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
		projectService = appCtx.getProjectService();
		this.swap = swap;

		projectInfos = new HashMap<>();

		splitPane1Gen = new SplitPane1Gen();

		us1RightPanelGen = new US1RightPanelGen();
		splitPane1Gen.getSplitPane().setRightComponent( us1RightPanelGen );
		
		splitPane1Gen.getLogoutButton().addActionListener(new LogoutListener(swap));

		openProjectsModel = new DefaultListModel<>();
		closedProjectsModel = new DefaultListModel<>();

		splitPane1Gen.getLogoutButton().addActionListener( __ -> this.swap.showLoginView() );

		splitPane1Gen.getTopLabel().setVisible( false );

		splitPane1Gen.getAddButton().addActionListener( __ -> addProjectClicked() );
		splitPane1Gen.getDeleteButton().addActionListener( __ -> deleteProjectClicked() );

		TwoColumnListCellRenderer<Project> renderer = new TwoColumnListCellRenderer<>(
				Project::getProjectName,
				p -> {
					ProjectInfo pi = projectInfos.get( p.getId() );
					return String.format(PROJECT_COMPLETION_FMT, pi == null ? "?" : Integer.toString(pi.getCompletion()));
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

		splitPane1Gen.getBtnManage().setVisible( false );
		splitPane1Gen.getBtnView().setText(VIEW_BTN_TXT);
		splitPane1Gen.getBtnView().addActionListener( __ -> viewActivitiesClicked() );

		us1RightPanelGen.getSaveButton().addActionListener( __ -> saveProjectClicked() );

		newProjectTemplate = new Project();
		newProjectTemplate.setProjectName( NEW_PROJECT_NAME_DEFAULT_VALUE );
		newProjectTemplate.setDescription( NEW_PROJECT_DESC_DEFAULT_VALUE);
		newProjectTemplate.setCompleted( false );

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
		refresh( null );
	}

	/**
	 * Before switching to the project view inform ProjectsPanel
	 * what projectId to preselect.
	 * See also refresh().
	 */
	public void refresh( int projectId ) {
		refresh( projectId );
	}

	private void refresh( Integer projectId ) {

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
	}

	// gather the form input into a project object
	private Project buildProject() {

		Project p = new Project();
		p.setId( getProject().getId() );
		p.setProjectName( us1RightPanelGen.getProjectNameField().getText() );
		p.setDescription( us1RightPanelGen.getDescriptionArea().getText() );
		p.setCompleted( p.getId() != null && us1RightPanelGen.getCompletedCheckBox().isSelected() );
		return p;
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
