package ateamcomp354.projectmanagerapp.ui;

import ateamcomp354.projectmanagerapp.ui.util.FrameSaver;

public interface SwapInterface {

	void showLoginView();
	
	void showProjectsView();

	void showProjectsView( int preferredProjectId );
	
	void showActivitiesView( int projectId );
	
	void showMemberActivitiesView (int projectId, int userId);
	
	void showMemberProjectsView(int userId);

	void showCreateUserView();
	
	void showEditUserView(int userId);
	
	void showEditUserListView();

	/**
	 * Handles the switching from one frame to another. 
	 * It is designed to facilitate the navigation through the app.
	 */
	void frameSwitch();

	/**
	 * Saves the wanted frame by conserving it inside a stack.
	 */
	void saveFrame(FrameSaver savedFrame);

	/**
	 * Removes the frame that is conserved on top of the stack
	 * 
	 * @return returns the removed frame from the stack.
	 */
	FrameSaver removeSaveFrame();

	/**
	 * Fetches the frame conserved on top of the stack without
	 * removing it
	 * 
	 * @return returns the top frame from the stack
	 */
	FrameSaver getSaveFrame();

	void displayManagerMenuBar();
	
	void displayMemberMenuBar();
	
}
