package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;

import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.Tables;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivityDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivitylinksDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UseractivitiesDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activitylinks;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Useractivities;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.exception.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityServiceImpl implements ActivityService {

    private final DSLContext create;
    private final ProjectDao projectDao;
    private final ActivityDao activityDao;
    private final UseractivitiesDao userActivitiesDao;
    private final ActivitylinksDao activitylinksDao;

    private final int projectId;

    public ActivityServiceImpl(DSLContext create, int projectId) {
        this.create = create;
        this.projectId = projectId;
        projectDao = new ProjectDao( create.configuration() );
        activityDao = new ActivityDao( create.configuration() );
        userActivitiesDao = new UseractivitiesDao (create.configuration());
        activitylinksDao = new ActivitylinksDao( create.configuration() );
    }

    @Override
    public Project getProject() {
        try {
            return projectDao.fetchOneById(projectId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project with id " + projectId, e);
        }
    }

    @Override
    public List<Activity> getActivities() {
        try {
            return activityDao.fetchByProjectId(projectId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get all activities", e);
        }
    }

    @Override
    public List<Activity> getActivities(List<Integer> activityIds) {
        try {
            return create.select()
                    .from(Tables.ACTIVITY)
                    .where(Tables.ACTIVITY.PROJECT_ID.eq(projectId))
                    .and(Tables.ACTIVITY.ID.in(activityIds))
                    .fetchInto(Activity.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get specified activities", e);
        }
    }

    @Override
    public Activity getActivity(int activityId) {
        try {
            return activityDao.fetchOneById(activityId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get activity with id " + activityId, e);
        }
    }

    @Override
    public void deleteActivity(int activityId) {

        checkProjectNotCompleted( "Project to delete activity from is completed" );

        try {
            activityDao.deleteById(activityId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to delete activity with id " + activityId, e);
        }
    }

    @Override
	public void addActivity(Activity activity) {
	
	    checkProjectNotCompleted( "Project to add activity to is completed" );
	
	    try {
	        activityDao.insert(activity);
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to add a new activity", e);
	    }
	}

	@Override
    public void updateActivity(Activity activity) {
        try {
            activityDao.update(activity);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to update existing activity", e);
        }
    }

    @Override
    public void addDependency(int fromActivityId, int toActivityId) {

        checkProjectNotCompleted( "Project to add activity dependency is completed" );
        checkCircularDependency(fromActivityId, toActivityId, "Project will have circular activities if these were added");
        
        Activitylinks link = new Activitylinks( null, fromActivityId, toActivityId );

        try {
            activitylinksDao.insert(link);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to add a new dependency from " + fromActivityId + " to " + toActivityId , e);
        }
    }

    @Override
    public void deleteDependency(int fromActivityId, int toActivityId) {

        checkProjectNotCompleted( "Project to delete activity dependency is completed" );
        

        try {
            create.deleteFrom(Tables.ACTIVITYLINKS)
                    .where(Tables.ACTIVITYLINKS.FROM_ACTIVITY_ID.eq(fromActivityId))
                    .and(Tables.ACTIVITYLINKS.TO_ACTIVITY_ID.eq(toActivityId))
                    .execute();
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to delete a dependency from " + fromActivityId + " to " + toActivityId , e);
        }
    }

    private void checkCircularDependency(int fromActivityId, int toActivityId, String string) {
		if(addActivityToList(fromActivityId, toActivityId, new ArrayList<Integer>()) == null) {
			throw new ServiceFunctionalityException(string);
		}
	}
    
    private List<Integer> addActivityToList(int toAdd, int activityId, List<Integer> activities) {
    	
    	List<Integer> links = getDependencies(toAdd);
    	if(activities.contains(toAdd)) {
    		return null;
    	}
    	if(links.size() == 0) {
    		return links;
    	}
    	
    	for (int x : links) {
    		if(activities.contains(activityId)) {
    			return null;
    		}
    		else {
    			activities.add(activityId);
    			List<Integer> y = addActivityToList(x, toAdd, activities);
    			if(y == null) {
    				return null;
    			}
    		}
    	}
    	
    	return activities;
    }
    
	@Override
    public List<Integer> getDependencies(int toActivityId) {

        List<Activitylinks> links;

        try {
            links = activitylinksDao.fetchByToActivityId(toActivityId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get dependencies of activity " + toActivityId , e);
        }

        return links.stream()
                .map( Activitylinks::getFromActivityId )
                .collect( Collectors.toList() );
    }

    @Override
    public List<Integer> getDependents(int fromActivityId) {

        List<Activitylinks> links;

        try {
            links = activitylinksDao.fetchByFromActivityId(fromActivityId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get dependents of activity " + fromActivityId , e);
        }

        return links.stream()
                .map( Activitylinks::getToActivityId )
                .collect( Collectors.toList() );
    }

    private void checkProjectNotCompleted( String errMsg ) {   	
        if ( getProject().getCompleted() ) {
            throw new IllegalStateException( errMsg );
        }
    }

    @Override
    public void addUserToActivity(int activityId, Users user) {
    	
    	checkUserInProject(user.getId(), "User is not in project activity belongs to");
    	
	    try {
	    	userActivitiesDao.insert(new Useractivities ( null, activityId, user.getId()));
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to add a new user to activity", e);
	    }
    }
    
    private void checkUserInProject( int memberId, String errMsg ) {   
        if ( getProjectMember(memberId) == null ) {
            throw new IllegalStateException( errMsg );
        }
    }

    @Override
    public void deleteUserFromActivity(int activityId, Users user) {
	    try {
	    	create.deleteFrom(Tables.USERACTIVITIES)
	    	.where(Tables.USERACTIVITIES.ACTIVITY_ID.equal(activityId))
	    	.and(Tables.USERACTIVITIES.USER_ID.equal(user.getId()))
	    	.execute();
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to add delete user from activity", e);
	    }
    }
    
    @Override
    public List<Users> getAssigneesForActivity(int activityId)
    {
        try {
            return create.select(Tables.USERS.fields())
                    .from(Tables.USERS)
                    .join(Tables.USERACTIVITIES).on(Tables.USERACTIVITIES.USER_ID.equal(Tables.USERS.ID))
                    .where(Tables.USERACTIVITIES.ACTIVITY_ID.equal(activityId))
                    .fetchInto(Users.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project members for activity " + activityId, e);
        }
    }

	@Override
	public List<Users> getUnassignedMembersForActivity(int activityId) {
        try {
        	return create.select(Tables.USERS.fields())
                    .from(Tables.USERS)
                    .join(Tables.PROJECTMEMBERS).on(Tables.PROJECTMEMBERS.USER_ID.equal(Tables.USERS.ID))
                    .where(Tables.PROJECTMEMBERS.PROJECT_ID.equal(create.select(Tables.ACTIVITY.PROJECT_ID)
                    		.from(Tables.ACTIVITY)
                    		.where(Tables.ACTIVITY.ID.equal(activityId))))
                    .and(Tables.PROJECTMEMBERS.USER_ID.notIn(create.select(Tables.PROJECTMEMBERS.USER_ID)
                    		.from(Tables.PROJECTMEMBERS)
                    		.join(Tables.USERACTIVITIES)
                    		.on(Tables.USERACTIVITIES.USER_ID.equal(Tables.PROJECTMEMBERS.USER_ID))
                    		.where(Tables.USERACTIVITIES.ACTIVITY_ID.equal(activityId))))
                    .fetchInto(Users.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project members for activity " + activityId, e);
        }
	}
	
	@Override
    public Users getProjectMember(int memberId)
    {
        try {
            return create.select(Tables.USERS.fields())
                    .from(Tables.USERS)
                    .join(Tables.PROJECTMEMBERS).on(Tables.PROJECTMEMBERS.USER_ID.equal(Tables.USERS.ID))
                    .where(Tables.PROJECTMEMBERS.PROJECT_ID.equal(this.projectId))
                    .and(Tables.PROJECTMEMBERS.USER_ID.equal(memberId))
                    .fetchOneInto(Users.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project member "+memberId+" for project", e);
        }
    }
	
	@Override
    public List<Users> getProjectMembers()
    {
        try {
            return create.select(Tables.USERS.fields())
                    .from(Tables.USERS)
                    .join(Tables.PROJECTMEMBERS).on(Tables.PROJECTMEMBERS.USER_ID.equal(Tables.USERS.ID))
                    .where(Tables.PROJECTMEMBERS.PROJECT_ID.equal(this.projectId))
                    .fetchInto(Users.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project members for project", e);
        }
    }
}
