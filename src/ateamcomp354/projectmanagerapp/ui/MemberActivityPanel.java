package ateamcomp354.projectmanagerapp.ui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectMemberService;
import ateamcomp354.projectmanagerapp.ui.gen.MemberActivityPanelGen;

public class MemberActivityPanel {
	private MemberActivityPanelGen memberActivityPanelGen;
	
	private ProjectMemberService projectMemberService;
	private SwapInterface swap;
	
	private List<Activity> assignedActivities;
	private JList<String> assignedActivitiesList;
	private int[] activityIndexes;
	
	private int userId;
	private int selectedActivityId;
	private int projectId;
	
	public MemberActivityPanel (ApplicationContext appCtx, SwapInterface swap)
	{
		this.swap = swap;
		
		memberActivityPanelGen = new MemberActivityPanelGen();
		
		memberActivityPanelGen.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				projectMemberService = appCtx.getProjectMemberService(userId);
				assignedActivities = projectMemberService.getAssignedActivities(projectId);
				
				//memberActivityPanelGen.getActivityScrollPane().setColumnHeaderView(new JLabel("Assigned Activities"));

				fillAssignedActivityList();
			}
		});
		
		memberActivityPanelGen.getLogoutButton().addActionListener( __ -> this.swap.showLoginView() );
		
		memberActivityPanelGen.getBackButton().addActionListener( __ -> this.swap.showMemberProjectsView(userId));
	}
	
	public JComponent getComponent()
	{
		return memberActivityPanelGen;
	}
	
	public void setProjectId(int id)
	{
		projectId = id;
	}
	
	public void setUserId(int id)
	{
		userId = id;
	}
	
	private void fillAssignedActivityList() {
		assignedActivities = projectMemberService.getAssignedActivities(projectId);
		
		selectedActivityId = 0;
		String activityNames[] = new String[assignedActivities.size()];
		activityIndexes = new int[assignedActivities.size()];
		
		for (int i = 0; i < assignedActivities.size(); i++)
		{
			activityNames[i] = assignedActivities.get(i).getLabel();
			activityIndexes[i] = assignedActivities.get(i).getId();
		}
		
		assignedActivitiesList = new JList<>(activityNames);
		
		memberActivityPanelGen.getActivityScrollPane().setViewportView(assignedActivitiesList);
		memberActivityPanelGen.getActivityScrollPane().validate();
	}
}
