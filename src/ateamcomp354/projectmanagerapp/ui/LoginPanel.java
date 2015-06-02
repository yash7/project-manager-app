package ateamcomp354.projectmanagerapp.ui;

import javax.swing.JComponent;
import java.awt.event.*;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.LoginService;
import ateamcomp354.projectmanagerapp.ui.gen.LoginPanelGen;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import ateamcomp354.projectmanagerapp.services.LoginFailedException;

public class LoginPanel {

	private final ApplicationContext appCtx;

	private LoginPanelGen loginPanelGen;
	
	private SwapInterface swap;
	
	public LoginPanel( ApplicationContext appCtx , SwapInterface swap) {

		this.appCtx = appCtx;
		this.swap = swap;
		
		loginPanelGen = new LoginPanelGen();
		
		loginPanelGen.getOKButton().addActionListener(new passwordActionListener());
		loginPanelGen.getPasswordField().addActionListener(new passwordActionListener());
	}
	
	public JComponent getComponent() {
		return loginPanelGen;
	}
	
	class passwordActionListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			LoginService l = appCtx.getLoginService();
			try
			{
				String username = loginPanelGen.getUsernameField().getText();
				String password = new String(loginPanelGen.getPasswordField().getPassword());
				Users u = l.login( username , password );
				swap.showView("PROJECTS_PANEL");
			}
			catch (LoginFailedException lfe)
			{
				loginPanelGen.getUsernameField().setText("");
				loginPanelGen.getPasswordField().setText("");
			}
			
		}
		
	}
	
}
