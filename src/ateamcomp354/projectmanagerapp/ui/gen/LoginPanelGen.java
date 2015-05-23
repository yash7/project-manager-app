package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class LoginPanelGen extends JPanel {
	private JTextField usernameField;
	private JButton oKButton;
	private JButton cancelButton;
	private JPasswordField passwordField;

	/**
	 * Create the panel.
	 */
	public LoginPanelGen() {
		setLayout(null);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(140, 70, 70, 16);
		add(usernameLabel);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(140, 135, 70, 16);
		add(passwordLabel);
		
		usernameField = new JTextField();
		usernameField.setBounds(140, 100, 116, 22);
		add(usernameField);
		usernameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(140, 165, 116, 22);
		add(passwordField);
		
		oKButton = new JButton("OK");
		oKButton.setBounds(140, 206, 90, 25);
		add(oKButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(250, 206, 90, 25);
		add(cancelButton);

	}
	public JTextField getUsernameField() {
		return usernameField;
	}
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	public JButton getOKButton() {
		return oKButton;
	}
	public JButton getCancelButton() {
		return cancelButton;
	}
}
