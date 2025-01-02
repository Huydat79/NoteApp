package com.project.service;

import com.project.dataaccess.BasicDataAccess;
import com.project.dataaccess.DataAccessException;
import com.project.dataaccess.FailedExecuteException;
import com.project.dataaccess.NotExistDataException;
import com.project.dataaccess.NullKey;
import com.project.dataaccess.UserDataAccess;
import com.project.dataaccess.UserKey;
import com.project.entity.User;

/**
 * Tạo một User mới
 */

public class CreateUserService implements ServerService<User> {    
    private User user;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;
    
    public CreateUserService() {
        user = new User();
    }

    public CreateUserService(User user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User execute() throws DataAccessException {       
        //Tạo một đối tượng access dữ liệu
        userDataAccess = UserDataAccess.getInstance();
        try {
            userDataAccess.get(new UserKey(user.getUsername()));
            throw new DataAccessException("This user is already exist!");
        } catch (FailedExecuteException ex1) {
            throw ex1;
        } catch (NotExistDataException ex2) {
            return userDataAccess.add(user);
        }
    }    
}