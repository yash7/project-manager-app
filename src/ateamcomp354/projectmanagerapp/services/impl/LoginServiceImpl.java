package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.services.LoginFailedException;
import ateamcomp354.projectmanagerapp.services.LoginService;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.Tables;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.exception.DataAccessException;

public class LoginServiceImpl implements LoginService {

    private final DSLContext create;

    public LoginServiceImpl(DSLContext create) {
        this.create = create;
    }

    @Override
    public Users login(String username, String password) throws LoginFailedException {

        Users user;

        try {
            user = create.select()
                    .from(Tables.USERS)
                    .where(Tables.USERS.USERNAME.eq(username))
                    .and(Tables.USERS.PASSWORD.eq(password))
                    .fetchOneInto(Users.class);
        } catch (DataAccessException e) {
            throw new LoginFailedException();
        }

        if ( user == null ) {
            throw new LoginFailedException();
        }

        return user;
    }
}
