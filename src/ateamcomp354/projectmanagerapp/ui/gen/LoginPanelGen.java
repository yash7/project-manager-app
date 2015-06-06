package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LoginPanelGen extends JPanel {
	private JTextField usernameField;
	private JButton oKButton;
	private JPasswordField passwordField;
	private JLabel validityLabel;
	private JPanel panel;
	private Component verticalGlue;
	private Component verticalGlue_1;

	/**
	 * Create the panel.
	 */
	public LoginPanelGen() {
		setLayout(new BorderLayout(0, 0));
		
		verticalGlue = Box.createVerticalGlue();
		add(verticalGlue, BorderLayout.NORTH);
		
		verticalGlue_1 = Box.createVerticalGlue();
		add(verticalGlue_1, BorderLayout.SOUTH);
		
		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{300};
		gbl_panel.rowHeights = new int[]{23, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);
		
		JLabel usernameLabel = new JLabel("Username:");
		GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
		gbc_usernameLabel.anchor = GridBagConstraints.WEST;
		gbc_usernameLabel.insets = new Insets(0, 0, 10, 0);
		gbc_usernameLabel.gridx = 0;
		gbc_usernameLabel.gridy = 1;
		panel.add(usernameLabel, gbc_usernameLabel);
		
		JLabel passwordLabel = new JLabel("Password:");
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.insets = new Insets(0, 0, 10, 0);
		gbc_passwordLabel.anchor = GridBagConstraints.WEST;
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 3;
		panel.add(passwordLabel, gbc_passwordLabel);
		
		usernameField = new JTextField();
		GridBagConstraints gbc_usernameField = new GridBagConstraints();
		gbc_usernameField.ipady = 5;
		gbc_usernameField.ipadx = 5;
		gbc_usernameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameField.anchor = GridBagConstraints.WEST;
		gbc_usernameField.insets = new Insets(0, 0, 20, 0);
		gbc_usernameField.gridx = 0;
		gbc_usernameField.gridy = 2;
		panel.add(usernameField, gbc_usernameField);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.ipady = 5;
		gbc_passwordField.ipadx = 5;
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.anchor = GridBagConstraints.WEST;
		gbc_passwordField.insets = new Insets(0, 0, 20, 0);
		gbc_passwordField.gridx = 0;
		gbc_passwordField.gridy = 4;
		panel.add(passwordField, gbc_passwordField);
		
		oKButton = new JButton("OK");
		GridBagConstraints gbc_oKButton = new GridBagConstraints();
		gbc_oKButton.insets = new Insets(0, 0, 0, 5);
		gbc_oKButton.gridx = 0;
		gbc_oKButton.gridy = 5;
		panel.add(oKButton, gbc_oKButton);
		
		validityLabel = new JLabel("");
		GridBagConstraints gbc_validityLabel = new GridBagConstraints();
		gbc_validityLabel.insets = new Insets(0, 0, 20, 0);
		gbc_validityLabel.gridx = 0;
		gbc_validityLabel.gridy = 0;
		panel.add(validityLabel, gbc_validityLabel);
		validityLabel.setForeground(Color.RED);
		validityLabel.setFont(new Font("Tahoma", Font.BOLD, 13));

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
