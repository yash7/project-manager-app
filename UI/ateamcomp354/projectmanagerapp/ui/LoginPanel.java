package ateamcomp354.projectmanagerapp.ui;

import javax.swing.JComponent;

import java.awt.event.*;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.LoginService;
import ateamcomp354.projectmanagerapp.ui.gen.LoginPanelGen;
import ateamcomp354.projectmanagerapp.ui.util.FrameSaver;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import ateamcomp354.projectmanagerapp.services.LoginFailedException;

public class LoginPanel {

	private final ApplicationContext appCtx;

	private LoginPanelGen loginPanelGen;
	
	private SwapInterface swap;
	
	private Users loggedInUser;
	
	
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
	
	public void logout() {
		loggedInUser = null;
		loginPanelGen.getUsernameField().setText("");
		loginPanelGen.getPasswordField().setText("");
		loginPanelGen.getValidityLabel().setText("");
	}
	
	public Users getLoggedInUser()
	{
		return loggedInUser;
	}
	
	class passwordActionListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			LoginService ls = appCtx.getLoginService();
			
			try
			{
				String username = loginPanelGen.getUsernameField().getText();
				String password = new String(loginPanelGen.getPasswordField().getPassword());
				loggedInUser = ls.login( username , password );
				if (loggedInUser.getManagerRole())
				{
					FrameSaver projectFrame = new FrameSaver();
					projectFrame.setFrameName("PROJECTS_PANEL");
					
					swap.saveFrame(projectFrame);
					swap.showProjectsView();
		
				}
				else
				{
					FrameSaver memberProjectFrame = new FrameSaver();
					memberProjectFrame.setFrameName("MEMBERPROJECT_PANEL");
					memberProjectFrame.setFirstID(loggedInUser.getId());
					
					swap.saveFrame(memberProjectFrame);
					swap.showMemberProjectsView(loggedInUser.getId());
					
					
				}
						
			}
			catch (LoginFailedException lfe)
			{
				loginPanelGen.getPasswordField().setText("");
				loginPanelGen.getValidityLabel().setText("Invalid username/password combination");
			}
			
		}
		
	}
	
}
