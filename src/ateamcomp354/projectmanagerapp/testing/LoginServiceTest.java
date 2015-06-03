package ateamcomp354.projectmanagerapp.testing;

import static org.junit.Assert.*;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UsersDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.impl.DSL;
import org.junit.Test;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.LoginFailedException;
import ateamcomp354.projectmanagerapp.services.LoginService;
/**
 * 
 * @author Geodner 
 * 
 * The following class will test the action of login from the application
 * It uses it's own sample database to make search for any kind of error
 *
 */
public class LoginServiceTest extends AbstractDatabaseTest 
{
	
	@Test
	public void SuccessFullLogin() throws Exception
	{		
		InsertUsers();
	
	    ApplicationContext appCtx = App.getApplicationContext( db.getConnection());
	    LoginService loginService = appCtx.getLoginService();
	        
	    Users loginAttempt1 = new Users(null,null,null,"jdoe","top!secret",null);
        Users user1Result = loginService.login(loginAttempt1.getUsername(), loginAttempt1.getPassword());
        
        assertEquals(user1Result.getUsername(),loginAttempt1.getUsername());
        assertEquals("John",user1Result.getFirstName());
        assertEquals("Doe",user1Result.getLastName());
	     
	}
	
	@Test(expected = LoginFailedException.class)
	public void LoginFailedTest() throws Exception
	{
		InsertUsers();
		
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection());
		LoginService loginService = appCtx.getLoginService();
 
	    Users loginAttempt2 = new Users(null,null,null,"ssmith","incorectpass",null);
	    
	    @SuppressWarnings("unused")
		Users user2Result = loginService.login(loginAttempt2.getUsername(), loginAttempt2.getPassword());
	}
	
	@Test
	public void ManagerLoginTest() throws Exception
	{
		InsertUsers();
		
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection());
		LoginService loginService = appCtx.getLoginService();
 
	    Users loginAttempt3 = new Users(null,null,null,"dcarter","password1",null);
	    Users user3Result = loginService.login(loginAttempt3.getUsername(), loginAttempt3.getPassword());
	    
	    assertTrue(user3Result.getManagerRole() == true);    
	    assertEquals("Dwayne",user3Result.getFirstName());
        assertEquals("Carter",user3Result.getLastName());
	        
	}
	
	@Test
	public void MemberLoginTest() throws Exception
	{
		InsertUsers();
		
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection());
		LoginService loginService = appCtx.getLoginService();
		
	    Users loginAttempt4 = new Users( null, null, null, "ssmith", "super_secret", null );
	    Users user4Result = loginService.login(loginAttempt4.getUsername(), loginAttempt4.getPassword());
	    
	    assertTrue(user4Result.getManagerRole() == false);
	    assertEquals("Sarah",user4Result.getFirstName());
	    assertEquals("Smith",user4Result.getLastName());
	
	}
	
	@Test(expected = LoginFailedException.class)
	public void NullLoginTest() throws Exception
	{
		InsertUsers();
		
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection());
		LoginService loginService = appCtx.getLoginService();
		
	    Users loginAttempt4 = new Users( null, null , null, null, null, null );
	    
	    @SuppressWarnings("unused")
		Users user4Result = loginService.login(loginAttempt4.getUsername(), loginAttempt4.getPassword());
	  
	}
	
	
	@Test(expected = LoginFailedException.class)
	public void DeletedUserLoginTest() throws Exception
	{
		InsertUsers();
		RemoveUser();
		
		ApplicationContext appCtx = App.getApplicationContext( db.getConnection());
		LoginService loginService = appCtx.getLoginService();
		
	    Users loginAttempt5 = new Users( null, null , null, "dcarter", "password1", null );
		Users user5Result = loginService.login(loginAttempt5.getUsername(), loginAttempt5.getPassword());
		
	}
	
	/**
	 * Sample "Users" database use for testing login attempts
	 */
	public void InsertUsers()
	{
		DSLContext create = DSL.using(db.getConnection(), SQLDialect.SQLITE);
		UsersDao usersDao = new UsersDao( create.configuration() );
	
		usersDao.deleteById(1); // deletes the default admin user
		List <Users> userList = usersDao.findAll();
		assertEquals(userList.size(), 0);
	
		if ( usersDao.fetchByUsername("jdoe").isEmpty() )
		{
			usersDao.insert( new Users( null, "John", "Doe", "jdoe", "top!secret", true ) );
		}
		
		if ( usersDao.fetchByUsername("ssmith").isEmpty() )
		{
			usersDao.insert( new Users( null, "Sarah", "Smith", "ssmith", "super_secret", false ) );
		}
		
		if ( usersDao.fetchByUsername("dcarter").isEmpty() )
		{
			usersDao.insert( new Users( null, "Dwayne", "Carter", "dcarter", "password1", true ) );
		}
		
		Users jdoe = usersDao.fetchByUsername("jdoe").get(0);
		Users ssmith = usersDao.fetchByUsername("ssmith").get(0);
		Users dcarter = usersDao.fetchByUsername("dcarter").get(0);
		
		userList.add(jdoe);
		userList.add(ssmith);
		userList.add(dcarter);
		
		assertEquals(userList.size(), 3);
	}
	
	/**
	 * Sample "Users" database use for testing login attempts
	 */
	public void RemoveUser()
	{
		DSLContext create = DSL.using(db.getConnection(), SQLDialect.SQLITE);
		UsersDao usersDao = new UsersDao( create.configuration() );
	
		List <Users> userList = usersDao.findAll();
		assertEquals(userList.size(), 3);
		
		Users deleteUser = usersDao.fetchByUsername("dcarter").get(0);
		usersDao.delete( deleteUser);
		userList = usersDao.findAll();
		
		assertEquals(userList.size(),2);
		
	}
}
