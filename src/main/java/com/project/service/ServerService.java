package com.project.service;

import com.project.dataaccess.DataAccessException;

/**
 * Định nghĩa các phương thức xử lý service bên server
 */
public interface ServerService<T> {   

    /**
     * Thực thi service
     * @return Kết quả của việc thực thi
     */
    
    T execute() throws DataAccessException;   
}