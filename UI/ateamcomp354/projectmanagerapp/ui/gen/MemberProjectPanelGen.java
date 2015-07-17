package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;

import ateamcomp354.projectmanagerapp.ui.MainFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class MemberProjectPanelGen extends JPanel {

	private JButton logoutButton;
	private JScrollPane projectScrollPane;
	private JButton viewActivitiesButton;
	
	/**
	 * Create the panel.
	 */
	public MemberProjectPanelGen() {
		setLayout(null);
		
		logoutButton = new JButton("Logout");
		logoutButton.setBounds(10, 11, 89, 33);
		add(logoutButton);
		
		projectScrollPane = new JScrollPane();
		projectScrollPane.setBounds(20, 86, 661, 284);
		add(projectScrollPane);
		
		JLabel lblAssignedProjects = new JLabel("Assigned Projects");
		lblAssignedProjects.setBounds(20, 66, 120, 14);
		add(lblAssignedProjects);
		
		viewActivitiesButton = new JButton("View Activities");
		viewActivitiesButton.setBounds(20, 391, 120, 33);
		add(viewActivitiesButton);
	}

	public JButton getLogoutButton() {
		return logoutButton;
	}
	public JScrollPane getProjectScrollPane() {
		return projectScrollPane;
	}
	public JButton getViewActivitiesButton() {
		return viewActivitiesButton;
	}
}
