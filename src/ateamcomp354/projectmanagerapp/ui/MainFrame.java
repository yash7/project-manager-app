package ateamcomp354.projectmanagerapp.ui;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	private HelloPanel helloPanel;
	
	public MainFrame() {
		
		helloPanel = new HelloPanel();
		add( helloPanel.getComponent() );
	}
}
