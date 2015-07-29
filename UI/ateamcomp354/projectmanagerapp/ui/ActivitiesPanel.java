package ateamcomp354.projectmanagerapp.ui;

import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;
import ateamcomp354.projectmanagerapp.services.UserService;
import ateamcomp354.projectmanagerapp.ui.gen.SplitPane1Gen;
import ateamcomp354.projectmanagerapp.ui.util.FrameSaver;
import ateamcomp354.projectmanagerapp.ui.util.TwoColumnListCellRenderer;

import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import javax.swing.*;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ActivitiesPanel {

	private final ApplicationContext appCtx;
	private ActivityService activityService;

	private UserService userService;
	private ProjectService projectService;
	private SwapInterface swap;

	private SplitPane1Gen splitPane1Gen;
	private List<Activity> activities;
	private Project project;
	private int idIndexes[];
	private int completedIdIndexes[];
	private int dependencyIndexes[];
	private List<Integer> dependencyComboIndexes;
	private int selectedDependencyId;
	
	private int selectedAssigneeId;
	private int assigneeIndexes[];
	private List<Integer> assigneeComboIndexes;
	
	private JList<Activity> activityList;
	private JList<Activity> completedActivityList;
	private JList<Users> assigneeList;
	private JList<String> dependencyList;
	
	private DefaultListModel<String> assigneeListModel;
	private DefaultListModel<String> dependencyListModel;
	
	private int projectId = 1;
	private int activityId = 0;
	private int oldYPos = -1;
	

	public ActivitiesPanel(ApplicationContext appCtx, SwapInterface swap) {

		this.appCtx = appCtx;
		this.swap = swap;
		splitPane1Gen = new SplitPane1Gen();
		splitPane1Gen.getBtnChart().setVisible(false);

		activityList = new JList<>();
		activityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		completedActivityList = new JList<>();
		completedActivityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		assigneeListModel = new DefaultListModel<String>();
		dependencyListModel = new DefaultListModel<String>();

		splitPane1Gen.getStatusComboBox().addItem("Open");
		splitPane1Gen.getStatusComboBox().addItem("In Progress");
		splitPane1Gen.getStatusComboBox().addItem("Resolved");

		// REMOVE THINGS FROM SPLITPANE THAT ARE NOT NEEDED IN ACTIVITY VIEW
		splitPane1Gen.getBtnEVanalysis().setVisible(false);
		splitPane1Gen.getBtnView().setVisible(false);
		splitPane1Gen.getTopLabel().setVisible(false);

		// TEMPORARY - MAKE FIELDS READ ONLY FOR FIRST ITERATION
		splitPane1Gen.getEarliestStartField().setEnabled(true);
		splitPane1Gen.getLatestStartField().setEnabled(true);
		splitPane1Gen.getEarliestFinishField().setEnabled(true);
		splitPane1Gen.getLatestFinishField().setEnabled(true);
		splitPane1Gen.getAssigneesComboBox().setEnabled(true);
		splitPane1Gen.getAddAssigneeButton().setEnabled(true);
		splitPane1Gen.getRemoveAssigneeButton().setEnabled(true);
		splitPane1Gen.getAssigneeScrollPane().setEnabled(true);
		
		splitPane1Gen.resetDatePickers();
		
		splitPane1Gen.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				activityId = 0;
				clear(true);
				activityService = appCtx.getActivityService(projectId);
				userService = appCtx.getUserService();
				projectService = appCtx.getProjectService();
				activities = activityService.getActivities();
				project = activityService.getProject();

				splitPane1Gen.getTopLabel().setText(project.getProjectName());
				JLabel projectLabelO = new JLabel(project.getProjectName()
						+ " - open activities");
				JLabel projectLabelC = new JLabel(project.getProjectName()
						+ " - resolved activities");
				splitPane1Gen.getListScrollPane().setColumnHeaderView(
						projectLabelO);
				splitPane1Gen.getCompletedScrollPane().setColumnHeaderView(
						projectLabelC);

				fillActivitiesList();
				
				
			}
		});

		// Saving the activity from the edited fields
		splitPane1Gen.getSaveActivityButton().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						saveActivity();
					}
				});

		// adds a new activity. only finalized on save
		splitPane1Gen.getAddButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!clear(true))
					return;
				activityId = -1;
				activityList.clearSelection();
				completedActivityList.clearSelection();
				splitPane1Gen.getDeleteButton().setEnabled(false);
				splitPane1Gen.getActivityNameField().setText("New Activity");
				splitPane1Gen.getDependenciesComboBox().removeAllItems();
				
				toggleDependenciesAndAssignees(false);
				
				setReadOnly(false);
			}
		});

		// deletes currently selected activity
		splitPane1Gen.getDeleteButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteActivity(activityId);
			}
		});

		//adds a dependency to the selected project
		splitPane1Gen.getAddDependencyButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addDependency();
			}
		});
		
		//removes selected dependency from selected project
		splitPane1Gen.getRemoveDependencyButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeDependency();
			}
		});
		
		// adds a member to the selected activity
		splitPane1Gen.getAddAssigneeButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addAssignee();
			}
		});
		
		// removes selected member from the selected activity
		splitPane1Gen.getRemoveAssigneeButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeAssignee();
			}
		});

		splitPane1Gen.getBackBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				swap.removeSaveFrame();
				swap.showProjectsView(projectId);
				
			}
		});
		
		splitPane1Gen.getEarliestStartDatePicker().addDateListener(new DateListener() {
			@Override
			public void dateChanged(DateEvent e) {
				int yearInt = splitPane1Gen.getEarliestStartDatePicker().getCalendar().get(Calendar.YEAR);
				int monthInt = splitPane1Gen.getEarliestStartDatePicker().getCalendar().get(Calendar.MONTH);
				int dateInt = splitPane1Gen.getEarliestStartDatePicker().getCalendar().get(Calendar.DATE);
				
				String year = yearInt+"";
				String month = monthInt+1 < 10 ? "0"+(monthInt+1) : ""+(monthInt+1);
				String day = dateInt < 10 ? "0"+dateInt : ""+dateInt;
				
				splitPane1Gen.getEarliestStartField().setText(year+month+day);
			}
		});
		
		splitPane1Gen.getLatestStartDatePicker().addDateListener(new DateListener() {
			@Override
			public void dateChanged(DateEvent e) {
				int yearInt = splitPane1Gen.getLatestStartDatePicker().getCalendar().get(Calendar.YEAR);
				int monthInt = splitPane1Gen.getLatestStartDatePicker().getCalendar().get(Calendar.MONTH);
				int dateInt = splitPane1Gen.getLatestStartDatePicker().getCalendar().get(Calendar.DATE);
				
				String year = yearInt+"";
				String month = monthInt+1 < 10 ? "0"+(monthInt+1) : ""+(monthInt+1);
				String day = dateInt < 10 ? "0"+dateInt : ""+dateInt;
				
				splitPane1Gen.getLatestStartField().setText(year+month+day);
			}
		});
		
		splitPane1Gen.getEarliestFinishDatePicker().addDateListener(new DateListener() {
			@Override
			public void dateChanged(DateEvent e) {
				int yearInt = splitPane1Gen.getEarliestFinishDatePicker().getCalendar().get(Calendar.YEAR);
				int monthInt = splitPane1Gen.getEarliestFinishDatePicker().getCalendar().get(Calendar.MONTH);
				int dateInt = splitPane1Gen.getEarliestFinishDatePicker().getCalendar().get(Calendar.DATE);
				
				String year = yearInt+"";
				String month = monthInt+1 < 10 ? "0"+(monthInt+1) : ""+(monthInt+1);
				String day = dateInt < 10 ? "0"+dateInt : ""+dateInt;
				
				splitPane1Gen.getEarliestFinishField().setText(year+month+day);
			}
		});
		
		splitPane1Gen.getLatestFinishDatePicker().addDateListener(new DateListener() {
			@Override
			public void dateChanged(DateEvent e) {
				int yearInt = splitPane1Gen.getLatestFinishDatePicker().getCalendar().get(Calendar.YEAR);
				int monthInt = splitPane1Gen.getLatestFinishDatePicker().getCalendar().get(Calendar.MONTH);
				int dateInt = splitPane1Gen.getLatestFinishDatePicker().getCalendar().get(Calendar.DATE);
				
				String year = yearInt+"";
				String month = monthInt+1 < 10 ? "0"+(monthInt+1) : ""+(monthInt+1);
				String day = dateInt < 10 ? "0"+dateInt : ""+dateInt;
				
				splitPane1Gen.getLatestFinishField().setText(year+month+day);
			}
		});
	}

	protected void toggleDependenciesAndAssignees(boolean b) {
		splitPane1Gen.getDependenciesLabel().setVisible(b);
		splitPane1Gen.getDependencyScrollPane().setVisible(b);
		splitPane1Gen.getDependenciesComboBox().setVisible(b);
		splitPane1Gen.getAddDependencyButton().setVisible(b);
		splitPane1Gen.getDelDepButton().setVisible(b);
		
		splitPane1Gen.getAssigneesLabel().setVisible(b);
		splitPane1Gen.getAssigneeScrollPane().setVisible(b);
		splitPane1Gen.getAssigneesComboBox().setVisible(b);
		splitPane1Gen.getAddAssigneeButton().setVisible(b);
		splitPane1Gen.getDelAssignButton().setVisible(b);
		
		if(!b) { //small view
			if(oldYPos == -1) {
				oldYPos = splitPane1Gen.getSaveActivityButton().getLocation().y;
				splitPane1Gen.getSaveActivityButton().setLocation(splitPane1Gen.getSaveActivityButton().getLocation().x, splitPane1Gen.getSaveActivityButton().getLocation().y - 275);
			}
			else {
				splitPane1Gen.getSaveActivityButton().setLocation(splitPane1Gen.getSaveActivityButton().getLocation().x, oldYPos - 275);
			}
		}
		else {
			splitPane1Gen.getSaveActivityButton().setLocation(splitPane1Gen.getSaveActivityButton().getLocation().x, oldYPos == -1 ? splitPane1Gen.getSaveActivityButton().getLocation().y : oldYPos);
			oldYPos = -1;
		}
	}

	public JComponent getComponent() {
		return splitPane1Gen;
	}

	public void setProjectId(int id) {
		projectId = id;
	}

	private boolean clear(boolean allowDialog) {
		int dialogResult = 1;
		if (isDirty() && allowDialog) {
			int dialogButton = JOptionPane.YES_NO_CANCEL_OPTION;
			dialogResult = JOptionPane.showConfirmDialog(splitPane1Gen,
					"Would you like to save "
							+ splitPane1Gen.getActivityNameField().getText()
							+ " first?", "You forgot to save", dialogButton);
		}
		if (dialogResult != 2) {
			if (dialogResult == 0)
				saveActivity();
			splitPane1Gen.getActivityNameField().setText("");
			splitPane1Gen.getStatusComboBox().setSelectedIndex(0);
			splitPane1Gen.getMaxDurationField().setText("1");
			splitPane1Gen.getDurationField().setText("1");
			splitPane1Gen.getDescriptionArea().setText("");
			splitPane1Gen.getPlannedValueField().setText("0");
			splitPane1Gen.getActualCostField().setText("0");
			
			splitPane1Gen.resetDatePickers();
			
			splitPane1Gen.getDependenciesComboBox().removeAllItems();
			splitPane1Gen.getAssigneesComboBox().removeAllItems();
			assigneeListModel.clear();
			dependencyListModel.clear();
			
			return true;
		}
		return false;
	}

	private void saveActivity() {
		String errorString = "";
		Activity activity = new Activity();
		activity.setId(activityId);
		if(splitPane1Gen.getActivityNameField().getText().trim().equals("")) {
			errorString += "Activity Must Have a Name\n";
		}
		else {
			activity.setLabel(splitPane1Gen.getActivityNameField().getText());
		}

		activity.setProjectId(projectId);
		activity.setStatus(Status.values()[splitPane1Gen.getStatusComboBox()
				.getSelectedIndex()]);
		activity.setEarliestStart(Integer.parseInt(splitPane1Gen
				.getEarliestStartField().getText()));
		activity.setLatestStart(Integer.parseInt(splitPane1Gen
				.getLatestStartField().getText()));
		activity.setEarliestFinish(Integer.parseInt(splitPane1Gen
				.getEarliestFinishField().getText()));
		activity.setLatestFinish(Integer.parseInt(splitPane1Gen
				.getLatestFinishField().getText()));
		activity.setMaxDuration(Integer.parseInt(splitPane1Gen
				.getMaxDurationField().getText()));
		activity.setDuration(Integer.parseInt(splitPane1Gen.getDurationField()
				.getText()));
		activity.setDescription(splitPane1Gen.getDescriptionArea().getText());
		
		Integer iVal = 0;
		try {
			iVal = Integer.parseInt(splitPane1Gen.getPlannedValueField().getText());
			if(iVal < 0) {
				errorString += "Planned Value must be a valid non-negative whole number\n";
			}
			else {
				activity.setPlannedValue(iVal);
			}
		}
		catch(NumberFormatException e) {
			errorString += "Planned Value must be a valid non-negative whole number\n";
		}
		
		iVal = 0;
		
		try {
			iVal = Integer.parseInt(splitPane1Gen.getActualCostField().getText());
			if(iVal < 0) {
				errorString += "Actual Cost must be a valid non-negative whole number\n";
			}
			else {
				activity.setActualCost(iVal);
			}
		}
		catch(NumberFormatException e) {
			errorString += "Actual Cost must be a valid non-negative whole number\n";
		}
		
		if(activity.getEarliestStart() > activity.getLatestFinish()) {
			errorString += "Start Date may not exceed End Date\n";
		}
		
		String endDateAfterStartDate = anyEndDateAfterStartDate(Integer.parseInt(splitPane1Gen.getEarliestStartField().getText()), activity.getId());
		
		if(!endDateAfterStartDate.equals("")) {
			errorString += endDateAfterStartDate+"\n";
		}
		
		String startDateAfterEndDate = anyStartDateAfterEndDate(Integer.parseInt(splitPane1Gen.getLatestFinishField().getText()), activity.getId());
		
		if(!startDateAfterEndDate.equals("")) {
			errorString += startDateAfterEndDate+"\n";
		}

		if(errorString.equals("")) {
			addOrUpdateActivity(activity);

			projectService.updateProjectBudgetAtCompletion(projectId);
			projectService.updateActualCostAtCompletion(projectId);
			
			boolean found = false;
			for (int i = 0; i < idIndexes.length; i++) {
				if (idIndexes[i] == activityId) {
					found = true;
					activityList.setSelectedIndex(i);
					break;
				}
			}
			if (!found) {
				for (int i = 0; i < completedIdIndexes.length; i++) {
					if (completedIdIndexes[i] == activityId) {
						completedActivityList.setSelectedIndex(i);
						break;
					}
				}
			}
			setReadOnly(activity.getStatus() == Status.RESOLVED);
		}
		else {
			JOptionPane.showMessageDialog(null, errorString);
		}
	}

	private String anyStartDateAfterEndDate(Integer finishDate, Integer ID) {
		List<Integer> activities = activityService.getDependents(ID);
		Integer earliestDate = -1;
		String earliestActivity = "";
		
		for(Integer a : activities) {
			if(activityService.getActivity(a).getEarliestStart() < finishDate) {
				earliestDate = activityService.getActivity(a).getEarliestStart();
				earliestActivity = activityService.getActivity(a).getLabel();
			}
		}
		
		if(earliestDate != -1) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			try {
				return "End Date must be before "+new SimpleDateFormat("yyyy/MM/dd").format(formatter.parse(earliestDate.toString()))+" to not overlap activity "+earliestActivity;
			} catch (ParseException e) {
				return "End Date must be before any overlapping activities";
			}
		}
		return "";
	}

	private String anyEndDateAfterStartDate(Integer startDate, Integer ID) {
		List<Integer> activities = activityService.getDependencies(ID);
		Integer latestDate = -1;
		String latestActivity = "";
		
		for(Integer a : activities) {
			if(activityService.getActivity(a).getEarliestStart() > startDate) {
				latestDate = activityService.getActivity(a).getEarliestStart();
				latestActivity = activityService.getActivity(a).getLabel();
			}
		}
		
		if(latestDate != -1) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			try {
				return "Start Date must be after "+new SimpleDateFormat("yyyy/MM/dd").format(formatter.parse(latestDate.toString()))+" to not overlap activity "+latestActivity;
			} catch (ParseException e) {
				return "Start Date must be after any overlapping activities";
			}
		}
		return "";
	}

	private void addOrUpdateActivity(Activity a) {
		if (activityId < 0) {
			a.setId(null);
			activityService.addActivity(a);
			activities = activityService.getActivities();
			int lastIndex = activities.size() - 1;
			activityId = activities.get(lastIndex).getId();

			// We disable dependency functionality until the activity is saved
			splitPane1Gen.getAssigneesComboBox().setEnabled(true);
			splitPane1Gen.getAddAssigneeButton().setEnabled(true);
			splitPane1Gen.getRemoveAssigneeButton().setEnabled(true);
			splitPane1Gen.getAssigneeScrollPane().setEnabled(true);
			splitPane1Gen.getDeleteButton().setEnabled(true);
		} else {
			activityService.updateActivity(a);
			activities = activityService.getActivities();
		}
		fillActivitiesList();
	}

	private void selectActivity(int id) {
		if (!clear(true))
			return;
		toggleDependenciesAndAssignees(true);
		Activity activity = activityService.getActivity(id);
		activityId = id;
		splitPane1Gen.getActivityNameField().setText(activity.getLabel());
		splitPane1Gen.getStatusComboBox().setSelectedIndex(
				activity.getStatus().ordinal());
		splitPane1Gen.getEarliestStartField().setText(
				Integer.toString(activity.getEarliestStart()));
		splitPane1Gen.getLatestStartField().setText(
				Integer.toString(activity.getLatestStart()));
		splitPane1Gen.getEarliestFinishField().setText(
				Integer.toString(activity.getEarliestFinish()));
		splitPane1Gen.getLatestFinishField().setText(
				Integer.toString(activity.getLatestFinish()));
		splitPane1Gen.getMaxDurationField().setText(
				Integer.toString(activity.getMaxDuration()));
		splitPane1Gen.getDurationField().setText(
				Integer.toString(activity.getDuration()));
	
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			splitPane1Gen.getEarliestStartDatePicker().setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
			splitPane1Gen.getLatestStartDatePicker().setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
			splitPane1Gen.getEarliestFinishDatePicker().setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
			splitPane1Gen.getLatestFinishDatePicker().setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
			splitPane1Gen.getEarliestStartDatePicker().setDate(formatter.parse(activity.getEarliestStart().toString()));
			splitPane1Gen.getLatestStartDatePicker().setDate(formatter.parse(activity.getLatestStart().toString()));
			splitPane1Gen.getEarliestFinishDatePicker().setDate(formatter.parse(activity.getEarliestFinish().toString()));
			splitPane1Gen.getLatestFinishDatePicker().setDate(formatter.parse(activity.getLatestFinish().toString()));
		} catch (ParseException e) {}
		
		splitPane1Gen.getDescriptionArea().setText(activity.getDescription());
		splitPane1Gen.getDeleteButton().setEnabled(true);
		splitPane1Gen.getPlannedValueField().setText(Integer.toString(activity.getPlannedValue()));
		splitPane1Gen.getActualCostField().setText(Integer.toString(activity.getActualCost()));
		showDependencies(id);
		showAssignees(id);
		setReadOnly(activity.getStatus() == Status.RESOLVED);
	}
	private void showDependencies(int id) {
		dependencyListModel.clear();
		selectedDependencyId = 0;
		List<Integer> dependencies = activityService.getDependencies(id);
		dependencyIndexes = new int[dependencies.size()];
		
		for (int i = 0; i < dependencies.size(); i++)
		{
			dependencyListModel.addElement(activityService.getActivity(dependencies.get(i)).getLabel());
			dependencyIndexes[i] = activityService.getActivity(dependencies.get(i)).getId();
		}
		
		dependencyList = new JList<String>(dependencyListModel);
		dependencyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		splitPane1Gen.getDependencyScrollPane().setViewportView(dependencyList);
		splitPane1Gen.getDependencyScrollPane().validate();
		fillDependencyComboBox();

		dependencyList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (dependencyList.locationToIndex(e.getPoint()) == -1)
					return;
				selectedDependencyId = dependencyIndexes[dependencyList
						.locationToIndex(e.getPoint())];
			}
		});
	}

	private void fillDependencyComboBox() {
		splitPane1Gen.getDependenciesComboBox().removeAllItems();
		dependencyComboIndexes = activityService.getDependencies(0);
		dependencyComboIndexes.clear();
		for (int i = 0; i < activities.size(); i++) {
			boolean isDependency = false;
			for (int j = 0; j < dependencyIndexes.length; j++) {
				if (activities.get(i).getId() == dependencyIndexes[j]) {
					isDependency = true;
					break;
				}
			}
			// only checks for direct circular dependencies. A graph traversal
			// will be needed later
			boolean circular = false;
			for (Integer dependent : activityService.getDependents(activityId)) {
				if (dependent == activities.get(i).getId()) {
					circular = true;
					break;
				}
			}
			if (!isDependency && activities.get(i).getId() != activityId
					&& !circular
					&& activities.get(i).getStatus() != Status.RESOLVED) {
				splitPane1Gen.getDependenciesComboBox().addItem(
						activities.get(i).getLabel());
				dependencyComboIndexes.add(activities.get(i).getId());
			}
		}
	}
	
	private void addDependency()
	{	
		if (splitPane1Gen.getDependenciesComboBox().getSelectedIndex() == -1) return;
		int toAdd = dependencyComboIndexes.get(splitPane1Gen.getDependenciesComboBox().getSelectedIndex());
		
		try {
			activityService.addDependency(toAdd, activityId);
			
			String errorString = "";
			String endDateAfterStartDate = anyEndDateAfterStartDate(Integer.parseInt(splitPane1Gen.getEarliestStartField().getText()), activityId);
			
			if(!endDateAfterStartDate.equals("")) {
				errorString += endDateAfterStartDate+"\n";
			}
			
			String startDateAfterEndDate = anyStartDateAfterEndDate(Integer.parseInt(splitPane1Gen.getLatestFinishField().getText()), activityId);
			
			if(!startDateAfterEndDate.equals("")) {
				errorString += startDateAfterEndDate+"\n";
			}
					
			if(!errorString.equals("")) {
				JOptionPane.showMessageDialog(null, errorString);
				activityService.deleteDependency(toAdd, activityId);
			}
		}
		catch(ServiceFunctionalityException e) {
			JOptionPane.showMessageDialog(null, "Adding this Dependency would create a circular dependency chain. Unfortunately this is not allowed.");
		}
		showDependencies(activityId);
	}

	private void removeDependency() {
		activityService.deleteDependency(selectedDependencyId, activityId);
		selectedDependencyId = 0;
		showDependencies(activityId);
	}

	private void deleteActivity(int id) {
		clear(false);
		activityService.deleteActivity(id);
		activityId = 0;
		fillActivitiesList();
	}

	private void fillActivitiesList() {
		activities = activityService.getActivities();
		idIndexes = new int[activities.size()];
		completedIdIndexes = new int[activities.size()];

		DefaultListModel<Activity> activitiesModel = new DefaultListModel<>();
		DefaultListModel<Activity> completedActivitiesModel = new DefaultListModel<>();

		int i = 0;
		int j = 0;
		for (Activity a : activities) {
			if (a.getStatus() != Status.RESOLVED) {
				activitiesModel.addElement(a);
				idIndexes[i] = a.getId();
				i++;
			} else {
				completedActivitiesModel.addElement(a);
				completedIdIndexes[j] = a.getId();
				j++;
			}
		}

		TwoColumnListCellRenderer<Activity> renderer = new TwoColumnListCellRenderer<>(
				Activity::getLabel, a -> a.getStatus().getPrettyString());

		activityList = new JList<>(activitiesModel);
		activityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		activityList.setCellRenderer(renderer);

		completedActivityList = new JList<>(completedActivitiesModel);
		completedActivityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		completedActivityList.setCellRenderer(renderer);

		splitPane1Gen.getListScrollPane().setViewportView(activityList);
		splitPane1Gen.getListScrollPane().validate();
		splitPane1Gen.getCompletedScrollPane().setViewportView(
				completedActivityList);
		splitPane1Gen.getCompletedScrollPane().validate();

		activityList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = activityList.locationToIndex(e.getPoint());

				if (index >= 0) {
					selectActivity(idIndexes[index]);
					completedActivityList.clearSelection();
				}
			}
		});

		completedActivityList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = completedActivityList.locationToIndex(e.getPoint());

				if (index >= 0) {
					selectActivity(completedIdIndexes[index]);
					activityList.clearSelection();
				}
			}
		});
	}
	
	private void showAssignees(int activityId) {		
		
		assigneeListModel.clear();
		
		List<Users> assignees = activityService.getAssigneesForActivity(activityId);
		
		selectedAssigneeId = 0;
		assigneeIndexes = new int[assignees.size()];
		
		for (int i = 0; i < assignees.size(); i++)
		{
			assigneeListModel.addElement(assignees.get(i).getFirstName() + " " + assignees.get(i).getLastName());
			assigneeIndexes[i] = assignees.get(i).getId();
		}
		
		JList<String> assigneeList = new JList<String>(assigneeListModel);
		assigneeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		splitPane1Gen.getAssigneeScrollPane().setViewportView(assigneeList);
		splitPane1Gen.getAssigneeScrollPane().validate();
		
		fillAssigneeComboBox();
		
		assigneeList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if (assigneeList.locationToIndex(e.getPoint()) == -1) return;
				selectedAssigneeId = assigneeIndexes[assigneeList.locationToIndex(e.getPoint())];
			}
		});
	}
	
	private void fillAssigneeComboBox() {
		splitPane1Gen.getAssigneesComboBox().removeAllItems();
		assigneeComboIndexes = new ArrayList<Integer>();
		assigneeComboIndexes.clear();
		
		List<Users> notAssigned = activityService.getUnassignedMembersForActivity(activityId);
		
		for (int i = 0; i < notAssigned.size(); i++) {
			splitPane1Gen.getAssigneesComboBox().addItem(notAssigned.get(i).getFirstName() + " " + notAssigned.get(i).getLastName());
			assigneeComboIndexes.add(notAssigned.get(i).getId());
		}
	}
	
	private void addAssignee()
	{	
		if (splitPane1Gen.getAssigneesComboBox().getSelectedIndex() == -1) return;
		int toAdd = assigneeComboIndexes.get(splitPane1Gen.getAssigneesComboBox().getSelectedIndex());
		activityService.addUserToActivity(activityId, userService.getUser(toAdd));
		showAssignees(activityId);
	}
	
	private void removeAssignee()
	{
		if (selectedAssigneeId == 0) return;
		activityService.deleteUserFromActivity(activityId, userService.getUser(selectedAssigneeId));
		selectedAssigneeId = 0;
		showAssignees(activityId);
	}
	
	private void setReadOnly(boolean readOnly)
	{
		splitPane1Gen.getActivityNameField().setEnabled(!readOnly);
		splitPane1Gen.getPlannedValueField().setEnabled(!readOnly);
		splitPane1Gen.getActualCostField().setEnabled(!readOnly);
		splitPane1Gen.getDescriptionArea().setEnabled(!readOnly);
		splitPane1Gen.getEarliestStartDatePicker().setEnabled(!readOnly);
		splitPane1Gen.getLatestFinishDatePicker().setEnabled(!readOnly);
		// splitPane1Gen.getEarliestStartField().setEnabled(!readOnly);
		// splitPane1Gen.getLatestStartField().setEnabled(!readOnly);
		// splitPane1Gen.getEarliestFinishField().setEnabled(!readOnly);
		// splitPane1Gen.getLatestFinishField().setEnabled(!readOnly);
		splitPane1Gen.getMaxDurationField().setEnabled(!readOnly);
		splitPane1Gen.getDurationField().setEnabled(!readOnly);
		
		splitPane1Gen.getDependenciesComboBox().setEnabled(!readOnly);
		splitPane1Gen.getDependencyScrollPane().setEnabled(!readOnly);
		splitPane1Gen.getAddDependencyButton().setEnabled(!readOnly);
		splitPane1Gen.getRemoveDependencyButton().setEnabled(!readOnly);
		
		splitPane1Gen.getAssigneesComboBox().setEnabled(!readOnly);
		splitPane1Gen.getAssigneeScrollPane().setEnabled(!readOnly);
		splitPane1Gen.getAddAssigneeButton().setEnabled(!readOnly);
		splitPane1Gen.getRemoveAssigneeButton().setEnabled(!readOnly);
	}

	private boolean isDirty() {
		// if this is a new activity, it is dirty
		if (activityId == -1)
			return true;

		// if the view was just opened, there is no activity to be dirty
		if (activityId == 0)
			return false;

		Activity a = activityService.getActivity(activityId);
		
		//if this is not a new activity, but has been changed, it is dirty
		return (!a.getLabel().equals(splitPane1Gen.getActivityNameField().getText())
				//|| a.getEarliestStart() != Integer.parseInt(splitPane1Gen.getEarliestStartField().getText())
				//|| a.getLatestStart() != Integer.parseInt(splitPane1Gen.getLatestStartField().getText())
				//|| a.getEarliestFinish() != Integer.parseInt(splitPane1Gen.getEarliestStartField().getText())
				//|| a.getLatestFinish() != Integer.parseInt(splitPane1Gen.getLatestFinishField().getText())
				|| a.getStatus().ordinal() != splitPane1Gen.getStatusComboBox().getSelectedIndex()
				|| a.getMaxDuration() != Integer.parseInt(splitPane1Gen.getMaxDurationField().getText())
				|| a.getDuration() != Integer.parseInt(splitPane1Gen.getDurationField().getText())
				|| a.getPlannedValue() != Integer.parseInt(splitPane1Gen.getPlannedValueField().getText())
				|| a.getActualCost() != Integer.parseInt(splitPane1Gen.getActualCostField().getText())
				|| !a.getDescription().equals(splitPane1Gen.getDescriptionArea().getText()));
	}
}
