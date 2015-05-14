package ateamcomp354.projectmanagerapp;

import java.awt.EventQueue;

import javax.swing.JFrame;

import ateamcomp354.projectmanagerapp.ui.MainFrame;

public class App {

	private MainFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new MainFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setLocationRelativeTo( null );
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
