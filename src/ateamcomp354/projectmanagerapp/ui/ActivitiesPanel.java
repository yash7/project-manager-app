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

	public ActivitiesPanel( ApplicationContext appCtx ) {

		this.appCtx = appCtx;
		splitPane1Gen = new SplitPane1Gen();
		
		splitPane1Gen.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				activityService = appCtx.getActivityService(projectId);
				activities = activityService.getActivities();
				project = activityService.getProject();
				
				splitPane1Gen.getTopLabel().setText(project.getProjectName());
				JLabel projectLabel = new JLabel(project.getProjectName()); 
				splitPane1Gen.getListScrollPane().setColumnHeaderView(projectLabel);
				
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
						System.out.println(index);
						if (index >= 0)
						{
							Activity activity = activityService.getActivity(index + 1);
							splitPane1Gen.getActivityNameLabel().setText(activity.getLabel());
							// TODO:  combo box indexing  splitPane1Gen.getStatusComboBox().setSelectedIndex(0);
							splitPane1Gen.getEarliestStartField().setText(Integer.toString(activity.getEarliestStart()));
							splitPane1Gen.getLatestStartField().setText(Integer.toString(activity.getLatestStart()));
							splitPane1Gen.getEarliestFinishField().setText(Integer.toString(activity.getEarliestFinish()));
							splitPane1Gen.getLatestFinishField().setText(Integer.toString(activity.getLatestFinish()));
							splitPane1Gen.getMaxDurationField().setText(Integer.toString(activity.getMaxDuration()));
							splitPane1Gen.getDurationField().setText(Integer.toString(activity.getDuration()));
							splitPane1Gen.getDescriptionArea().setText(activity.getDescription());
							//TODO: dependencies and assignees
						}
					}
				});
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

}
