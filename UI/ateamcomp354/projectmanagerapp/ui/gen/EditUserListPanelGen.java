package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class EditUserListPanelGen extends JPanel {
	private JScrollPane userScrollPane;
	private JButton editButton;
	private JButton cancelButton;

	/**
	 * Create the panel.
	 */
	public EditUserListPanelGen() {
		setLayout(null);
		
		userScrollPane = new JScrollPane();
		userScrollPane.setBounds(30, 50, 376, 238);
		add(userScrollPane);
		
		editButton = new JButton("Edit");
		editButton.setBounds(30, 310, 117, 29);
		add(editButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(289, 310, 117, 29);
		add(cancelButton);

	}
	public JScrollPane getUserScrollPane() {
		return userScrollPane;
	}
	public JButton getEditButton() {
		return editButton;
	}
	public JButton getCancelButton() {
		return cancelButton;
	}
}
