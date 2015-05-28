package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.services.ActivityService;
import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.Tables;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivityDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivitylinksDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activitylinks;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

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
        return projectDao.fetchOneById( projectId );
    }

    @Override
    public List<Activity> getActivities() {
        return activityDao.fetchByProjectId( projectId );
    }

    @Override
    public List<Activity> getActivities(List<Integer> ids) {

        return create.select()
                .from( Tables.ACTIVITY )
                .where( Tables.ACTIVITY.PROJECT_ID.eq( projectId ) )
                .fetchInto(Activity.class);
    }

    @Override
    public Activity getActivity(int id) {
        return activityDao.fetchOneById(id);
    }

    @Override
    public void addActivity(Activity activity) {
        activityDao.insert( activity );
    }

    @Override
    public void deleteActivity(int activityId) {
        activityDao.deleteById( activityId );
    }

    @Override
    public void updateActivity(Activity activity) {
        activityDao.update(activity);
    }

    @Override
    public void addDependency(int activityId, int dependingActivityId) {

        Activitylinks link = new Activitylinks( null, dependingActivityId, activityId );
        activitylinksDao.insert( link );
    }

    @Override
    public void deleteDependency(int activityId, int dependingActivityId) {

        create.deleteFrom( Tables.ACTIVITYLINKS )
                .where( Tables.ACTIVITYLINKS.FROM_ACTIVITY_ID.eq( dependingActivityId ) )
                .and( Tables.ACTIVITYLINKS.TO_ACTIVITY_ID.eq( activityId ) )
                .execute();
    }

    @Override
    public List<Integer> getDependencies(int activityId) {

        return activitylinksDao.fetchByToActivityId( activityId )
                .stream()
                .map( Activitylinks::getFromActivityId )
                .collect(Collectors.toList() );
    }

    @Override
    public List<Integer> getDependents(int activityId) {

        return activitylinksDao.fetchByFromActivityId( activityId )
                .stream()
                .map( Activitylinks::getToActivityId )
                .collect(Collectors.toList() );
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
