package ateamcomp354.projectmanagerapp.ui;

import javax.swing.JComponent;

import ateamcomp354.projectmanagerapp.ui.gen.SplitPane1Gen;

public class SplitPane1 {
	
	private SplitPane1Gen splitPane1Gen;
	
	public SplitPane1()
	{
		splitPane1Gen = new SplitPane1Gen();
	}
	
	public JComponent getComponent()
	{
		return splitPane1Gen;
	}

}
