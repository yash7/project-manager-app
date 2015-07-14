package ateamcomp354.projectmanagerapp.services;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

/**
 * The object that provides access to the necessary services.
 */
public interface ApplicationContext {

    /**
     * @return The service responsible for handling logins
     */
    LoginService getLoginService();

    /**
     * @return The service responsible for CRUD operation on projects.
     */
    ProjectService getProjectService();

    /**
     * @param projectId The id of the project for the activity operations to refer to.
     * @return A service responsible for CRUD operations on activities in the specified project.
     */
    ActivityService getActivityService( int projectId );
    
    ProjectMemberService getProjectMemberService( int userId );
    
    UserService getUserService();

	CreateUserService getCreateUserService();
}
