package ateamcomp354.projectmanagerapp.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import ateamcomp354.projectmanagerapp.model.Pojos;
import ateamcomp354.projectmanagerapp.model.ProjectInfo;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.ui.gen.SplitPane1Gen;
import ateamcomp354.projectmanagerapp.ui.gen.US1RightPanelGen;
import ateamcomp354.projectmanagerapp.ui.util.Dialogs;
import ateamcomp354.projectmanagerapp.ui.util.TwoColumnListCellRenderer;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

	private final Map<Integer,ProjectInfo> projectInfos;

	private final Project newProjectTemplate;
	private Optional<Project> selectedProject;

	private boolean valueIsAdjusting;
	private boolean refreshing;
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

	private Project getProject() {
		return selectedProject.orElse( newProjectTemplate );
	}

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

	private int checkDirty( ) {

		if ( saving ) {
			return JOptionPane.NO_OPTION;
		}

		if ( isDirty() ) {

			int r = Dialogs.dirty( getComponent(), us1RightPanelGen.getProjectNameField().getText() );

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

	private void viewActivitiesClicked() {

		Optional.ofNullable( getProject().getId() )
				.ifPresent( swap::showActivitiesView );
	}

	private void addProjectClicked() {

		int r = checkDirty();
		if ( r == JOptionPane.CANCEL_OPTION ) { return; }

		openProjectList.clearSelection();
		closedProjectList.clearSelection();

		selectedProject = Optional.empty();

		displayProject();

		us1RightPanelGen.getProjectNameField().requestFocus();
	}

	private void deleteProjectClicked() {

		int i = openProjectList.getSelectedIndex();
		int j = closedProjectList.getSelectedIndex();

		Optional.ofNullable( getProject().getId() )
				.ifPresent( projectService::deleteProject );

		refresh();

		selectNextProject( openProjectList, i);
		selectNextProject( closedProjectList, j );
	}

	private void selectNextProject( JList<Project> list, int oldIndex ) {

		if ( oldIndex == -1 ) { return;
		}

		int newIndex = Math.max( 0, oldIndex - 1 );

		if ( list.getModel().getSize() > 0 ) {
			list.setSelectedIndex( newIndex );
		}
	}

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

	private Project buildProject() {

		Project p = new Project();
		p.setId( getProject().getId() );
		p.setProjectName( us1RightPanelGen.getProjectNameField().getText() );
		p.setDescription( us1RightPanelGen.getDescriptionArea().getText() );
		p.setCompleted( p.getId() != null && us1RightPanelGen.getCompletedCheckBox().isSelected() );
		return p;
	}

	private boolean isDirty() {
		Project p = getProject();
		return !refreshing && ( p.getId() == null || !Pojos.projectsEqual( p, buildProject() ) );
	}
}
