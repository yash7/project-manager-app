package ateamcomp354.projectmanagerapp.ui;

import java.util.List;

import javax.swing.*;

import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.ui.gen.SplitPane1Gen;
import ateamcomp354.projectmanagerapp.model.Status;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ActivitiesPanel {

	private final ApplicationContext appCtx;
	private ActivityService activityService;
	
	private SwapInterface swap;

	private SplitPane1Gen splitPane1Gen;
	private List<Activity> activities;
	private Project project;
	private int idIndexes[];
	private int completedIdIndexes[];
	private int dependencyIndexes[];
	private List<Integer> dependencyComboIndexes;
	private int selectedDependencyId;
	
	private int projectId = 1;
	private int activityId = 0;

	public ActivitiesPanel( ApplicationContext appCtx , SwapInterface swap) {

		this.appCtx = appCtx;
		this.swap = swap;
		splitPane1Gen = new SplitPane1Gen();

		splitPane1Gen.getStatusComboBox().addItem("Open");
		splitPane1Gen.getStatusComboBox().addItem("In Progress");
		splitPane1Gen.getStatusComboBox().addItem("Resolved");
		
		splitPane1Gen.getBtnManage().setVisible(false);
		splitPane1Gen.getBtnView().setVisible(false);
		
		//TEMPORARY - MAKE FIELDS READ ONLY FOR FIRST ITERATION
		splitPane1Gen.getEarliestStartField().setEnabled(false);
		splitPane1Gen.getLatestStartField().setEnabled(false);
		splitPane1Gen.getEarliestFinishField().setEnabled(false);
		splitPane1Gen.getLatestFinishField().setEnabled(false);
		splitPane1Gen.getAssigneesComboBox().setEnabled(false);
		splitPane1Gen.getAddAssigneeButton().setEnabled(false);
		splitPane1Gen.getRemoveAssigneeButton().setEnabled(false);
		splitPane1Gen.getAssigneeScrollPane().setEnabled(false);
		
		splitPane1Gen.getEarliestStartField().setText("coming soon");
		splitPane1Gen.getLatestStartField().setText("coming soon");
		splitPane1Gen.getEarliestFinishField().setText("coming soon");
		splitPane1Gen.getLatestFinishField().setText("coming soon");
		splitPane1Gen.getAssigneesComboBox().addItem("coming soon");
		splitPane1Gen.getAssigneeScrollPane().setColumnHeaderView(new JLabel("coming soon"));
		
		//occurs whenever the view is opened. projectId should be set from the project view
		splitPane1Gen.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				activityId = 0;
				clear(true);
				activityService = appCtx.getActivityService(projectId);
				activities = activityService.getActivities();
				project = activityService.getProject();
				
				splitPane1Gen.getTopLabel().setText(project.getProjectName());
				JLabel projectLabel = new JLabel(project.getProjectName()); 
				splitPane1Gen.getListScrollPane().setColumnHeaderView(projectLabel);
				splitPane1Gen.getCompletedScrollPane().setColumnHeaderView(projectLabel);
				
				fillActivitiesList();
			}
		});
		
		//Saving the activity from the edited fields
		splitPane1Gen.getSaveActivityButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				saveActivity();
			}
		});
		
		//adds a new activity. only finalized on save
		splitPane1Gen.getAddButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!clear(true)) return;
				activityId = -1;
				splitPane1Gen.getActivityNameField().setText("New Activity");
			}
		});
		
		//deletes currently selected activity
		splitPane1Gen.getDeleteButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				deleteActivity(activityId);
			}
		});
		
		//adds a dependency to the selected project
		splitPane1Gen.getAddDependencyButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addDependency();
			}
		});
		
		//removes selected dependency from selected project
		splitPane1Gen.getRemoveDependencyButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				removeDependency();
			}
		});
		
		splitPane1Gen.getLogoutButton().addActionListener(new LogoutListener(swap));
		
		//occurs whenever the view is opened. projectId should be set from the project view
		splitPane1Gen.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {	
				clear(true);
				activityService = appCtx.getActivityService(projectId);
				activities = activityService.getActivities();
				project = activityService.getProject();
				
				splitPane1Gen.getTopLabel().setText(project.getProjectName());
				JLabel projectLabel = new JLabel(project.getProjectName()); 
				splitPane1Gen.getListScrollPane().setColumnHeaderView(projectLabel);
				
				fillActivitiesList();
			}
		});
	}
	
	public JComponent getComponent()
	{
		return splitPane1Gen;
	}
	
	public void setProjectId(int id)
	{
		projectId = id;
	}
	
	private boolean clear(boolean allowDialog)
	{
		int dialogResult = 1;
		if (isDirty() && allowDialog)
		{
			int dialogButton = JOptionPane.YES_NO_CANCEL_OPTION;
			dialogResult = JOptionPane.showConfirmDialog(splitPane1Gen, "Would you like to save " + splitPane1Gen.getActivityNameField().getText() + " first?", "You forgot to save", dialogButton);	
		}
		if (dialogResult != 2)
		{
			if (dialogResult == 0) saveActivity();
			splitPane1Gen.getActivityNameField().setText("");
			splitPane1Gen.getStatusComboBox().setSelectedIndex(0);
			//splitPane1Gen.getEarliestStartField().setText("0");
			//splitPane1Gen.getLatestStartField().setText("0");
			//splitPane1Gen.getEarliestFinishField().setText("0");
			//splitPane1Gen.getLatestFinishField().setText("0");
			splitPane1Gen.getMaxDurationField().setText("0");
			splitPane1Gen.getDurationField().setText("0");
			splitPane1Gen.getDescriptionArea().setText("");
			return true;
		}
		return false;
	}
	
	private void saveActivity()
	{
		Activity activity = new Activity();
		activity.setId(activityId);
		activity.setLabel(splitPane1Gen.getActivityNameField().getText());
		activity.setProjectId(projectId);
		activity.setStatus(Status.values()[splitPane1Gen.getStatusComboBox().getSelectedIndex()]);
		//activity.setEarliestStart(Integer.parseInt(splitPane1Gen.getEarliestStartField().getText()));
		//activity.setLatestStart(Integer.parseInt(splitPane1Gen.getLatestStartField().getText()));
		//activity.setEarliestFinish(Integer.parseInt(splitPane1Gen.getEarliestFinishField().getText()));
		//activity.setLatestFinish(Integer.parseInt(splitPane1Gen.getLatestFinishField().getText()));
		activity.setMaxDuration(Integer.parseInt(splitPane1Gen.getMaxDurationField().getText()));
		activity.setDuration(Integer.parseInt(splitPane1Gen.getDurationField().getText()));
		activity.setDescription(splitPane1Gen.getDescriptionArea().getText());
		addOrUpdateActivity(activity);
		setReadOnly(activity.getStatus() == Status.RESOLVED);
	}
	
	private void addOrUpdateActivity(Activity a)
	{
		if(activityId < 0)
		{
			a.setId(null);
			activityService.addActivity(a);
			activities = activityService.getActivities();
			int lastIndex = activities.size() - 1;
			activityId = activities.get(lastIndex).getId();
			
			//We disable dependency functionality until the activity is saved
			splitPane1Gen.getAssigneesComboBox().setEnabled(false);
			splitPane1Gen.getAddAssigneeButton().setEnabled(false);
			splitPane1Gen.getRemoveAssigneeButton().setEnabled(false);
			splitPane1Gen.getAssigneeScrollPane().setEnabled(false);
		}
		else
		{
			activityService.updateActivity(a);
			activities = activityService.getActivities();
		}
		fillActivitiesList();
	}
	
	private void selectActivity(int id)
	{
		if (!clear(true)) return;
		Activity activity = activityService.getActivity(id);
		activityId = id;
		splitPane1Gen.getActivityNameField().setText(activity.getLabel());
		splitPane1Gen.getStatusComboBox().setSelectedIndex(activity.getStatus().ordinal());
		//splitPane1Gen.getEarliestStartField().setText(Integer.toString(activity.getEarliestStart()));
		//splitPane1Gen.getLatestStartField().setText(Integer.toString(activity.getLatestStart()));
		//splitPane1Gen.getEarliestFinishField().setText(Integer.toString(activity.getEarliestFinish()));
		//splitPane1Gen.getLatestFinishField().setText(Integer.toString(activity.getLatestFinish()));
		splitPane1Gen.getMaxDurationField().setText(Integer.toString(activity.getMaxDuration()));
		splitPane1Gen.getDurationField().setText(Integer.toString(activity.getDuration()));
		splitPane1Gen.getDescriptionArea().setText(activity.getDescription());
		showDependencies(id);
		setReadOnly(activity.getStatus() == Status.RESOLVED || project.getCompleted());
	}
	
	private void showDependencies(int id)
	{
		selectedDependencyId = 0;
		List<Integer> dependencies = activityService.getDependencies(id);
		String dependencyNames[] = new String[dependencies.size()];
		dependencyIndexes = new int[dependencies.size()];
		
		for (int i = 0; i < dependencies.size(); i++)
		{
			dependencyNames[i] = activityService.getActivity(dependencies.get(i)).getLabel();
			dependencyIndexes[i] = activityService.getActivity(dependencies.get(i)).getId();
		}
		
		JList<String> dependencyList = new JList<String>(dependencyNames);
		splitPane1Gen.getDependencyScrollPane().setViewportView(dependencyList);
		splitPane1Gen.getDependencyScrollPane().validate();
		fillDependencyComboBox();
		
		dependencyList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if (dependencyList.locationToIndex(e.getPoint()) == -1) return;
				selectedDependencyId = dependencyIndexes[dependencyList.locationToIndex(e.getPoint())];
			}
		});
	}
	
	private void fillDependencyComboBox()
	{
		splitPane1Gen.getDependenciesComboBox().removeAllItems();
		dependencyComboIndexes = activityService.getDependencies(0);
		dependencyComboIndexes.clear();
		for (int i = 0; i < activities.size(); i++)
		{
			boolean isDependency = false;
			for (int j = 0; j < dependencyIndexes.length; j ++)
			{
				if (activities.get(i).getId() == dependencyIndexes[j])
				{
					isDependency = true;
					break;
				}
			}
			//only checks for direct circular dependencies. A graph traversal will be needed later
			boolean circular = false;
			for (Integer dependent : activityService.getDependents(activityId))
			{
				if (dependent == activities.get(i).getId())
				{
					circular = true;
					break;
				}
			}
			if (!isDependency && activities.get(i).getId() != activityId && !circular && activities.get(i).getStatus() != Status.RESOLVED)
			{
				splitPane1Gen.getDependenciesComboBox().addItem(activities.get(i).getLabel());
				dependencyComboIndexes.add(activities.get(i).getId());
			}
		}
	}
	
	private void addDependency()
	{	
		if (splitPane1Gen.getDependenciesComboBox().getSelectedIndex() == -1) return;
		int toAdd = dependencyComboIndexes.get(splitPane1Gen.getDependenciesComboBox().getSelectedIndex());
		activityService.addDependency(toAdd, activityId);
		showDependencies(activityId);
	}
	
	private void removeDependency()
	{
		activityService.deleteDependency(selectedDependencyId, activityId);
		selectedDependencyId = 0;
		showDependencies(activityId);
	}
	
	private void deleteActivity(int id)
	{
		clear(false);
		activityService.deleteActivity(id);
		activityId = 0;
		fillActivitiesList();
	}
	
	private void fillActivitiesList()
	{
		activities = activityService.getActivities();
		String activityNames[] = new String[activities.size()];
		String completedActivityNames[] = new String[activities.size()];
		idIndexes = new int[activities.size()];
		completedIdIndexes = new int[activities.size()];

		for (int i = 0; i < activities.size(); i++)
		{
			if (activities.get(i).getStatus() != Status.RESOLVED)
			{
				String str = activities.get(i).getStatus() == Status.NEW ? "Open" : "In Progress";
				activityNames[i] = activities.get(i).getLabel() + "    " + str;
				idIndexes[i] = activities.get(i).getId();
			}
			else
			{
				completedActivityNames[i] = activities.get(i).getLabel() + "    Resolved";
				completedIdIndexes[i] = activities.get(i).getId();
			}
		}
		
		JList<String> activityList = new JList<String>(activityNames);
		JList<String> completedActivityList = new JList<String>(completedActivityNames);
		splitPane1Gen.getListScrollPane().setViewportView(activityList);
		splitPane1Gen.getListScrollPane().validate();
		splitPane1Gen.getCompletedScrollPane().setViewportView(completedActivityList);
		splitPane1Gen.getCompletedScrollPane().validate();
		
		activityList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = activityList.locationToIndex(e.getPoint());

				if (index >= 0)
				{
					selectActivity(idIndexes[index]);
					completedActivityList.clearSelection();
				}
			}
		});
		
		completedActivityList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = completedActivityList.locationToIndex(e.getPoint());

				if (index >= 0)
				{
					selectActivity(completedIdIndexes[index]);
					activityList.clearSelection();
				}
			}
		});
	}
	
	private void setReadOnly(boolean readOnly)
	{
		splitPane1Gen.getActivityNameField().setEnabled(!readOnly);
		//splitPane1Gen.getEarliestStartField().setEnabled(!readOnly);
		//splitPane1Gen.getLatestStartField().setEnabled(!readOnly);
		//splitPane1Gen.getEarliestFinishField().setEnabled(!readOnly);
		//splitPane1Gen.getLatestFinishField().setEnabled(!readOnly);
		splitPane1Gen.getMaxDurationField().setEnabled(!readOnly);
		splitPane1Gen.getDurationField().setEnabled(!readOnly);
		splitPane1Gen.getDescriptionArea().setEnabled(!readOnly);
		splitPane1Gen.getDependenciesComboBox().setEnabled(!readOnly);
		splitPane1Gen.getDependencyScrollPane().setEnabled(!readOnly);
		splitPane1Gen.getAddDependencyButton().setEnabled(!readOnly);
		splitPane1Gen.getRemoveDependencyButton().setEnabled(!readOnly);
	}
	
	private boolean isDirty()
	{
		//if this is a new activity, it is dirty
		if (activityId == -1) return true;
		
		//if the view was just opened, there is no activity to be dirty
		if (activityId == 0) return false;
		
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
				|| !a.getDescription().equals(splitPane1Gen.getDescriptionArea().getText()));
	}
}
