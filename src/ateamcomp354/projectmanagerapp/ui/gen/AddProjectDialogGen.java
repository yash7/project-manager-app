package ateamcomp354.projectmanagerapp.ui.gen;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AddProjectDialogGen extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField projectNameField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddProjectDialogGen dialog = new AddProjectDialogGen();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddProjectDialogGen() {
		setTitle("New Project");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel nameLabel = new JLabel("Name:");
			nameLabel.setBounds(86, 13, 56, 16);
			contentPanel.add(nameLabel);
		}
		{
			projectNameField = new JTextField();
			projectNameField.setBounds(86, 42, 243, 22);
			contentPanel.add(projectNameField);
			projectNameField.setColumns(10);
		}
		{
			JLabel descriptionLabel = new JLabel("Description:");
			descriptionLabel.setBounds(86, 77, 80, 16);
			contentPanel.add(descriptionLabel);
		}
		{
			JTextArea descriptionArea = new JTextArea();
			descriptionArea.setLineWrap(true);
			JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
			descriptionScrollPane.setBounds(86, 106, 243, 90);
			contentPanel.add(descriptionScrollPane);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public JTextField getProjectNameField() {
		return projectNameField;
	}
}
