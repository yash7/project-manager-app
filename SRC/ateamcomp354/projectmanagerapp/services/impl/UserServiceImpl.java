package ateamcomp354.projectmanagerapp.services.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UsersDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.exception.DataAccessException;

import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;
import ateamcomp354.projectmanagerapp.services.UserService;

public class UserServiceImpl implements UserService {

    private final DSLContext create;
    private final UsersDao usersDao;
    
    public UserServiceImpl (DSLContext create) {
    	this.create = create;
    	usersDao = new UsersDao (create.configuration());
    }
	
    @Override
    public Users getUser(int userId) {
        try {
            return usersDao.fetchOneById(userId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get user with id " + userId, e);
        }
    }

	@Override
	public List<Users> getUsers() {
        try {
            return usersDao.findAll();
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get all users", e);
        }
	}

	@Override
	public void addUser(Users user) {
	    try {
	        usersDao.insert(user);
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to add a new user", e);
	    }
	}

	@Override
	public void deleteUser(int userId) {
	    try {
	        usersDao.deleteById(userId);
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to delete an existing user", e);
	    }
	}

	@Override
	public void updateUser(Users user) {
	    try {
	        usersDao.update(user);
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to update a user", e);
	    }
	}

}
