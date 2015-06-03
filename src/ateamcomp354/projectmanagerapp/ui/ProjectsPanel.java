package ateamcomp354.projectmanagerapp.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import ateamcomp354.projectmanagerapp.model.Pojos;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.ui.gen.SplitPane1Gen;
import ateamcomp354.projectmanagerapp.ui.gen.US1RightPanelGen;
import ateamcomp354.projectmanagerapp.ui.util.TwoColumnListCellRenderer;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectsPanel {

	private static final String VIEW_BTN_TXT = "View Activities";
	private static final String EDIT_PROJECT_TITLE_LBL_TXT = "Edit Project";
	private static final String ADD_PROJECT_TITLE_LBL_TXT = "New Project";
	private static final String NEW_PROJECT_NAME_DEFAULT_VALUE = "My new project";
	private static final String PROJECT_COMPLETION_FMT = "%s%%";

	private final ProjectService projectService;
	private final SwapInterface swap;

	private final SplitPane1Gen splitPane1Gen;
	private final US1RightPanelGen us1RightPanelGen;

	private final JList<Project> openProjectList;
	private final DefaultListModel<Project> openProjectsModel;
	private final JList<Project> closedProjectList;
	private final DefaultListModel<Project> closedProjectsModel;

	private Map<Integer,Integer> projectCompletions;

	private final Project newProjectTemplate;
	private Optional<Project> selectedProject;

	public ProjectsPanel( ApplicationContext appCtx, SwapInterface swap )
	{
		projectService = appCtx.getProjectService();
		this.swap = swap;
		
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
					Integer pc = projectCompletions.get( p.getId() );
					return String.format( PROJECT_COMPLETION_FMT, pc == null ? "?" : pc.toString() );
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
		newProjectTemplate.setCompleted( false );

		selectedProject = Optional.empty();
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

		fillProjectLists();

		boolean select = projectId != null;

		if ( select ) {
			select = !selectProject( openProjectList, projectId );
		}

		if ( select ) {
			select = !selectProject( closedProjectList, projectId );
		}

		if ( (projectId == null | select) && !openProjectsModel.isEmpty() ) {
			openProjectList.setSelectedIndex( 0 );
		}
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
		projects.stream()
				.filter( p -> !p.getCompleted() )
				.forEach( openProjectsModel::addElement );


		closedProjectsModel.clear();
		projects.stream()
				.filter( p -> p.getCompleted() )
				.forEach( closedProjectsModel::addElement );

		projectCompletions = projects.stream()
				.collect( Collectors.toMap( Project::getId, p -> projectService.getProjectCompletion( p.getId() ) ) );
	}

	private void openProjectSelected( ListSelectionEvent e ) {
		projectSelected( e, openProjectList, closedProjectList );
	}

	private void closedProjectSelected( ListSelectionEvent e ) {
		projectSelected( e, closedProjectList, openProjectList );
	}

//	private JList<Project> oldList;
//	private int oldSelectedIndex;

	private void projectSelected( ListSelectionEvent e, JList<Project> list1, JList<Project> list2 ) {

		if ( e.getValueIsAdjusting() ) { return; }

		int i = list1.getSelectedIndex();
		if ( i == -1 ) {
//			oldList = list1;
//			oldSelectedIndex = -1;
			splitPane1Gen.getDeleteButton().setEnabled( false );
			splitPane1Gen.getBtnView().setEnabled( false );
			return;
		}

		list2.clearSelection();

//		if ( checkDirty() ) {
//			return;
//		}
//
//		oldList = list1;
//		oldSelectedIndex = i;

		selectedProject = Optional.of( list1.getModel().getElementAt( i ) );

		splitPane1Gen.getDeleteButton().setEnabled( getProject().getCompleted() );
		splitPane1Gen.getBtnView().setEnabled( true );

		displayProject();
	}

//	private boolean checkDirty( ) {
//
//		if ( oldList != null && isDirty() ) {
//
//			int r = JOptionPane.showConfirmDialog(
//					splitPane1Gen,
//					"Would you like to save " + us1RightPanelGen.getProjectNameField().getText() + " first?",
//					"You forgot to save",
//					JOptionPane.YES_NO_CANCEL_OPTION
//			);
//
//			if ( r == JOptionPane.YES_OPTION ) {
//
//				setValueIsAdjusting(true);
//				saveProjectClicked();
//				setValueIsAdjusting(false);
//			} else if ( r == JOptionPane.CANCEL_OPTION ) {
//
//				setValueIsAdjusting( true );
//				if ( oldSelectedIndex == -1) {
//					oldList.clearSelection();
//				}
//				else {
//					oldList.setSelectedIndex( oldSelectedIndex );
//				}
//				setValueIsAdjusting( false );
//
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	private void setValueIsAdjusting( boolean adjusting ) {
//		openProjectList.getSelectionModel().setValueIsAdjusting( adjusting );
//		closedProjectList.getSelectionModel().setValueIsAdjusting( adjusting );
//	}

	private void viewActivitiesClicked() {

		Optional.ofNullable( getProject().getId() )
				.ifPresent( swap::showActivitiesView );
	}

	private void addProjectClicked() {

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

		selectNextProject( openProjectList, i );
		selectNextProject( closedProjectList, j );
	}

	private void selectNextProject( JList<Project> list, int oldIndex ) {

		if ( oldIndex == -1 ) { return; }

		int newIndex = Math.max( 0, oldIndex - 1 );

		if ( list.getModel().getSize() > 0 ) {
			list.setSelectedIndex( newIndex );
		}
	}

	private void saveProjectClicked() {

		saveProject();

		Project p = getProject();
		if ( p.getId() == null ) {
			refresh();
			openProjectList.setSelectedIndex( openProjectsModel.getSize() - 1 );
		}
		else {
			refresh( p.getId() );
		}
	}

	private void saveProject() {

		Project p = buildProject();
		if ( p.getId() == null ) {
			projectService.addProject( p );
		}
		else {
			projectService.updateProject( p );
		}
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
		return !Pojos.projectsEqual( getProject(), buildProject() );
	}
}
