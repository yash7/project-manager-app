package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.services.ActivityService;
import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import java.util.List;

public class ActivityServiceImpl implements ActivityService {

    private final DSLContext create;

    private final int projectId;

    public ActivityServiceImpl(DSLContext create, int projectId) {
        this.create = create;
        this.projectId = projectId;
    }

    @Override
    public Project getProject() {
        return null;
    }

    @Override
    public List<Activity> getActivities() {
        return null;
    }

    @Override
    public Activity getActivity(int id) {
        return null;
    }

    @Override
    public void addActivity(Activity activity) {

    }

    @Override
    public void deleteActivity(Activity activity) {

    }

    @Override
    public void updateActivity(Activity activity) {

    }

    @Override
    public void addUserToActivity(Activity activity, Users user) {
        throw new UnsupportedOperationException("This is a proposition for user story 3. It may or may not be implemented later on.");
    }

    @Override
    public void deleteUserFromActivity(Activity activity, Users user) {
        throw new UnsupportedOperationException("This is a proposition for user story 3. It may or may not be implemented later on.");
    }
}
