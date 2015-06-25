package ateamcomp354.projectmanagerapp.services;

import java.util.List;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

public interface UserService {
	
	List<Users> getUsers();
	
	Users getUser(int userId);
	
	void addUser( Users user );
	
	void deleteUser (int userId);
	
	void updateUser (Users user);
}
