package ateamcomp354.projectmanagerapp.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectMemberService;
import ateamcomp354.projectmanagerapp.ui.gen.MemberActivityPanelGen;
import ateamcomp354.projectmanagerapp.model.Status;

public class MemberActivityPanel {
	private MemberActivityPanelGen memberActivityPanelGen;
	
	private ProjectMemberService projectMemberService;
	private ActivityService activityService;
	private SwapInterface swap;
	
	private List<Activity> assignedActivities;
	private JList<String> assignedActivitiesList;
	private int[] activityIndexes;
	
	private DefaultListModel<String> assigneeListModel;
	
	private int userId;
	private int selectedActivityId;
	private int projectId;
	
	public MemberActivityPanel (ApplicationContext appCtx, SwapInterface swap)
	{
		this.swap = swap;
		
		memberActivityPanelGen = new MemberActivityPanelGen();
		assigneeListModel = new DefaultListModel();
		
		memberActivityPanelGen.getStatusComboBox().addItem("Open");
		memberActivityPanelGen.getStatusComboBox().addItem("In Progress");
		memberActivityPanelGen.getStatusComboBox().addItem("Resolved");
		
		memberActivityPanelGen.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				projectMemberService = appCtx.getProjectMemberService(userId);
				activityService = appCtx.getActivityService(projectId);
				assignedActivities = projectMemberService.getAssignedActivities(projectId);
				
				//memberActivityPanelGen.getActivityScrollPane().setColumnHeaderView(new JLabel("Assigned Activities"));

				fillAssignedActivityList();
			}
		});
		
		//Saving the activity from the edited fields
		memberActivityPanelGen.getSaveActivityButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Component[] comp = memberActivityPanelGen.getActivityScrollPane().getViewport().getComponents();
				if(comp.length > 0) {
					if(((JList<String>) comp[0]).getSelectedIndex() != -1) {
							saveActivity();
					}
				}
			}
		});
		
		memberActivityPanelGen.getLogoutButton().addActionListener( __ -> this.swap.showLoginView() );
		
		memberActivityPanelGen.getBackButton().addActionListener( new backActionListener());
	}
	
	class backActionListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			swap.removeSaveFrame();
			swap.frameSwitch();
			
		}
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
		
		assignedActivitiesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = assignedActivitiesList.locationToIndex(e.getPoint());

				if (index >= 0)
				{
					selectActivity(activityIndexes[index]);
				}
			}
		});
	}
	
	private void selectActivity(int id)
	{
		Activity activity = activityService.getActivity(id);
		selectedActivityId = id;
		memberActivityPanelGen.getNameTextField().setText(activity.getLabel());
		memberActivityPanelGen.getStatusComboBox().setSelectedIndex(activity.getStatus().ordinal());
		memberActivityPanelGen.getDescriptionTextArea().setText(activity.getDescription());
		showAssignees(id);
		
		if(activity.getStatus() == Status.RESOLVED) {
			greyOutActivity(false);
		}
		else {
			greyOutActivity(true);
		}
	}
	
	private void greyOutActivity(boolean enabled) {
		memberActivityPanelGen.getNameTextField().setEnabled(enabled);
		memberActivityPanelGen.getDescriptionTextArea().setEnabled(enabled);
	}

	private void saveActivity() {
		String errorString = "";
		Activity activity = activityService.getActivity(selectedActivityId);
		if(memberActivityPanelGen.getNameTextField().getText().trim().equals("")) {
			errorString += "Activity Must Have a Name\n";
		}
		else {
			activity.setLabel(memberActivityPanelGen.getNameTextField().getText());
		}
		activity.setProjectId(projectId);
		activity.setStatus(Status.values()[memberActivityPanelGen.getStatusComboBox().getSelectedIndex()]);
		activity.setDescription(memberActivityPanelGen.getDescriptionTextArea().getText());
		
		if(errorString.equals("")) {
			activityService.updateActivity(activity);
			if(activity.getStatus() == Status.RESOLVED) {
				greyOutActivity(false);
			}
			else {
				greyOutActivity(true);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, errorString);
		}
	}
	
	private void showAssignees(int activityId) {		
		
		assigneeListModel.clear();
		
		List<Users> assignees = projectMemberService.getOtherAssigneesForActivity(activityId);
		
		for (int i = 0; i < assignees.size(); i++)
		{
			assigneeListModel.addElement(assignees.get(i).getFirstName() + " " + assignees.get(i).getLastName());
		}
		
		JList<String> assigneeList = new JList<String>(assigneeListModel);
		memberActivityPanelGen.getAssigneeScrollPane().setViewportView(assigneeList);
		memberActivityPanelGen.getAssigneeScrollPane().validate();
	}
}
