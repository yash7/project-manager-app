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

	private int appWidth = MainFrame.getAppWidth();
	private int appHeight = MainFrame.getAppHeight();
	
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
	private JButton addDepButton;
	private JButton delDepButton;
	private JScrollPane scrollPane;
	private JButton btnView;
	private JScrollPane assigneeScrollPane;
	private JButton delAssignButton;
	private JButton addAssignButton;
	private JButton backBtn;
	private JTextField plannedValueField;
	
	/**
	 * Create the panel.
	 */
	public SplitPane1Gen() {
		
		setSize(appWidth, appHeight);
		setLayout(null);
		
		splitPane = new JSplitPane();
		splitPane.setBounds(0, 0, appWidth, appHeight);
		splitPane.setDividerLocation(2*(appWidth/5));
		add(splitPane);
		splitPane.setEnabled(false);
		
		JPanel LeftPanel = new JPanel();
		
		JPanel RightPanel = new JPanel();
		

		splitPane.setLeftComponent(LeftPanel);
		LeftPanel.setLayout(null);
		
		logoutButton = new JButton("Logout");
		logoutButton.setBounds(12, 13, 90, 30);
		LeftPanel.add(logoutButton);
		
		topLabel = new JLabel("TopLabel");
		topLabel.setBackground(Color.BLACK);
		topLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		topLabel.setHorizontalAlignment(SwingConstants.LEFT);
		topLabel.setBounds(43, 94, 77, 25);
		LeftPanel.add(topLabel);
		
		listScrollPane = new JScrollPane();
		listScrollPane.setBounds(45, 132, 256, 197);
		LeftPanel.add(listScrollPane);
		
		addButton = new JButton("Add");
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addButton.setBounds(231, 91, 70, 30);
		LeftPanel.add(addButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		deleteButton.setBounds(149, 91, 70, 30);
		LeftPanel.add(deleteButton);
		
		btnManage = new JButton("Manage Activity");
		btnManage.setBounds(153, 342, 148, 30);
		LeftPanel.add(btnManage);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(43, 385, 256, 197);
		LeftPanel.add(scrollPane);
		
		btnView = new JButton("View Activity");
		btnView.setBounds(149, 617, 148, 30);
		LeftPanel.add(btnView);
		
		backBtn = new JButton("< Back");
		backBtn.setBounds(12, 49, 90, 30);
		LeftPanel.add(backBtn);

		splitPane.setRightComponent(RightPanel);
		RightPanel.setLayout(null);
		
		activityNameLabel = new JLabel("Name:");
		activityNameLabel.setBounds(12, 51, 39, 16);
		RightPanel.add(activityNameLabel);
		
		statusLabel = new JLabel("Status:");
		statusLabel.setBounds(12, 79, 56, 16);
		RightPanel.add(statusLabel);
		
		statusComboBox = new JComboBox();
		statusComboBox.setBounds(147, 76, 116, 25);
		RightPanel.add(statusComboBox);
		
		earliestStartLabel = new JLabel("Earliest Start:");
		earliestStartLabel.setBounds(12, 134, 89, 16);
		RightPanel.add(earliestStartLabel);
		
		latestStartLabel = new JLabel("Latest Start:");
		latestStartLabel.setBounds(12, 163, 89, 16);
		RightPanel.add(latestStartLabel);
		
		earliestFinishLabel = new JLabel("Earliest Finish:");
		earliestFinishLabel.setBounds(12, 192, 116, 16);
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
		earliestStartField.setBounds(147, 134, 116, 22);
		RightPanel.add(earliestStartField);
		earliestStartField.setColumns(10);
		
		latestStartField = new JTextField();
		latestStartField.setColumns(10);
		latestStartField.setBounds(147, 163, 116, 22);
		RightPanel.add(latestStartField);
		
		earliestFinishField = new JTextField();
		earliestFinishField.setColumns(10);
		earliestFinishField.setBounds(147, 192, 116, 22);
		RightPanel.add(earliestFinishField);
		
		latestFinishField = new JTextField();
		latestFinishField.setColumns(10);
		latestFinishField.setBounds(147, 221, 116, 22);
		RightPanel.add(latestFinishField);
		
		maxDurationField = new JTextField();
		maxDurationField.setColumns(10);
		maxDurationField.setBounds(147, 250, 116, 22);
		RightPanel.add(maxDurationField);
		
		durationField = new JTextField();
		durationField.setColumns(10);
		durationField.setBounds(147, 276, 116, 22);
		RightPanel.add(durationField);
		
		descriptionArea = new JTextArea();
		descriptionArea.setLineWrap(true);
		
		descriptionScrollPane = new JScrollPane(descriptionArea);
		descriptionScrollPane.setBounds(287, 80, 207, 178);
		RightPanel.add(descriptionScrollPane);
		
		descriptionLabel = new JLabel("Description:");
		descriptionLabel.setBounds(287, 51, 89, 16);
		RightPanel.add(descriptionLabel);
		
		dependenciesLabel = new JLabel("Dependencies:");
		dependenciesLabel.setBounds(12, 328, 89, 16);
		RightPanel.add(dependenciesLabel);
		
		dependenciesComboBox = new JComboBox();
		dependenciesComboBox.setBounds(12, 493, 116, 25);
		RightPanel.add(dependenciesComboBox);
		
		assigneesLabel = new JLabel("Assignees:");
		assigneesLabel.setBounds(147, 328, 89, 16);
		RightPanel.add(assigneesLabel);
		
		assigneesComboBox = new JComboBox();
		assigneesComboBox.setBounds(147, 493, 116, 25);
		RightPanel.add(assigneesComboBox);
		
		saveActivityButton = new JButton("Save Activity");
		saveActivityButton.setBounds(86, 617, 116, 30);
		RightPanel.add(saveActivityButton);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(147, 48, 116, 22);
		RightPanel.add(textField);
		
		addDepButton = new JButton("Add");
		addDepButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addDepButton.setBounds(31, 531, 70, 30);
		RightPanel.add(addDepButton);
		
		delDepButton = new JButton("Delete");
		delDepButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		delDepButton.setBounds(31, 569, 70, 30);
		RightPanel.add(delDepButton);
		
		addAssignButton = new JButton("Add");
		addAssignButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addAssignButton.setBounds(166, 531, 70, 30);
		RightPanel.add(addAssignButton);
		
		delAssignButton = new JButton("Delete");
		delAssignButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		delAssignButton.setBounds(166, 569, 70, 30);
		RightPanel.add(delAssignButton);
		
		dependencyScrollPane = new JScrollPane();
		dependencyScrollPane.setBounds(12, 357, 116, 129);
		RightPanel.add(dependencyScrollPane);
		
		assigneeScrollPane = new JScrollPane();
		assigneeScrollPane.setBounds(147, 357, 116, 129);
		RightPanel.add(assigneeScrollPane);
		
		JLabel lblPlannedValue = new JLabel("Planned Value:");
		lblPlannedValue.setBounds(12, 107, 101, 16);
		RightPanel.add(lblPlannedValue);
		
		plannedValueField = new JTextField();
		plannedValueField.setBounds(147, 101, 116, 28);
		RightPanel.add(plannedValueField);
		plannedValueField.setColumns(10);
		
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
		return addDepButton;
	}
	public JButton getRemoveDependencyButton() {
		return delDepButton;
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
		return delAssignButton;
	}
	public JButton getAddAssigneeButton() {
		return addAssignButton;
	}
	public JButton getBackBtn() {
		return backBtn;
	}
	public JTextField getPlannedValueField() {
		return plannedValueField;
	}
}
