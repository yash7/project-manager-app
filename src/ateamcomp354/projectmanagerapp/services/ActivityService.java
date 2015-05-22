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
     */
    Project getProject();

    /**
     * @return all the activities in the project.
     */
    List<Activity> getActivities();

    /**
     * @return activities that have their id in the parameter ids.
     */
    List<Activity> getActivities( List<Integer> ids );

    /**
     * A convenience method to obtain an activity by id.
     *
     * @param id The activity id.
     * @return An activity whose id corresponds to the id parameter.
     */
    Activity getActivity( int id );

    /**
     * Add a new activity to the project. The id property must be null. Other properties
     * may or may not be null.
     *
     * @param activity The activity to add to the project.
     */
    void addActivity( Activity activity );

    /**
     * Delete an activity from the project. The id property cannot be null.
     *
     * @param activityId The id of activity to murder.
     */
    void deleteActivity( int activityId );

    /**
     * Update changes made on an activity. The id property cannot be null
     * as well as any other non null fields.
     *
     * @param activity The activity to update.
     */
    void updateActivity( Activity activity );

    // User story 3 proposition
    void addUserToActivity( int activityId, Users user );

    // User story 3 proposition
    void deleteUserFromActivity( int activityId, Users user );
}
