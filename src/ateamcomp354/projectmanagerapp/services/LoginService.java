package ateamcomp354.projectmanagerapp.services;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

/**
 * Provides login related functionality.
 */
public interface LoginService {

    /**
     * @param username The entered username.
     * @param password The entered password.
     * @return A user if the login succeeded, otherwise throws a LoginFailException.
     * @throws LoginFailedException If the login failed.
     */
    Users login( String username, String password ) throws LoginFailedException;
    
    Users getLoggedInUser();
}
