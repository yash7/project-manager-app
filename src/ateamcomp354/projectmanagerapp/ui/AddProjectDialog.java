package ateamcomp354.projectmanagerapp.ui;

import javax.swing.JDialog;

import ateamcomp354.projectmanagerapp.ui.gen.AddProjectDialogGen;

public class AddProjectDialog {
	
	private AddProjectDialogGen addProjectDialogGen;
	
	public AddProjectDialog()
	{
		addProjectDialogGen = new AddProjectDialogGen();
	}
	
	// AddProjectDialog extends JDialog, thus cannot return JComponent
	public JDialog getDialog()
	{
		return addProjectDialogGen;
	}
}
