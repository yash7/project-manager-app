package ateamcomp354.projectmanagerapp;

import java.awt.EventQueue;

import javax.swing.JFrame;

import ateamcomp354.projectmanagerapp.ui.MainFrame;

public class App {

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
