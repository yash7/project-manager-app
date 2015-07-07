package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.services.*;

import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

public class ApplicationContextImpl implements ApplicationContext {

    private final DSLContext create;

    private final LoginService loginService;
    private final ProjectService projectService;
    private final UserService userService;
	private final CreateUserService createUserService;

    public ApplicationContextImpl(DSLContext create) {
        this.create = create;
        loginService = new LoginServiceImpl( create );
        projectService = new ProjectServiceImpl( create );
        userService = new UserServiceImpl(create);
        createUserService = new CreateUserServiceImpl(create);
    }

    @Override
    public LoginService getLoginService() {
        return loginService;
    }

    @Override
    public ProjectService getProjectService() {
        return projectService;
    }

    @Override
    public ActivityService getActivityService(int projectId) {
        return new ActivityServiceImpl( create, projectId );
    }

    @Override
    public ProjectMemberService getProjectMemberService(int userId) {
    	return new ProjectMemberServiceImpl(create, userId);
    }

	@Override
	public UserService getUserService() {
		return userService;
	}
	
	@Override
	public CreateUserService getCreateUserService() {
		return createUserService;
	}
}
