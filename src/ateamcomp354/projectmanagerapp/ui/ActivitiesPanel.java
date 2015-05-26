package ateamcomp354.projectmanagerapp.ui;

import java.util.List;

import javax.swing.*;

import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.ui.gen.SplitPane1Gen;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ActivitiesPanel {

	private final ApplicationContext appCtx;
	private ActivityService activityService;

	private SplitPane1Gen splitPane1Gen;
	private List<Activity> activities;
	private Project project;
	
	private int projectId = 1;
	private int activityId = 0;

	public ActivitiesPanel( ApplicationContext appCtx ) {

		this.appCtx = appCtx;
		splitPane1Gen = new SplitPane1Gen();

		splitPane1Gen.getStatusComboBox().addItem("Open");
		splitPane1Gen.getStatusComboBox().addItem("In Progress");
		splitPane1Gen.getStatusComboBox().addItem("Resolved");
		
		//occurs whenever the view is opened. projectId should be set from the project view
		splitPane1Gen.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				activityId = 0;
				clear();
				activityService = appCtx.getActivityService(projectId);
				activities = activityService.getActivities();
				project = activityService.getProject();
				
				splitPane1Gen.getTopLabel().setText(project.getProjectName());
				JLabel projectLabel = new JLabel(project.getProjectName()); 
				splitPane1Gen.getListScrollPane().setColumnHeaderView(projectLabel);
				
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
				if (!clear()) return;
				activityId = -1;
				splitPane1Gen.getActivityNameField().setText("New Activity");
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
	
	private boolean clear()
	{
		int dialogResult = 1;
		if (isDirty())
		{
			int dialogButton = JOptionPane.YES_NO_CANCEL_OPTION;
			dialogResult = JOptionPane.showConfirmDialog(splitPane1Gen, "Would you like to save first?", "You forgot to save", dialogButton);	
		}
		if (dialogResult != 2)
		{
			if (dialogResult == 0) saveActivity();
			splitPane1Gen.getActivityNameField().setText("");
			splitPane1Gen.getStatusComboBox().setSelectedIndex(0);
			splitPane1Gen.getEarliestStartField().setText("0");
			splitPane1Gen.getLatestStartField().setText("0");
			splitPane1Gen.getEarliestFinishField().setText("0");
			splitPane1Gen.getLatestFinishField().setText("0");
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
		activity.setStatus(splitPane1Gen.getStatusComboBox().getSelectedIndex());
		activity.setEarliestStart(Integer.parseInt(splitPane1Gen.getEarliestStartField().getText()));
		activity.setLatestStart(Integer.parseInt(splitPane1Gen.getLatestStartField().getText()));
		activity.setEarliestFinish(Integer.parseInt(splitPane1Gen.getEarliestFinishField().getText()));
		activity.setLatestFinish(Integer.parseInt(splitPane1Gen.getLatestFinishField().getText()));
		activity.setMaxDuration(Integer.parseInt(splitPane1Gen.getMaxDurationField().getText()));
		activity.setDuration(Integer.parseInt(splitPane1Gen.getDurationField().getText()));
		activity.setDescription(splitPane1Gen.getDescriptionArea().getText());
		addOrUpdateActivity(activity);
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
		if (!clear()) return;
		Activity activity = activityService.getActivity(id);
		activityId = id;
		splitPane1Gen.getActivityNameField().setText(activity.getLabel());
		splitPane1Gen.getStatusComboBox().setSelectedIndex(activity.getStatus());
		splitPane1Gen.getEarliestStartField().setText(Integer.toString(activity.getEarliestStart()));
		splitPane1Gen.getLatestStartField().setText(Integer.toString(activity.getLatestStart()));
		splitPane1Gen.getEarliestFinishField().setText(Integer.toString(activity.getEarliestFinish()));
		splitPane1Gen.getLatestFinishField().setText(Integer.toString(activity.getLatestFinish()));
		splitPane1Gen.getMaxDurationField().setText(Integer.toString(activity.getMaxDuration()));
		splitPane1Gen.getDurationField().setText(Integer.toString(activity.getDuration()));
		splitPane1Gen.getDescriptionArea().setText(activity.getDescription());
		//TODO: dependencies
	}
	
	private void fillActivitiesList()
	{
		String activityNames[] = new String[activities.size()];

		for (int i = 0; i < activities.size(); i++)
		{
			activityNames[i] = activities.get(i).getLabel();
		}
		
		JList<String> activityList = new JList<String>(activityNames);
		splitPane1Gen.getListScrollPane().setViewportView(activityList);
		splitPane1Gen.getListScrollPane().validate();
		
		activityList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = activityList.locationToIndex(e.getPoint());

				if (index >= 0)
				{
					selectActivity(index + 1);
				}
			}
		});
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
				|| a.getEarliestStart() != Integer.parseInt(splitPane1Gen.getEarliestStartField().getText())
				|| a.getLatestStart() != Integer.parseInt(splitPane1Gen.getLatestStartField().getText())
				|| a.getEarliestFinish() != Integer.parseInt(splitPane1Gen.getEarliestStartField().getText())
				|| a.getLatestFinish() != Integer.parseInt(splitPane1Gen.getLatestFinishField().getText())
				|| a.getStatus() != splitPane1Gen.getStatusComboBox().getSelectedIndex()
				|| a.getMaxDuration() != Integer.parseInt(splitPane1Gen.getMaxDurationField().getText())
				|| a.getDuration() != Integer.parseInt(splitPane1Gen.getDurationField().getText())
				|| !a.getDescription().equals(splitPane1Gen.getDescriptionArea().getText()));
	}
}
