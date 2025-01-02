package com.project.service;

import com.project.dataaccess.BasicDataAccess;
import com.project.dataaccess.DataAccessException;
import com.project.dataaccess.NullKey;
import com.project.dataaccess.UserDataAccess;
import com.project.dataaccess.UserKey;
import com.project.entity.User;


/**
 * Cập nhật User
 */


public class UpdateUserService implements ServerService<User> {    
    private User user;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;
    
    public UpdateUserService() {
        user = new User();
    }

    public UpdateUserService(User user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User execute() throws DataAccessException {
        userDataAccess = UserDataAccess.getInstance();
        userDataAccess.update(user);
        return user;
    }    
}