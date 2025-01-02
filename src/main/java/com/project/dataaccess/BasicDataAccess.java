package com.project.dataaccess;

import java.util.List;

/**
 * Định nghĩa các phương thức thao tác cơ bản với CSDL
 * @param <T> Kiểu datatransfer cho data từ CSDL
 */


public interface BasicDataAccess<T, K, R> {
    T get(K key) throws DataAccessException;
    
    T add(T element) throws DataAccessException;
    T add(T element, K key) throws DataAccessException;
    
    void update(T element) throws DataAccessException;
    void update(T element, K key) throws DataAccessException;
    
    void delete(K key) throws DataAccessException;
    void deleteAll(R referKey) throws DataAccessException;
    
    List<T> getAll() throws DataAccessException;
    List<T> getAll(R referKey) throws DataAccessException;
}