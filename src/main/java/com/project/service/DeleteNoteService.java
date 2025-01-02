package com.project.service;

import com.project.dataaccess.BasicDataAccess;
import com.project.dataaccess.DataAccessException;
import com.project.dataaccess.NoteDataAccess;
import com.project.dataaccess.NoteKey;
import com.project.dataaccess.ShareNoteDataAccess;
import com.project.dataaccess.ShareNoteKey;
import com.project.dataaccess.UserKey;
import com.project.entity.Note;
import com.project.entity.ShareNote;
import java.util.List;

/**
 * Xóa một Note đã có
 */
public class DeleteNoteService implements ServerService<Note> {    
    private int noteId;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDataAccess<ShareNote, ShareNoteKey, ShareNoteKey> shareNoteDataAccess;
    
    public DeleteNoteService() {
        noteId = -1;
    }

    public DeleteNoteService(int noteId) {
        this.noteId = noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    @Override
    public Note execute() throws DataAccessException {    
        noteDataAccess = NoteDataAccess.getInstance();
        shareNoteDataAccess = ShareNoteDataAccess.getInstance();
        Note deleteNote = noteDataAccess.get(new NoteKey(noteId));
        if(deleteNote.getLastModified() == 1) {
            List<Note> remainNotes = noteDataAccess.getAll(new UserKey(deleteNote.getAuthor()));
            if(remainNotes.size() >= 2) {
                Note newLastNote = remainNotes.get(remainNotes.size()-2);
                newLastNote.setLastModified(1);
                noteDataAccess.update(newLastNote);
            }
        }
        shareNoteDataAccess.deleteAll(new ShareNoteKey(noteId, ""));
        noteDataAccess.delete(new NoteKey(noteId));
        return deleteNote;
    } 
}