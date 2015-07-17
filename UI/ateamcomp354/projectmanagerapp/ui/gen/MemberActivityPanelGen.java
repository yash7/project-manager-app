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
	private JLabel lblStartDate;
	private JLabel lblEndDate;
	private JLabel startDate;
	private JLabel endDate;

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
		lblDescription.setBounds(455, 147, 74, 14);
		add(lblDescription);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(455, 513, 46, 14);
		add(lblStatus);
		
		JScrollPane descriptionScrollPane = new JScrollPane();
		descriptionScrollPane.setBounds(455, 172, 335, 130);
		add(descriptionScrollPane);
		
		descriptionTextArea = new JTextArea();
		descriptionScrollPane.setViewportView(descriptionTextArea);
		
		statusComboBox = new JComboBox();
		statusComboBox.setBounds(496, 510, 150, 20);
		add(statusComboBox);
		
		saveActivityButton = new JButton("Save Activity");
		saveActivityButton.setBounds(455, 551, 117, 23);
		add(saveActivityButton);
		
		JLabel lblActivityAssignees = new JLabel("Activity Assignees");
		lblActivityAssignees.setBounds(455, 313, 117, 14);
		add(lblActivityAssignees);
		
		assigneeScrollPane = new JScrollPane();
		assigneeScrollPane.setBounds(455, 338, 335, 164);
		add(assigneeScrollPane);
		
		lblStartDate = new JLabel("Start Date:");
		lblStartDate.setBounds(455, 122, 74, 14);
		add(lblStartDate);
		
		lblEndDate = new JLabel("End Date:");
		lblEndDate.setBounds(611, 122, 74, 14);
		add(lblEndDate);
		
		startDate = new JLabel("");
		startDate.setBounds(527, 122, 74, 14);
		add(startDate);
		
		endDate = new JLabel("");
		endDate.setBounds(679, 122, 74, 14);
		add(endDate);

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
	public JLabel getEndDate() {
		return endDate;
	}
	public JLabel getStartDate() {
		return startDate;
	}
}
