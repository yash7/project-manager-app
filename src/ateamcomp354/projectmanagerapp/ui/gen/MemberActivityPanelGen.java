package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class MemberActivityPanelGen extends JPanel {
	private JButton logoutButton;
	private JButton backButton;
	private JScrollPane activityScrollPane;

	/**
	 * Create the panel.
	 */
	public MemberActivityPanelGen() {
		setLayout(null);
		
		logoutButton = new JButton("Logout");
		logoutButton.setBounds(26, 6, 117, 29);
		add(logoutButton);
		
		backButton = new JButton("< Back");
		backButton.setBounds(26, 47, 117, 29);
		add(backButton);
		
		activityScrollPane = new JScrollPane();
		activityScrollPane.setBounds(32, 108, 384, 275);
		add(activityScrollPane);
		
		JLabel lblAssignedActivities = new JLabel("Assigned Activities");
		lblAssignedActivities.setBounds(36, 88, 163, 16);
		add(lblAssignedActivities);

	}

	public JButton getLogoutButton() {
		return logoutButton;
	}
	public JButton getBackButton() {
		return backButton;
	}
	public JScrollPane getActivityScrollPane() {
		return activityScrollPane;
	}
}
