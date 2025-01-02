package com.project.service;

import com.project.dataaccess.BasicDataAccess;
import com.project.dataaccess.DataAccessException;
import com.project.dataaccess.FailedExecuteException;
import com.project.dataaccess.NotExistDataException;
import com.project.dataaccess.NoteDataAccess;
import com.project.dataaccess.NoteKey;
import com.project.dataaccess.NullKey;
import com.project.dataaccess.ShareNoteDataAccess;
import com.project.dataaccess.ShareNoteKey;
import com.project.dataaccess.UserDataAccess;
import com.project.dataaccess.UserKey;
import com.project.entity.Note;
import com.project.entity.ShareNote;
import com.project.entity.User;


/**
 * Send một Note tới user khác
 */

public class SendNoteService implements ServerService<ShareNote> {
    private ShareNote shareNote;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDataAccess<ShareNote, ShareNoteKey, ShareNoteKey> shareNoteDataAccess;
    
    public SendNoteService() {
        shareNote = new ShareNote();
    }

    public SendNoteService(ShareNote shareNote) {
        this.shareNote = shareNote;
    }

    public void setShareNote(ShareNote shareNote) {
        this.shareNote = shareNote;
    }
        @Override
    public ShareNote execute() throws DataAccessException {
        userDataAccess = UserDataAccess.getInstance();
        noteDataAccess = NoteDataAccess.getInstance();
        shareNoteDataAccess = ShareNoteDataAccess.getInstance();
        try {
            shareNoteDataAccess.get(new ShareNoteKey(shareNote.getId(), shareNote.getReceiver()));
            shareNoteDataAccess.update(shareNote);
            return shareNote;
        } catch (FailedExecuteException ex1) {
            throw ex1;
        } catch (NotExistDataException ex2) {
            userDataAccess.get(new UserKey(shareNote.getReceiver()));
            noteDataAccess.get(new NoteKey(shareNote.getId()));
            return shareNoteDataAccess.add(shareNote);
        }
    }
}