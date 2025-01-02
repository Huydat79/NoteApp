package com.project.dataaccess;

/**
 * Ngoại lệ cho việc dữ liệu không tồn tại
 */

public class NotExistDataException extends DataAccessException {  
    public NotExistDataException() {
        super("Data is not exist");
    }
    
    public NotExistDataException(String message) {
        super(message);
    }
}
