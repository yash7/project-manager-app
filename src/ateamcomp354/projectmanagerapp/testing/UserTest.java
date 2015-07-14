package ateamcomp354.projectmanagerapp.testing;

import static org.junit.Assert.*;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UsersDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.UserService;

public class UserTest extends AbstractDatabaseTest {
	
	private UserService use;
	private ApplicationContext appCtx;
	
	@Test
	public void testgetUser()  {
		InsertUsers();
		
		Users u = use.getUser(1);
		
		assertEquals("John", u.getFirstName());
	}
	
	@Test
	public void testgetUsers()  {
		InsertUsers();
		
		List<Users> u = use.getUsers();
		
		assertEquals(3, u.size());
	}
	
	@Test
	public void testaddUser()  {
		InsertUsers();
		
		Users u = new Users( null, "Testing", "Smith", "tsmith", "super_secret", false );
		
		use.addUser(u);
		
		assertEquals(4, use.getUsers().size());
	}
	
	@Test
	public void testdeleteUser()  {
		InsertUsers();
		
		assertEquals(3, use.getUsers().size());
		
		use.deleteUser(3);
		
		assertEquals(2, use.getUsers().size());
	}
	
	@Test
	public void testupdateUser()  {
		InsertUsers();
		
		Users u = use.getUser(1);
		
		assertEquals("John", u.getFirstName());
		
		u.setFirstName("Joe");
		
		use.updateUser(u);
		
		assertEquals("Joe", use.getUser(1).getFirstName());
	}
	
	@Before
	public void initial() {
		appCtx = App.getApplicationContext( db.getConnection() );
		use = appCtx.getUserService();
	}
	
	/**
	 * Borrowed from Geo
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