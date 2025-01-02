package com.project.service;

import com.project.dataaccess.BasicDataAccess;
import com.project.dataaccess.DataAccessException;
import com.project.dataaccess.NoteDataAccess;
import com.project.dataaccess.NoteKey;
import com.project.dataaccess.UserKey;
import com.project.entity.Note;
import java.util.List;

/**
 * Mở note được chỉnh sửa gần nhất bởi user
 */

public class OpenLastNoteService implements ServerService<Note> {   
    private String author;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    
    public OpenLastNoteService() {
        author = "";
    }

    public OpenLastNoteService(String author) {
        this.author = author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public Note execute() throws DataAccessException { 
        noteDataAccess = NoteDataAccess.getInstance();
        List<Note> notes = noteDataAccess.getAll(new UserKey(author));
        return notes.get(notes.size() - 1);
    }    
}