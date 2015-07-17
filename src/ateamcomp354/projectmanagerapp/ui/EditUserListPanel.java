package ateamcomp354.projectmanagerapp.ui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.UserService;
import ateamcomp354.projectmanagerapp.ui.gen.EditUserListPanelGen;
import ateamcomp354.projectmanagerapp.ui.util.FrameSaver;

public class EditUserListPanel {
	
	private ApplicationContext appCtx;
	private SwapInterface swap;
	
	private int selectedUserId;
	private int[] userIndexes;
	
	private EditUserListPanelGen editUserListPanel;
	
	private UserService userService;
	
	public EditUserListPanel (ApplicationContext appCtx, SwapInterface swap) {
		
		this.appCtx = appCtx;
		this.swap = swap;
		
		userService = appCtx.getUserService();
		
		editUserListPanel = new EditUserListPanelGen();
		
		editUserListPanel.getCancelButton().addActionListener( __ -> this.swap.frameSwitch());
		
		editUserListPanel.getEditButton().addActionListener(__ -> editUser());
		
		showMembers();
	}
	
	public JComponent getComponent() {
		return editUserListPanel;
	}
	
	private void showMembers() {
		
		List<Users> users = userService.getUsers();
		
		selectedUserId = 0;
		String userNames[] = new String[users.size()];
		userIndexes = new int[users.size()];
		
		for (int i = 0; i < users.size(); i++)
		{
			userNames[i] = users.get(i).getFirstName() + " " + users.get(i).getLastName();
			userIndexes[i] = users.get(i).getId();
		}
		
		JList<String> userList = new JList<String>(userNames);
		editUserListPanel.getUserScrollPane().setViewportView(userList);
		editUserListPanel.getUserScrollPane().validate();
		
		userList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if (userList.locationToIndex(e.getPoint()) == -1) return;
				selectedUserId = userIndexes[userList.locationToIndex(e.getPoint())];
			}
		});
	}
	
	private void editUser() {
		Component[] comp = editUserListPanel.getUserScrollPane().getViewport().getComponents();
		if(comp.length > 0) {
			if(((JList<String>) comp[0]).getSelectedIndex() != -1) {
				FrameSaver editUserFrame = new FrameSaver();
				
				editUserFrame.setFirstID(selectedUserId);
				editUserFrame.setFrameName("EDIT_USER_PANEL");
				swap.saveFrame(editUserFrame); // Saves the next frame
				swap.frameSwitch();
				
			}
		}
	}
}

