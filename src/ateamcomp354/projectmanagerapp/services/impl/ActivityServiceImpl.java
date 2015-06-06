package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;
import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.Tables;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivityDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivitylinksDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activitylinks;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.exception.DataAccessException;

import java.util.List;
import java.util.stream.Collectors;

public class ActivityServiceImpl implements ActivityService {

    private final DSLContext create;
    private final ProjectDao projectDao;
    private final ActivityDao activityDao;
    private final ActivitylinksDao activitylinksDao;

    private final int projectId;

    public ActivityServiceImpl(DSLContext create, int projectId) {
        this.create = create;
        this.projectId = projectId;
        projectDao = new ProjectDao( create.configuration() );
        activityDao = new ActivityDao( create.configuration() );
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
    public void addActivity(Activity activity) {

        checkProjectNotCompleted( "Project to add activity to is completed" );

        try {
            activityDao.insert(activity);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to add a new activity", e);
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
        throw new UnsupportedOperationException("This is a proposition for user story 3. It may or may not be implemented later on.");
    }

    @Override
    public void deleteUserFromActivity(int activityId, Users user) {
        throw new UnsupportedOperationException("This is a proposition for user story 3. It may or may not be implemented later on.");
    }
}
