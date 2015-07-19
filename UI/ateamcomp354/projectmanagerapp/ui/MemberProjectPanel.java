package ateamcomp354.projectmanagerapp.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectMemberService;
import ateamcomp354.projectmanagerapp.ui.gen.MemberProjectPanelGen;
import ateamcomp354.projectmanagerapp.ui.util.FrameSaver;

public class MemberProjectPanel {
	
	private MemberProjectPanelGen memberProjectPanelGen;
	private ProjectMemberService projectMemberService;
	private SwapInterface swap;
	
	private List<Project> assignedProjects;
	private JList<String> assignedProjectsList;
	private int[] projectIndexes;
	
	private int userId;
	private int selectedProjectId;
	
	public MemberProjectPanel (ApplicationContext appCtx , SwapInterface swap)
	{
		this.swap = swap;
		memberProjectPanelGen = new MemberProjectPanelGen();		
		
		//occurs whenever the view is opened. projectId should be set from the project view
		memberProjectPanelGen.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				projectMemberService = appCtx.getProjectMemberService(userId);
				assignedProjects = projectMemberService.getAssignedProjects();
				
				//memberProjectPanelGen.getProjectScrollPane().setColumnHeaderView(new JLabel("Assigned Projects"));

				fillAssignedProjectList();
			}
		});
		
		//deletes currently selected activity
		memberProjectPanelGen.getViewActivitiesButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Component[] comp = memberProjectPanelGen.getProjectScrollPane().getViewport().getComponents();
				if(comp.length > 0) {
					if(((JList<String>) comp[0]).getSelectedIndex() != -1) {
						FrameSaver memberActivityFrame = new FrameSaver();
						
						memberActivityFrame.setFirstID(selectedProjectId);
						memberActivityFrame.setSecondID(userId);
						memberActivityFrame.setFrameName("MEMBERACTIVITY_PANEL");
						swap.saveFrame(memberActivityFrame); // Saves the next frame
						swap.frameSwitch(); 
					}
				}
			}
		});
	}
	
	public JComponent getComponent()
	{
		return memberProjectPanelGen;
	}
	
	public void setUserId(int id)
	{
		userId = id;
	}
	
	private void fillAssignedProjectList() {
		assignedProjects = projectMemberService.getAssignedProjects();
		
		selectedProjectId = 0;
		String projectNames[] = new String[assignedProjects.size()];
		projectIndexes = new int[assignedProjects.size()];
		
		for (int i = 0; i < assignedProjects.size(); i++)
		{
			projectNames[i] = assignedProjects.get(i).getProjectName();
			projectIndexes[i] = assignedProjects.get(i).getId();
		}
		
		assignedProjectsList = new JList<>(projectNames);
		assignedProjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		memberProjectPanelGen.getProjectScrollPane().setViewportView(assignedProjectsList);
		memberProjectPanelGen.getProjectScrollPane().validate();
		
		assignedProjectsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if (assignedProjectsList.locationToIndex(e.getPoint()) == -1) return;
				selectedProjectId = projectIndexes[assignedProjectsList.locationToIndex(e.getPoint())];
			}
		});
	}
	
}