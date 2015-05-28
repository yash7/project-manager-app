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

public class SplitPane1Gen extends JPanel {
	private JButton logoutButton;
	private JLabel topLabel;
	private JScrollPane listScrollPane;
	private JButton addButton;
	private JButton deleteButton;
	private JButton currentButton;
	private JButton archivedButton;
	private JButton manageActivitiesButton;
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
		
		currentButton = new JButton("Current");
		currentButton.setBounds(6, 111, 75, 25);
		LeftPanel.add(currentButton);
		
		archivedButton = new JButton("Archived");
		archivedButton.setBounds(7, 142, 75, 25);
		LeftPanel.add(archivedButton);
		
		manageActivitiesButton = new JButton("Manage Activities");
		manageActivitiesButton.setBounds(124, 329, 148, 25);
		LeftPanel.add(manageActivitiesButton);

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
		dependenciesLabel.setBounds(12, 542, 89, 16);
		RightPanel.add(dependenciesLabel);
		
		dependenciesComboBox = new JComboBox();
		dependenciesComboBox.setBounds(12, 566, 100, 25);
		RightPanel.add(dependenciesComboBox);
		
		assigneesLabel = new JLabel("Assignees:");
		assigneesLabel.setBounds(140, 542, 89, 16);
		RightPanel.add(assigneesLabel);
		
		assigneesComboBox = new JComboBox();
		assigneesComboBox.setBounds(140, 566, 100, 25);
		RightPanel.add(assigneesComboBox);
		
		saveActivityButton = new JButton("Save Activity");
		saveActivityButton.setBounds(68, 637, 116, 25);
		RightPanel.add(saveActivityButton);
		
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
	public JButton getCurrentButton() {
		return currentButton;
	}
	public JButton getArchivedButton() {
		return archivedButton;
	}
	public JButton getManageActivitiesButton() {
		return manageActivitiesButton;
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
}
