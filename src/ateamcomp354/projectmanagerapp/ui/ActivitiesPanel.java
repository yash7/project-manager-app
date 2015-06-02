package ateamcomp354.projectmanagerapp.ui;

import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.ui.gen.SplitPane1Gen;

import javax.swing.*;

public class ActivitiesPanel {

	private final ApplicationContext appCtx;
	
	private SwapInterface swap;

	private SplitPane1Gen splitPane1Gen;

	public ActivitiesPanel( ApplicationContext appCtx , SwapInterface swap) {

		this.appCtx = appCtx;
		this.swap = swap;

		splitPane1Gen = new SplitPane1Gen();
	}
	
	public JComponent getComponent()
	{
		return splitPane1Gen;
	}

}
