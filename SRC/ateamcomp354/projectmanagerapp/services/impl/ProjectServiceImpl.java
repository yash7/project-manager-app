package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;

import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.Tables;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectmembersDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UseractivitiesDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Projectmembers;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Useractivities;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.exception.DataAccessException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

public class ProjectServiceImpl implements ProjectService {

    private final DSLContext create;
    private final ProjectDao projectDao;
    private final ProjectmembersDao projectMembersDao;

    public ProjectServiceImpl(DSLContext create) {
        this.create = create;
        projectDao = new ProjectDao( create.configuration() );
        projectMembersDao = new ProjectmembersDao (create.configuration());
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

	@Override
	public void addUserToProject(int projectId, Users user) {
	    try {
	    	projectMembersDao.insert(new Projectmembers ( null, projectId, user.getId()) );
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to add a new user to project", e);
	    }
	}

	@Override
	public List<Users> getUnassignedMembersForProject(int projectId) {
        try {
            return create.select(Tables.USERS.fields())
                    .from(Tables.USERS)
                    .where(Tables.USERS.ID.notIn(create.select(Tables.USERS.ID).from(Tables.USERS).join(Tables.PROJECTMEMBERS).on(Tables.PROJECTMEMBERS.USER_ID.equal(Tables.USERS.ID))
                    .where(Tables.PROJECTMEMBERS.PROJECT_ID.equal(projectId))))
                    .fetchInto(Users.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project members for project" + projectId, e);
        }
	}

	@Override
	public void deleteUserFromProject(int projectId, Users user) {
	    try {
	    	create.deleteFrom(Tables.PROJECTMEMBERS)
	    	.where(Tables.PROJECTMEMBERS.PROJECT_ID.equal(projectId))
	    	.and(Tables.PROJECTMEMBERS.USER_ID.equal(user.getId()))
	    	.execute();
	    	
	    	create.deleteFrom(Tables.USERACTIVITIES)
	    	.where(Tables.USERACTIVITIES.USER_ID.equal(user.getId()))
	    	.execute();
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to add delete user from project", e);
	    }
	}

	@Override
	public List<Users> getMembersForProject(int projectId) {
        try {
            return create.select(Tables.USERS.fields())
                    .from(Tables.USERS)
                    .join(Tables.PROJECTMEMBERS).on(Tables.PROJECTMEMBERS.USER_ID.equal(Tables.USERS.ID))
                    .where(Tables.PROJECTMEMBERS.PROJECT_ID.equal(projectId))
                    .fetchInto(Users.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project members for project " + projectId, e);
        }
	}

	@Override
	public void updateProjectBudgetAtCompletion(int projectId) {
        try {
            List<Integer> plannedValues = create.select(Tables.ACTIVITY.PLANNED_VALUE)
            		.from(Tables.ACTIVITY)
            		.where(Tables.ACTIVITY.PROJECT_ID.equal(projectId))
            		.fetchInto(Integer.class);
            
            int sum = 0;
            
            for (Integer i : plannedValues)
            	sum += i;
            
            create.update(Tables.PROJECT)
            .set(Tables.PROJECT.BUDGET_AT_COMPLETION, sum)
            .where(Tables.PROJECT.ID.equal(projectId))
            .execute();
            
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("unable to update project's BAC " + projectId, e);
        }
	}

	@Override
	public void ernedValueAnalysis(int projectId) {
		try{
			//LocalDate localDate = new LocalDate();
			
			//System.out.println(localDate.toString()+ "  localDate");	
			
			Date todayDate = new Date();
			
			System.out.println((todayDate.getTime() / (1000 * 60 * 60 * 24)));
			
			List<Integer> dates = create.select(Tables.ACTIVITY.EARLIEST_START)
					.from(Tables.ACTIVITY)
					.where(Tables.ACTIVITY.PROJECT_ID.equal(projectId))
					.fetchInto(Integer.class);
			
			for (Integer i: dates){
				
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				Date iDate = format.parse(i.toString());
				
				int daysBetween = (int) Math.abs( (todayDate.getTime() - iDate.getTime()) / (1000*60*60*24) );
				
				if( Math.abs(daysBetween) >=7)
				{
					System.out.println(iDate + " its been a week! Days: " + daysBetween);
				}
				else
				{
					System.out.println(iDate + " it hasn't been a week yet! Days: " + daysBetween);
				}
			}
				
		}
		catch(Exception e)
		{
			throw new ServiceFunctionalityException("" + projectId ,e);
		}
		
	}
}
