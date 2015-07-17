package ateamcomp354.projectmanagerapp.services.impl;

import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.Tables;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UsersDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import ateamcomp354.projectmanagerapp.services.CreateUserService;

public class CreateUserServiceImpl implements CreateUserService
{
	private Users newMember;
	private final DSLContext create;
	public  CreateUserServiceImpl (DSLContext create) 
	{
		this.create = create;
	    new UsersDao( create.configuration() );
	}
	
	@Override
	public boolean duplicateUsername()
	{
		
		Users queryUsername = create.select()
							.from(Tables.USERS)
							.where(Tables.USERS.USERNAME.eq(newMember.getUsername()))
							.fetchOneInto(Users.class);
		
		if (queryUsername == null )
			return false;
		else if (queryUsername.getUsername().equals( newMember.getUsername()))
			return true;
		else
			return false;
	}
	
	@Override
	public void ProjectMemberRole(String managerRole)
	{
		if (managerRole.equals("Yes"))
			this.newMember.setManagerRole(true);
		else if(managerRole.equals("No"))
			this.newMember.setManagerRole(false);
	}
	
	
	public Users getNewMember() {
		return newMember;
	}

	public void setNewMember(Users newMember) {
		this.newMember = newMember;
	}

	
}
