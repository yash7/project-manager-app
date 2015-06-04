package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;
import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.Tables;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.exception.DataAccessException;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private final DSLContext create;
    private final ProjectDao projectDao;

    public ProjectServiceImpl(DSLContext create) {
        this.create = create;
        projectDao = new ProjectDao( create.configuration() );
    }

    @Override
    public List<Project> getProjects() {
        try {
            return projectDao.findAll();
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get all projects", e);
        }
    }

    @Override
    public Project getProject(int projectId) {
        try {
            return projectDao.fetchOneById(projectId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project with id " + projectId, e);
        }
    }

    @Override
    public int getProjectCompletion(int projectId) {

        int activities = countActivities( projectId );
        int completedActivities = countActivities( projectId, Status.RESOLVED );

        if ( activities == 0 ) {
            return 0;
        }
        else {
            float roughPercent = completedActivities / ((float) activities);
            int percent = (int) (roughPercent * 100);
            return Math.max( 0, Math.min( percent, 100 ) );
        }
    }

    @Override
    public int getProjectActivitiesCount(int projectId) {
        return countActivities( projectId );
    }

    @Override
    public void addProject(Project project) {
        try {
            projectDao.insert(project);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to add a new project", e);
        }
    }

    @Override
    public void deleteProject(int projectId) {

        Project project = getProject( projectId );

        if ( project == null ) {
            return;
        }

        if ( !project.getCompleted() ) {

            int count = countActivities(projectId);

            if ( count != 0 ) {
                throw new IllegalArgumentException( "Project to delete is not completed and has activities: " + count );
            }
        }

        try {
            projectDao.deleteById(projectId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to delete project with id " + projectId, e);
        }
    }

    @Override
    public void updateProject(Project project) {
        try {
            projectDao.update(project);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to update existing project", e);
        }
    }

    private int countActivities(int projectId) {

        try {
            return create.selectCount()
                    .from(Tables.ACTIVITY)
                    .where(Tables.ACTIVITY.PROJECT_ID.eq(projectId))
                    .fetchOne()
                    .value1();
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to count activities in project with id " + projectId, e);
        }
    }

    private int countActivities(int projectId, Status status) {

        try {
            return create.selectCount()
                    .from(Tables.ACTIVITY)
                    .where(Tables.ACTIVITY.PROJECT_ID.eq(projectId))
                    .and(Tables.ACTIVITY.STATUS.eq(status))
                    .fetchOne()
                    .value1();
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to count activities with status " + status + " in project with id " + projectId, e);
        }
    }
}
