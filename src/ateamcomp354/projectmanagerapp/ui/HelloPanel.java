package ateamcomp354.projectmanagerapp.ui;

import javax.swing.JComponent;

import ateamcomp354.projectmanagerapp.ui.gen.HelloPanelGen;


public class HelloPanel {

	private HelloPanelGen helloPanelGen;
	
	/**
	 * Create the panel.
	 */
	public HelloPanel( String text ) {

		helloPanelGen = new HelloPanelGen();
		helloPanelGen.getHelloLbl().setText( text );
	}
	
	public JComponent getComponent() {
		return helloPanelGen;
	}

}
