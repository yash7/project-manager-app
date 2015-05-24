package ateamcomp354.projectmanagerapp.ui;

import javax.swing.JComponent;

import ateamcomp354.projectmanagerapp.ui.gen.LoginPanelGen;

public class LoginPanel {

	private LoginPanelGen loginPanelGen;
	
	public LoginPanel()
	{
		loginPanelGen = new LoginPanelGen();
	}
	
	public JComponent getComponent() {
		return loginPanelGen;
	}
	
}
