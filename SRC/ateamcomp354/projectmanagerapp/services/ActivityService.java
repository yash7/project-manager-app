package ateamcomp354.projectmanagerapp.services;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import java.util.List;

/**
 * Provides activity related functionality scoped to a specific project.
 */
public interface ActivityService {

    /**
     * @return The project this service's functionality are for.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    Project getProject();

    /**
     * @return all the activities in the project.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    List<Activity> getActivities();

    /**
     * @return activities that have their id in the parameter ids.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    List<Activity> getActivities( List<Integer> activityIds );

    /**
     * A convenience method to obtain an activity by id.
     *
     * @param activityId The activity id.
     * @return An activity whose id corresponds to the id parameter.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    Activity getActivity( int activityId );

    /**
     * Add a new activity to the project. The id property must be null. Other properties
     * may or may not be null.
     *
     * @param activity The activity to add to the project.
     * @throws IllegalStateException if the underlying project is completed
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    void addActivity( Activity activity );

    /**
     * Delete an activity from the project. The id property cannot be null.
     *
     * @param activityId The id of activity to murder.
     * @throws IllegalStateException if the underlying project is completed
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    void deleteActivity( int activityId );

    /**
     * Update changes made on an activity. The id property cannot be null
     * as well as any other non null fields.
     *
     * @param activity The activity to update.
     * @throws IllegalStateException if the underlying project is completed
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    void updateActivity( Activity activity );

    /**
     * Add a dependency between the activities represented by the two parameters.
     *
     * @param fromActivityId The id of the activity to depend on.
     * @param toActivityId The id of the activity that will depend on fromActivityId
     * @throws IllegalStateException if the underlying project is completed
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    void addDependency( int fromActivityId, int toActivityId );

    /**
     * Delete a dependency between the activities represented by the two parameters.
     *
     * @param fromActivityId The id of the activity to not depend on anymore.
     * @param toActivityId The id of the activity that depended on fromActivityId.
     * @throws IllegalStateException if the underlying project is completed
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    void deleteDependency( int fromActivityId, int toActivityId );

    /**
     * Get an activity's dependencies, or in graph terms,
     * get the incoming edges of an activity.
     *
     * @param toActivityId The activity that acts as a node.
     * @return The activity's dependencies as a list of ids.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    List<Integer> getDependencies( int toActivityId );

    /**
     * Get the dependents of an activity, or in graph terms,
     * get the outgoing edges of an activity.
     *
     * @param fromActivityId The activity that acts as a node.
     * @return The activity's that depend on the parameter activity as a list of ids.
     * @throws ServiceFunctionalityException if something went wrong with this functionality.
     */
    List<Integer> getDependents( int fromActivityId);

    void addUserToActivity( int activityId, Users user );

    void deleteUserFromActivity( int activityId, Users user );
    
    List<Users> getAssigneesForActivity(int activityId);
    
    List<Users> getUnassignedMembersForActivity(int activityId);

	List<Users> getProjectMembers();

	Users getProjectMember(int memberId);

	List<Integer> calculateAllParamsOfChain(int i, int ii);

	List<Integer> calculateNumberOfStartingNodes(List<Integer> startingNodes, int activityId);

	List<Integer> calculateSizeOfChain(List<Integer> nodes, int id);

	List<Integer> calculateNumberOfEndingNodes(List<Integer> endingNodes,	int activityId);
	
	void calculateEstimatesAndDerivatives();
}
