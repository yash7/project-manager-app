package ateamcomp354.projectmanagerapp.services.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.Tables;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivityDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UsersDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.exception.DataAccessException;

import ateamcomp354.projectmanagerapp.services.ProjectMemberService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;

public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final DSLContext create;
    private final ProjectDao projectDao;
    private final ActivityDao activityDao;
    private final UsersDao usersDao;
    private final int projectMemberId;
    
    public ProjectMemberServiceImpl(DSLContext create, int projectMemberId) {
        this.create = create;
        projectDao = new ProjectDao( create.configuration() );
        activityDao = new ActivityDao (create.configuration());
        usersDao = new UsersDao (create.configuration());
        this.projectMemberId = projectMemberId;
    }
	
	@Override
	public List<Activity> getAssignedActivities(int projectId) {
        try {
            return create.select()
                    .from(Tables.ACTIVITY)
                    .join(Tables.USERACTIVITIES).on(Tables.ACTIVITY.ID.eq(Tables.USERACTIVITIES.ACTIVITY_ID))
                    .join(Tables.USERS).on(Tables.USERS.ID.eq(Tables.USERACTIVITIES.USER_ID))
                    .where(Tables.USERS.ID.eq(projectMemberId))
                    .and(Tables.ACTIVITY.PROJECT_ID.eq(projectId))
                    .fetchInto(Activity.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get member's assigned activities", e);
        }
	}

	@Override
	public List<Project> getAssignedProjects() {
        try {
            return create.select()
                    .from(Tables.PROJECT)
                    .join(Tables.ACTIVITY).on(Tables.ACTIVITY.PROJECT_ID.eq(Tables.PROJECT.ID))
                    .join(Tables.USERACTIVITIES).on(Tables.ACTIVITY.ID.eq(Tables.USERACTIVITIES.ACTIVITY_ID))
                    .where(Tables.USERACTIVITIES.USER_ID.eq(projectMemberId))
                    .fetchInto(Project.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get member's assigned projects", e);
        }
	}
}
