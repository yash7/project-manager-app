package ateamcomp354.projectmanagerapp.services;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.exception.DataAccessException;

import java.util.List;

/**
 * Provides project member related functionality
 */
public interface ProjectMemberService {
	
    List<Activity> getAssignedActivities(int projectId);
    
    List<Project> getAssignedProjects();

	List<Users> getOtherAssigneesForActivity(int activityId);
    
    //void markPercentCompleteOfActivity( Activity activity );
}
