package ateamcomp354.projectmanagerapp.ui;

public interface SwapInterface {

	public void showLoginView();
	
	public void showProjectsView();

	public void showProjectsView( int preferredProjectId );
	
	public void showActivitiesView( int projectId );

}