package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.services.LoginFailedException;
import ateamcomp354.projectmanagerapp.services.LoginService;
import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

public class LoginServiceImpl implements LoginService {

    private final DSLContext create;

    public LoginServiceImpl(DSLContext create) {
        this.create = create;
    }

    @Override
    public Users login(String username, String password) throws LoginFailedException {
        return null;
    }
}
