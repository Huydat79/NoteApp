package com.project.dataaccess;

/**
 * Ngoại lệ cho việc access dữ liệu
 */
public class DataAccessException extends Exception {
    public DataAccessException(String message) {
        super(message);
    }
}