package com.project.service;

import com.project.dataaccess.BasicDataAccess;
import com.project.dataaccess.DataAccessException;
import com.project.dataaccess.NoteDataAccess;
import com.project.dataaccess.NoteKey;
import com.project.dataaccess.UserKey;
import com.project.entity.Note;

import java.util.List;

/**
 * Lấy tất cả các note của một user
 */
public class GetAllNotesService implements ServerService<List<Note>> {   
    private String author;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    
    public GetAllNotesService() {
        author = "";
    }
    
    public GetAllNotesService(String author) {
        this.author = author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
  
    @Override
    public List<Note> execute() throws DataAccessException {       
        noteDataAccess = NoteDataAccess.getInstance();
        return noteDataAccess.getAll(new UserKey(author));
    }   
}