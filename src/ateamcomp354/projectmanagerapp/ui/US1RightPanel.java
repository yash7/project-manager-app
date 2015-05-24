package ateamcomp354.projectmanagerapp.ui;

import javax.swing.JComponent;

import ateamcomp354.projectmanagerapp.ui.gen.US1RightPanelGen;

public class US1RightPanel {

	private US1RightPanelGen uS1RightPanelGen;
	
	public US1RightPanel()
	{
		uS1RightPanelGen = new US1RightPanelGen();
	}
	
	public JComponent getComponent()
	{
		return uS1RightPanelGen;
	}
	
}
