package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import java.awt.GridLayout;

import javax.swing.SwingConstants;

import java.awt.geom.Dimension2D;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BoxLayout;

import java.awt.Component;

import javax.swing.JButton;

import java.awt.Font;

import javax.swing.JScrollPane;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import ateamcomp354.projectmanagerapp.ui.MainFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SplitPane1Gen extends JPanel {
	private JButton logoutButton;
	private JLabel topLabel;
	private JScrollPane listScrollPane;
	private JButton addButton;
	private JButton deleteButton;
	private JButton btnManage;
	private JLabel activityNameLabel;

	private int appWidth = 800;
	private int appHeight = 800;
	
	private JLabel statusLabel;
	private JComboBox statusComboBox;
	private JLabel earliestStartLabel;
	private JLabel latestStartLabel;
	private JLabel earliestFinishLabel;
	private JLabel latestFinishLabel;
	private JLabel maxDurationLabel;
	private JLabel durationLabel;
	private JTextField earliestStartField;
	private JTextField latestStartField;
	private JTextField earliestFinishField;
	private JTextField latestFinishField;
	private JTextField maxDurationField;
	private JTextField durationField;
	private JScrollPane descriptionScrollPane;
	private JTextArea descriptionArea;
	private JLabel descriptionLabel;
	private JLabel dependenciesLabel;
	private JComboBox dependenciesComboBox;
	private JLabel assigneesLabel;
	private JComboBox assigneesComboBox;
	private JButton saveActivityButton;
	private JSplitPane splitPane;
	private JTextField textField;
	private JScrollPane dependencyScrollPane;
	private JButton button;
	private JButton button_1;
	private JScrollPane scrollPane;
	private JButton btnView;
	private JScrollPane assigneeScrollPane;
	private JButton button_3;
	private JButton button_2;
	private JButton backBtn;
	
	/**
	 * Create the panel.
	 */
	public SplitPane1Gen() {
		
		setSize(MainFrame.getAppWidth(), MainFrame.getAppHeight());
		setLayout(null);
		
		splitPane = new JSplitPane();
		splitPane.setBounds(0, 0, appWidth, appHeight);
		splitPane.setDividerLocation(appWidth/2);
		add(splitPane);
		
		JPanel LeftPanel = new JPanel();
		
		JPanel RightPanel = new JPanel();
		

		splitPane.setLeftComponent(LeftPanel);
		LeftPanel.setLayout(null);
		
		logoutButton = new JButton("Logout");
		logoutButton.setBounds(12, 13, 90, 25);
		LeftPanel.add(logoutButton);
		
		topLabel = new JLabel("TopLabel");
		topLabel.setBackground(Color.BLACK);
		topLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		topLabel.setHorizontalAlignment(SwingConstants.LEFT);
		topLabel.setBounds(86, 73, 77, 25);
		LeftPanel.add(topLabel);
		
		listScrollPane = new JScrollPane();
		listScrollPane.setBounds(88, 111, 176, 197);
		LeftPanel.add(listScrollPane);
		
		addButton = new JButton("+");
		addButton.setBounds(210, 66, 54, 40);
		LeftPanel.add(addButton);
		
		deleteButton = new JButton("X");
		deleteButton.setBounds(150, 66, 54, 40);
		LeftPanel.add(deleteButton);
		
		btnManage = new JButton("Manage Activity");
		btnManage.setBounds(120, 319, 148, 25);
		LeftPanel.add(btnManage);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(86, 352, 182, 197);
		LeftPanel.add(scrollPane);
		
		btnView = new JButton("View Activity");
		btnView.setBounds(120, 560, 148, 25);
		LeftPanel.add(btnView);
		
		backBtn = new JButton("< Back");
		backBtn.setBounds(12, 49, 90, 25);
		LeftPanel.add(backBtn);

		splitPane.setRightComponent(RightPanel);
		RightPanel.setLayout(null);
		
		activityNameLabel = new JLabel("Name:");
		activityNameLabel.setBounds(12, 51, 39, 16);
		RightPanel.add(activityNameLabel);
		
		statusLabel = new JLabel("Status:");
		statusLabel.setBounds(12, 85, 56, 16);
		RightPanel.add(statusLabel);
		
		statusComboBox = new JComboBox();
		statusComboBox.setBounds(68, 81, 100, 25);
		RightPanel.add(statusComboBox);
		
		earliestStartLabel = new JLabel("Earliest Start:");
		earliestStartLabel.setBounds(12, 134, 89, 16);
		RightPanel.add(earliestStartLabel);
		
		latestStartLabel = new JLabel("Latest Start:");
		latestStartLabel.setBounds(12, 163, 89, 16);
		RightPanel.add(latestStartLabel);
		
		earliestFinishLabel = new JLabel("Earliest Finish:");
		earliestFinishLabel.setBounds(12, 192, 89, 16);
		RightPanel.add(earliestFinishLabel);
		
		latestFinishLabel = new JLabel("Latest Finish:");
		latestFinishLabel.setBounds(12, 221, 89, 16);
		RightPanel.add(latestFinishLabel);
		
		maxDurationLabel = new JLabel("Max Duration:");
		maxDurationLabel.setBounds(12, 250, 89, 16);
		RightPanel.add(maxDurationLabel);
		
		durationLabel = new JLabel("Duration:");
		durationLabel.setBounds(12, 279, 89, 16);
		RightPanel.add(durationLabel);
		
		earliestStartField = new JTextField();
		earliestStartField.setBounds(103, 131, 116, 22);
		RightPanel.add(earliestStartField);
		earliestStartField.setColumns(10);
		
		latestStartField = new JTextField();
		latestStartField.setColumns(10);
		latestStartField.setBounds(103, 160, 116, 22);
		RightPanel.add(latestStartField);
		
		earliestFinishField = new JTextField();
		earliestFinishField.setColumns(10);
		earliestFinishField.setBounds(103, 189, 116, 22);
		RightPanel.add(earliestFinishField);
		
		latestFinishField = new JTextField();
		latestFinishField.setColumns(10);
		latestFinishField.setBounds(103, 218, 116, 22);
		RightPanel.add(latestFinishField);
		
		maxDurationField = new JTextField();
		maxDurationField.setColumns(10);
		maxDurationField.setBounds(103, 247, 116, 22);
		RightPanel.add(maxDurationField);
		
		durationField = new JTextField();
		durationField.setColumns(10);
		durationField.setBounds(103, 276, 116, 22);
		RightPanel.add(durationField);
		
		descriptionArea = new JTextArea();
		descriptionArea.setLineWrap(true);
		
		descriptionScrollPane = new JScrollPane(descriptionArea);
		descriptionScrollPane.setBounds(12, 340, 207, 178);
		RightPanel.add(descriptionScrollPane);
		
		descriptionLabel = new JLabel("Description:");
		descriptionLabel.setBounds(12, 319, 89, 16);
		RightPanel.add(descriptionLabel);
		
		dependenciesLabel = new JLabel("Dependencies:");
		dependenciesLabel.setBounds(12, 529, 89, 16);
		RightPanel.add(dependenciesLabel);
		
		dependenciesComboBox = new JComboBox();
		dependenciesComboBox.setBounds(12, 681, 116, 25);
		RightPanel.add(dependenciesComboBox);
		
		assigneesLabel = new JLabel("Assignees:");
		assigneesLabel.setBounds(147, 529, 89, 16);
		RightPanel.add(assigneesLabel);
		
		assigneesComboBox = new JComboBox();
		assigneesComboBox.setBounds(147, 681, 116, 25);
		RightPanel.add(assigneesComboBox);
		
		saveActivityButton = new JButton("Save Activity");
		saveActivityButton.setBounds(151, 762, 116, 25);
		RightPanel.add(saveActivityButton);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(103, 49, 116, 22);
		RightPanel.add(textField);
		
		button = new JButton("+");
		button.setBounds(68, 717, 60, 32);
		RightPanel.add(button);
		
		button_1 = new JButton("X");
		button_1.setBounds(12, 717, 56, 32);
		RightPanel.add(button_1);
		
		button_2 = new JButton("+");
		button_2.setBounds(203, 717, 60, 32);
		RightPanel.add(button_2);
		
		button_3 = new JButton("X");
		button_3.setBounds(147, 717, 56, 32);
		RightPanel.add(button_3);
		
		dependencyScrollPane = new JScrollPane();
		dependencyScrollPane.setBounds(12, 545, 116, 129);
		RightPanel.add(dependencyScrollPane);
		
		assigneeScrollPane = new JScrollPane();
		assigneeScrollPane.setBounds(147, 545, 116, 129);
		RightPanel.add(assigneeScrollPane);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(103, 49, 116, 22);
		RightPanel.add(textField);
		
	}

	// LeftPanel
	public JButton getLogoutButton() {
		return logoutButton;
	}
	public JLabel getTopLabel() {
		return topLabel;
	}
	public JScrollPane getListScrollPane() {
		return listScrollPane;
	}
	public JButton getAddButton() {
		return addButton;
	}
	public JButton getDeleteButton() {
		return deleteButton;
	}

	public JButton getBtnManage() {
		return btnManage;
	}
	
	// RightPanel
	public JComboBox getStatusComboBox() {
		return statusComboBox;
	}
	public JTextField getEarliestStartField() {
		return earliestStartField;
	}
	public JTextField getLatestFinishField() {
		return latestFinishField;
	}
	public JTextField getDurationField() {
		return durationField;
	}
	public JTextField getEarliestFinishField() {
		return earliestFinishField;
	}
	public JTextField getLatestStartField() {
		return latestStartField;
	}
	public JTextField getMaxDurationField() {
		return maxDurationField;
	}
	public JTextArea getDescriptionArea() {
		return descriptionArea;
	}
	public JComboBox getDependenciesComboBox() {
		return dependenciesComboBox;
	}
	public JComboBox getAssigneesComboBox() {
		return assigneesComboBox;
	}
	public JButton getSaveActivityButton() {
		return saveActivityButton;
	}
	public JSplitPane getSplitPane() {
		return splitPane;
	}
	public JTextField getActivityNameField() {
		return textField;
	}
	public JScrollPane getDependencyScrollPane() {
		return dependencyScrollPane;
	}
	public JButton getAddDependencyButton() {
		return button;
	}
	public JButton getRemoveDependencyButton() {
		return button_1;
	}
	public JScrollPane getCompletedScrollPane() {
		return scrollPane;
	}
	public JButton getBtnView() {
		return btnView;
	}
	public JScrollPane getAssigneeScrollPane() {
		return assigneeScrollPane;
	}
	public JButton getRemoveAssigneeButton() {
		return button_3;
	}
	public JButton getAddAssigneeButton() {
		return button_2;
	}
	public JButton getBackBtn() {
		return backBtn;
	}
}
