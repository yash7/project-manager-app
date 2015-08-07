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

	private JLabel lblBudgetAtCompletion;
	private JLabel budgetAtCompletionLabel;
	private JLabel lblActualCostAtCompletion;
	private JLabel actualCostLabel;
	private JLabel projectMembersLabel;
	
	/**
	 * Create the panel.
	 */
	public US1RightPanelGen() {
		
		setSize(MainFrame.getAppWidth()/2, MainFrame.getAppHeight());
		setLayout(null);
		
		saveButton = new JButton("Save Project");
		saveButton.setBounds(200, 321, 141, 30);
		add(saveButton);
		
		newEditProjectLabel = new JLabel("New/Edit Project");
		newEditProjectLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		newEditProjectLabel.setBounds(12, 13, 173, 30);
		add(newEditProjectLabel);
		
		JLabel projectNameLabel = new JLabel("Name:");
		projectNameLabel.setBounds(12, 73, 53, 16);
		add(projectNameLabel);
		
		projectNameField = new JTextField();
		projectNameField.setBounds(101, 73, 240, 22);
		add(projectNameField);
		projectNameField.setColumns(10);
		JScrollPane descriptionScrollPane = new JScrollPane();
		descriptionScrollPane.setBounds(12, 160, 329, 150);
		add(descriptionScrollPane);
		
		descriptionArea = new JTextArea();
		descriptionScrollPane.setViewportView(descriptionArea);
		
		JLabel descriptionLabel = new JLabel("Description:");
		descriptionScrollPane.setColumnHeaderView(descriptionLabel);
		
		chckbxProjectIsCompleted = new JCheckBox("Project is completed");
		chckbxProjectIsCompleted.setBounds(12, 325, 173, 23);
		add(chckbxProjectIsCompleted);
		
		projectMembersScrollPane = new JScrollPane();
		projectMembersScrollPane.setBounds(12, 362, 329, 201);
		add(projectMembersScrollPane);
		
		projectMembersLabel = new JLabel("Project Members:");
		projectMembersScrollPane.setColumnHeaderView(projectMembersLabel);
		
		projectMembersComboBox = new JComboBox();
		projectMembersComboBox.setBounds(12, 575, 329, 22);
		add(projectMembersComboBox);
		
		addButton = new JButton("Add");
		addButton.setBounds(12, 609, 162, 29);
		add(addButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.setBounds(179, 609, 162, 29);
		add(deleteButton);

		lblBudgetAtCompletion = new JLabel("Budget at Completion:");
		lblBudgetAtCompletion.setBounds(12, 133, 162, 20);
		add(lblBudgetAtCompletion);
		
		budgetAtCompletionLabel = new JLabel("0");
		budgetAtCompletionLabel.setBounds(152, 133, 100, 20);
		add(budgetAtCompletionLabel);
		
		lblActualCostAtCompletion = new JLabel("Actual Cost to Date:");
		lblActualCostAtCompletion.setBounds(12, 106, 141, 20);
		add(lblActualCostAtCompletion);
		
		actualCostLabel = new JLabel("0");
		actualCostLabel.setBounds(152, 104, 100, 20);
		add(actualCostLabel);
		
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
	public JLabel getProjectMembersLabel() {
		return projectMembersLabel;
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
	public JLabel getBudgetAtCompletionLabel() {
		return budgetAtCompletionLabel;
	}
	public JLabel getActualCostField() {
		return actualCostLabel;
	}
}
