package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;

import ateamcomp354.projectmanagerapp.ui.MainFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class US1RightPanelGen extends JPanel {
	private JTextField projectNameField;
	private JTextArea descriptionArea;
	private JButton saveButton;
	private JLabel newEditProjectLabel;
	private JCheckBox chckbxProjectIsCompleted;
	private JScrollPane projectMembersScrollPane;
	private JComboBox projectMembersComboBox;
	private JButton addButton;
	private JButton deleteButton;

	/**
	 * Create the panel.
	 */
	public US1RightPanelGen() {
		
		setSize(MainFrame.getAppWidth()/2, MainFrame.getAppHeight());
		setLayout(null);
		
		newEditProjectLabel = new JLabel("New/Edit Project");
		newEditProjectLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		newEditProjectLabel.setBounds(12, 13, 173, 30);
		add(newEditProjectLabel);
		
		JLabel projectNameLabel = new JLabel("Name:");
		projectNameLabel.setBounds(12, 73, 53, 16);
		add(projectNameLabel);
		
		projectNameField = new JTextField();
		projectNameField.setBounds(77, 70, 264, 22);
		add(projectNameField);
		projectNameField.setColumns(10);
		
		JLabel descriptionLabel = new JLabel("Description:");
		descriptionLabel.setBounds(12, 129, 112, 16);
		add(descriptionLabel);
		
		descriptionArea = new JTextArea();
		JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
		descriptionScrollPane.setBounds(12, 156, 329, 150);
		add(descriptionScrollPane);
		
		saveButton = new JButton("Save Project");
		saveButton.setBounds(200, 317, 141, 30);
		add(saveButton);
		
		chckbxProjectIsCompleted = new JCheckBox("Project is completed");
		chckbxProjectIsCompleted.setBounds(12, 99, 329, 23);
		add(chckbxProjectIsCompleted);
		
		JLabel projectMembersLabel = new JLabel("Project Members:");
		projectMembersLabel.setBounds(12, 390, 132, 16);
		add(projectMembersLabel);
		
		projectMembersScrollPane = new JScrollPane();
		projectMembersScrollPane.setBounds(12, 418, 329, 201);
		add(projectMembersScrollPane);
		
		projectMembersComboBox = new JComboBox();
		projectMembersComboBox.setBounds(12, 631, 329, 22);
		add(projectMembersComboBox);
		
		addButton = new JButton("Add");
		addButton.setBounds(12, 665, 117, 29);
		add(addButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.setBounds(141, 665, 117, 29);
		add(deleteButton);
		
	}
	public JTextField getProjectNameField() {
		return projectNameField;
	}
	public JTextArea getDescriptionArea() {
		return descriptionArea;
	}
	public JButton getSaveButton() {
		return saveButton;
	}
	public JLabel getTitleLabel() {
		return newEditProjectLabel;
	}
	public JCheckBox getCompletedCheckBox() {
		return chckbxProjectIsCompleted;
	}
	public JScrollPane getProjectMembersScrollPane() {
		return projectMembersScrollPane;
	}
	public JComboBox getProjectMembersComboBox() {
		return projectMembersComboBox;
	}
	public JButton getAddButton() {
		return addButton;
	}
	public JButton getDeleteButton() {
		return deleteButton;
	}
}
