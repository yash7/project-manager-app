package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class HelloPanelGen extends JPanel {
	private JLabel helloLbl;

	/**
	 * Create the panel.
	 */
	public HelloPanelGen() {
		setLayout(new BorderLayout(0, 0));
		
		helloLbl = new JLabel("Hello, World!");
		helloLbl.setFont(new Font("Tahoma", Font.PLAIN, 42));
		helloLbl.setHorizontalAlignment(SwingConstants.CENTER);
		add(helloLbl, BorderLayout.CENTER);

	}

	public JLabel getHelloLbl() {
		return helloLbl;
	}
}
