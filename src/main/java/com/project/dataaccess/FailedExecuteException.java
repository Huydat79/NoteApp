package com.project.dataaccess;

/**
 * Ngoại lệ không thể thực thi câu lệnh SQL

 */
public class FailedExecuteException extends DataAccessException {
    public FailedExecuteException() {
        super("Can't execute!");
    }
    
    public FailedExecuteException(String message) {
        super(message);
    }
}
