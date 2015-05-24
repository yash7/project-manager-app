package ateamcomp354.projectmanagerapp.ui;

import javax.swing.JComponent;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.LoginService;
import ateamcomp354.projectmanagerapp.ui.gen.LoginPanelGen;

public class LoginPanel {

	private final ApplicationContext appCtx;

	private LoginPanelGen loginPanelGen;
	
	public LoginPanel( ApplicationContext appCtx ) {

		this.appCtx = appCtx;

		loginPanelGen = new LoginPanelGen();
	}
	
	public JComponent getComponent() {
		return loginPanelGen;
	}
	
}
