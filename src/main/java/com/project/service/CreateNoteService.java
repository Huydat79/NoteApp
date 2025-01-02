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
 * Tạo một Note mới
 */
public class CreateNoteService implements ServerService<Note> {   
    private Note note;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;

    public CreateNoteService() {
        note = new Note();
    }

    public CreateNoteService(Note note) {
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
            throw new DataAccessException("This note already exists");
        } catch (FailedExecuteException ex1) {
            throw ex1;
        } catch (NotExistDataException ex2) {
            userDataAccess.get(new UserKey(note.getAuthor()));
            List<Note> existNotes = noteDataAccess.getAll();
            if (existNotes.isEmpty()) {
                // Nếu danh sách rỗng, gán ID mặc định cho note (ví dụ: 1)
                note.setId(1);
            } else {
                // Nếu danh sách không rỗng, lấy ID tiếp theo của note
                Note lastNote = existNotes.get(existNotes.size() - 1);
                for(Note note : existNotes){
                    System.out.println(note.getId());
                }
                note.setId(lastNote.getId() + 1);
            }         
        }
        return noteDataAccess.add(note);         
    }    
}