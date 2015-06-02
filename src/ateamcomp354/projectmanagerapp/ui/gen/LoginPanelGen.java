package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.Color;

public class LoginPanelGen extends JPanel {
	private JTextField usernameField;
	private JButton oKButton;
	private JButton cancelButton;
	private JPasswordField passwordField;
	private JLabel validityLabel;

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
		
		validityLabel = new JLabel("");
		validityLabel.setForeground(Color.RED);
		validityLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		validityLabel.setBounds(140, 244, 298, 16);
		add(validityLabel);

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
	public JLabel getValidityLabel() {
		return validityLabel;
	}
}
