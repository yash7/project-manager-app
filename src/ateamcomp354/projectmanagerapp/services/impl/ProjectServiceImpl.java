package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.Tables;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

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
        return projectDao.findAll();
    }

    @Override
    public Project getProject(int id) {
        return projectDao.fetchOneById( id );
    }

    @Override
    public int getProjectCompletion(int id) {

        int activities = create.selectCount()
                .from( Tables.ACTIVITY )
                .where( Tables.ACTIVITY.PROJECT_ID.eq( id ) )
                .fetchOne()
                .value1();

        int completedActivities = create.selectCount()
                .from( Tables.ACTIVITY )
                .where( Tables.ACTIVITY.PROJECT_ID.eq( id ) )
                .and( Tables.ACTIVITY.STATUS.eq( Status.RESOLVED ) )
                .fetchOne()
                .value1();

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
    public void addProject(Project project) {
        projectDao.insert( project );
    }

    @Override
    public void deleteProject(int projectId) {
        projectDao.deleteById( projectId );
    }

    @Override
    public void updateProject(Project project) {
        projectDao.update( project );
    }
}
