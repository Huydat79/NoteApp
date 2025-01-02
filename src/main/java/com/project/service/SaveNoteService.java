package com.project.service;

import com.project.dataaccess.BasicDataAccess;
import com.project.dataaccess.DataAccessException;
import com.project.dataaccess.FailedExecuteException;
import com.project.dataaccess.NotExistDataException;
import com.project.dataaccess.NoteDataAccess;
import com.project.dataaccess.NoteKey;
import com.project.dataaccess.NullKey;
import com.project.dataaccess.UserDataAccess;
import com.project.dataaccess.UserKey;
import com.project.entity.Note;
import com.project.entity.User;
import java.util.List;

/**
 * Lưu một note
 */
public class SaveNoteService implements ServerService<Note> {    
    private Note note;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;
    
    public SaveNoteService() {
        note = new Note();
    }    

    public SaveNoteService(Note note) {
        this.note = note;
    }
    
    public void setNote(Note note) {
        this.note = note;
    }
    

    @Override
    public Note execute() throws DataAccessException {
        noteDataAccess = NoteDataAccess.getInstance();
        userDataAccess = UserDataAccess.getInstance();
        try {
            noteDataAccess.get(new NoteKey(note.getId()));
            noteDataAccess.update(note);
            return note;
        } catch (FailedExecuteException ex1) {
            throw ex1;
        } catch (NotExistDataException ex2) {
            userDataAccess.get(new UserKey(note.getAuthor()));
            List<Note> existNotes = noteDataAccess.getAll();
            Note lastNote = existNotes.get(existNotes.size() - 1);
            note.setId(lastNote.getId() + 1);
            return noteDataAccess.add(note);
        }     
    } 
}