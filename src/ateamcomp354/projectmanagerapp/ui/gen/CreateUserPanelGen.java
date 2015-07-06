package ateamcomp354.projectmanagerapp.ui.gen;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import ateamcomp354.projectmanagerapp.ui.MainFrame;

import javax.swing.DefaultComboBoxModel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSeparator;
import java.awt.Panel;

public class CreateUserPanelGen extends JPanel{

	
	private int appWidth = MainFrame.getAppWidth();
	private int appHeight = MainFrame.getAppHeight();


	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField usernameTextField;
	private JPasswordField passwordField;
	private JComboBox<String> managerRoleComboBox;
	
	
	JLabel errorFirstNameLabel;
	JLabel errorLastNameLabel;
	JLabel errorUsernameLabel;
	JLabel errorPasswordLabel;
	
	

	JButton saveButton;
	JButton cancelButton;

		
	public CreateUserPanelGen() {
		setForeground(new Color(255, 0, 0));
		setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		setSize(appWidth,appHeight);
		setLayout(null);
		
		JLabel newUserLabel = new JLabel("Add New User");
		newUserLabel.setBounds(47, 30, 206, 40);
		newUserLabel.setFont(new Font("Tahoma", Font.PLAIN, 33));
		add(newUserLabel);
		
		JLabel firstNameLablel = new JLabel("First Name");
		firstNameLablel.setBounds(80, 132, 93, 25);
		firstNameLablel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(firstNameLablel);
		
		firstNameTextField = new JTextField();
		firstNameTextField.setBounds(275, 132, 244, 26);
		firstNameTextField.setCaretColor(new Color(0, 0, 0));
		add(firstNameTextField);
		firstNameTextField.setColumns(10);
		
		JLabel lastNameLabel = new JLabel("Last Name");
		lastNameLabel.setBounds(80, 219, 90, 25);
		lastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lastNameLabel);
		
		lastNameTextField = new JTextField();
		lastNameTextField.setBounds(275, 219, 244, 26);
		lastNameTextField.setCaretColor(new Color(0, 0, 0));
		add(lastNameTextField);
		lastNameTextField.setColumns(10);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(80, 306, 85, 25);
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(usernameLabel);
		
		usernameTextField = new JTextField();
		usernameTextField.setBounds(275, 306, 244, 26);
		usernameTextField.setCaretColor(new Color(0, 0, 0));
		add(usernameTextField);
		usernameTextField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(80, 393, 80, 25);
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(passwordLabel);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(275, 393, 244, 26);
		passwordField.setCaretColor(new Color(0, 0, 0));
		add(passwordField);
		
		JLabel managerRoleLabel = new JLabel("Manager Role");
		managerRoleLabel.setBounds(80, 480, 119, 25);
		managerRoleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(managerRoleLabel);
		
		managerRoleComboBox = new JComboBox();
		managerRoleComboBox.setBounds(275, 481, 244, 26);
		managerRoleComboBox.setBackground(new Color(255, 255, 255));
		managerRoleComboBox.setModel(new DefaultComboBoxModel(new String[] {"Yes", "No"}));
		add(managerRoleComboBox);
		
		saveButton = new JButton("Save");
		saveButton.setBounds(78, 611, 153, 34);
		saveButton.setBackground(new Color(248, 248, 255));
		add(saveButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(373, 611, 146, 34);
		cancelButton.setBackground(new Color(248, 248, 255));
		add(cancelButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 86, 1000, 50);
		separator.setBackground(new Color(0, 0, 0));
		add(separator);
		
		errorFirstNameLabel = new JLabel("");
		errorFirstNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorFirstNameLabel.setForeground(new Color(255, 0, 0));
		errorFirstNameLabel.setBounds(555, 132, 430, 20);
		add(errorFirstNameLabel);
		
		errorLastNameLabel = new JLabel("");
		errorLastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorLastNameLabel.setForeground(new Color(255, 0, 0));
		errorLastNameLabel.setBounds(555, 219, 430, 20);
		add(errorLastNameLabel);
		
		errorUsernameLabel = new JLabel("");
		errorUsernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorUsernameLabel.setForeground(new Color(255, 0, 0));
		errorUsernameLabel.setBounds(555, 306, 430, 20);
		add(errorUsernameLabel);
		
		errorPasswordLabel = new JLabel("");
		errorPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorPasswordLabel.setForeground(new Color(255, 0, 0));
		errorPasswordLabel.setBounds(555, 393, 430, 20);
		add(errorPasswordLabel);
	}
	
	
	public JLabel getErrorUsernameLabel() {
		return errorUsernameLabel;
	}


	public JButton getSaveButton() {
		return saveButton;
	}


	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(JPasswordField passwordField) {
		this.passwordField = passwordField;
	}
	
	public JTextField getFirstNameTextField() {
		return firstNameTextField;
	}

	public JTextField getLastNameTextField() {
		return lastNameTextField;
	}

	public JTextField getUsernameTextField() {
		return usernameTextField;
	}
	
	public JComboBox getManagerRoleComboBox() {
		return managerRoleComboBox;
	}
	
	public JButton getCancelButton() {
		return cancelButton;
	}
	
	
	public JLabel getErrorLastNameLabel() {
		return errorLastNameLabel;
	}


	public JLabel getErrorFirstNameLabel() {
		return errorFirstNameLabel;
	}

	public JLabel getErrorPasswordLabel() {
		return errorPasswordLabel;
	}
}
