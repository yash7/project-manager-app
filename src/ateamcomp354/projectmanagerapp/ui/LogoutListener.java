package ateamcomp354.projectmanagerapp.ui;

import java.awt.event.*;

public class LogoutListener implements ActionListener {
	
	private SwapInterface swap;
	
	public LogoutListener(SwapInterface swap) {
		this.swap = swap;
	}
	
	public void actionPerformed(ActionEvent e) {
		swap.showLoginView("logout");
	}

}
