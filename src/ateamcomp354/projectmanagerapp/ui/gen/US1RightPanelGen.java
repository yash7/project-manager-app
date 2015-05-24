package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;

import ateamcomp354.projectmanagerapp.ui.MainFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class US1RightPanelGen extends JPanel {
	private JTextField projectNameField;
	private JTextArea descriptionArea;
	private JButton saveButton;

	/**
	 * Create the panel.
	 */
	public US1RightPanelGen() {
		
		setSize(MainFrame.getAppWidth()/2, MainFrame.getAppHeight());
		setLayout(null);
		
		JLabel newEditProjectLabel = new JLabel("New/Edit Project");
		newEditProjectLabel.setBounds(12, 13, 112, 16);
		add(newEditProjectLabel);
		
		JLabel projectNameLabel = new JLabel("Name:");
		projectNameLabel.setBounds(12, 73, 53, 16);
		add(projectNameLabel);
		
		projectNameField = new JTextField();
		projectNameField.setBounds(77, 70, 264, 22);
		add(projectNameField);
		projectNameField.setColumns(10);
		
		JLabel descriptionLabel = new JLabel("Description:");
		descriptionLabel.setBounds(12, 102, 68, 16);
		add(descriptionLabel);
		
		descriptionArea = new JTextArea();
		JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
		descriptionScrollPane.setBounds(12, 131, 329, 150);
		add(descriptionScrollPane);
		
		saveButton = new JButton("Save");
		saveButton.setBounds(244, 308, 97, 25);
		add(saveButton);
		
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
}
