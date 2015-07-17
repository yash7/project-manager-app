package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

import ateamcomp354.projectmanagerapp.ui.MainFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;

public class MemberActivityPanelGen extends JPanel {
	private JButton logoutButton;
	private JButton backButton;
	private JScrollPane activityScrollPane;
	
	private int appWidth = MainFrame.getAppWidth();
	private int appHeight = MainFrame.getAppHeight();
	private JTextField nameTextField;
	private JTextArea descriptionTextArea;
	private JScrollPane assigneeScrollPane;
	private JComboBox statusComboBox;
	private JButton saveActivityButton;

	/**
	 * Create the panel.
	 */
	public MemberActivityPanelGen() {
		
		setSize(appWidth,appHeight);
		setLayout(null);
		
		logoutButton = new JButton("Logout");
		logoutButton.setBounds(26, 6, 117, 29);
		add(logoutButton);
		
		backButton = new JButton("< Back");
		backButton.setBounds(26, 47, 117, 29);
		add(backButton);
		
		activityScrollPane = new JScrollPane();
		activityScrollPane.setBounds(32, 108, 340, 466);
		add(activityScrollPane);
		
		JLabel lblAssignedActivities = new JLabel("Assigned Activities");
		lblAssignedActivities.setBounds(36, 88, 163, 16);
		add(lblAssignedActivities);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(455, 89, 46, 14);
		add(lblName);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(496, 86, 294, 20);
		add(nameTextField);
		nameTextField.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(455, 125, 74, 14);
		add(lblDescription);
		
		JScrollPane descriptionScrollPane = new JScrollPane();
		descriptionScrollPane.setBounds(455, 150, 335, 130);
		add(descriptionScrollPane);
		
		descriptionTextArea = new JTextArea();
		descriptionScrollPane.setViewportView(descriptionTextArea);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(455, 513, 46, 14);
		add(lblStatus);
		
		statusComboBox = new JComboBox();
		statusComboBox.setBounds(496, 510, 150, 20);
		add(statusComboBox);
		
		saveActivityButton = new JButton("Save Activity");
		saveActivityButton.setBounds(455, 551, 117, 23);
		add(saveActivityButton);
		
		JLabel lblActivityAssignees = new JLabel("Activity Assignees");
		lblActivityAssignees.setBounds(455, 302, 87, 14);
		add(lblActivityAssignees);
		
		assigneeScrollPane = new JScrollPane();
		assigneeScrollPane.setBounds(455, 327, 335, 164);
		add(assigneeScrollPane);

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
	public JTextField getNameTextField() {
		return nameTextField;
	}
	public JTextArea getDescriptionTextArea() {
		return descriptionTextArea;
	}
	public JScrollPane getAssigneeScrollPane() {
		return assigneeScrollPane;
	}
	public JComboBox getStatusComboBox() {
		return statusComboBox;
	}
	public JButton getSaveActivityButton() {
		return saveActivityButton;
	}
}
