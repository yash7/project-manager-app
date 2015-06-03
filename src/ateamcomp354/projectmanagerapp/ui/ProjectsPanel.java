package ateamcomp354.projectmanagerapp.ui;

import javax.swing.JComponent;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.ui.gen.SplitPane1Gen;
import ateamcomp354.projectmanagerapp.ui.gen.US1RightPanelGen;

public class ProjectsPanel {

	private final ApplicationContext appCtx;

	private SplitPane1Gen splitPane1Gen;
	private US1RightPanelGen us1RightPanelGen;
	private SwapInterface swap;
	
	public ProjectsPanel( ApplicationContext appCtx , SwapInterface swap )
	{
		this.appCtx = appCtx;
		this.swap = swap;
		
		splitPane1Gen = new SplitPane1Gen();

		us1RightPanelGen = new US1RightPanelGen();

		splitPane1Gen.getSplitPane().setRightComponent( us1RightPanelGen );
		
		splitPane1Gen.getLogoutButton().addActionListener(new LogoutListener(swap));
	}
	
	public JComponent getComponent()
	{
		return splitPane1Gen;
	}

}
