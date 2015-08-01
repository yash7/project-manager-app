package ateamcomp354.projectmanagerapp.services;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import java.util.Date;
import java.util.List;

/**
 * Provides project related functionality.
 */
public interface ProjectService {

    /**
     * @return all the projects that exist.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    List<Project> getProjects();

    /**
     * A convenience method to obtain a project by id.
     *
     * @param projectId The project id.
     * @return A project whose id corresponds to the id parameter, or null if no projects with projectId exists.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    Project getProject( int projectId );

    /**
     * @param projectId The id of the project to determine completion.
     * @return A percentage from 0 to 100.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    int getProjectCompletion( int projectId );

    /**
     * @param projectId The id of the project to count its activities
     * @return The number of activities in the project
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    int getProjectActivitiesCount( int projectId );

    /**
     * Add a new project. The id property must be null. Other properties
     * may or may not be null.
     *
     * @param project The project to add.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    void addProject( Project project );

    /**
     * Delete a project from existence! The id property cannot be null.
     *
     * @param projectId The id of the project to murder.
     * @throws java.lang.IllegalArgumentException if the project has activities and is not completed.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    void deleteProject( int projectId );

    /**
     * Update changes made on a project. The id property cannot be null
     * as well as any other non null fields.
     *
     * @param project The project to update
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    void updateProject( Project project );
    
    void addUserToProject(int projectId, Users user);
    
    List<Users> getUnassignedMembersForProject(int projectId);
    
    void deleteUserFromProject(int projectId, Users user);
    
    List<Users> getMembersForProject(int projectId);
    
    void updateProjectBudgetAtCompletion (int projectId);
    
    void updateActualCostAtCompletion(int projectId);
    
    List<Activity> EVactivitiesByEarliestStart(int projectId);
    
    List<Object> EVStartDate(int projectId);
}
