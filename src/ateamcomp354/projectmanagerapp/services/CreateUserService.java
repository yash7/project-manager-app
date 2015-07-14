package ateamcomp354.projectmanagerapp.services;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;


/**
 * Provides user registration related functionality.
 */

public interface CreateUserService{
	
		
	    /**
	     * Determines the the role of the manager
	     * 
	     * @param managerRole The selection for the manager role
	     * 
	     */
		void ProjectMemberRole(String managerRole);
		
	
		/**
	     * Verifies if the username has already been chosen
	     * 
	     * @return return true if the username has already been taken
	     * 
	     */
		boolean duplicateUsername();
		
		
		void setNewMember(Users newMember);
}
