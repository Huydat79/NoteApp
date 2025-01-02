package com.project.service;

import com.project.dataaccess.BasicDataAccess;
import com.project.dataaccess.DataAccessException;
import com.project.dataaccess.NoteDataAccess;
import com.project.dataaccess.NoteKey;
import com.project.dataaccess.UserKey;
import com.project.entity.Note;

/**
 * Mở note với header và author cho trước
 */


public class OpenNoteService implements ServerService<Note> {    
    private int noteId;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    
    public OpenNoteService() {
        noteId = -1;
    }

    public OpenNoteService(int noteId) {
        this.noteId = noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
 
    @Override
    public Note execute() throws DataAccessException {        
        noteDataAccess = NoteDataAccess.getInstance();
        return noteDataAccess.get(new NoteKey(noteId));
    }    
}