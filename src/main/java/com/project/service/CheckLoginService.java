package com.project.service;

import com.project.dataaccess.BasicDataAccess;
import com.project.dataaccess.DataAccessException;
import com.project.dataaccess.NullKey;
import com.project.dataaccess.UserDataAccess;
import com.project.dataaccess.UserKey;
import com.project.entity.User;

/**
 * Kiểm tra thông tin đăng nhập
 */

public class CheckLoginService implements ServerService<User> {  
    private String username;
    private String password;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;
    
    public CheckLoginService() {
        username = "";
        password = "";
    }

    public CheckLoginService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public User execute() throws DataAccessException {
        userDataAccess = UserDataAccess.getInstance();
        User user = userDataAccess.get(new UserKey(username));
        if(password.equals(user.getPassword())) {
            return user;
        } else {
            throw new DataAccessException("Password is false!");
        }
    }    
}