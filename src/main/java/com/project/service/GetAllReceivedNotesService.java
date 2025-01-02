package com.project.service;

import com.project.dataaccess.BasicDataAccess;
import com.project.dataaccess.DataAccessException;
import com.project.dataaccess.ShareNoteDataAccess;
import com.project.dataaccess.ShareNoteKey;
import com.project.entity.ShareNote;
import java.util.List;

/**
 * Lấy tất cả các ShareNote được share tới user
 */

public class GetAllReceivedNotesService implements ServerService<List<ShareNote>> {
    private String receiver;
    protected BasicDataAccess<ShareNote, ShareNoteKey, ShareNoteKey> shareNoteDataAccess;
    
    public GetAllReceivedNotesService() {
        receiver = "";
    }
    
    public GetAllReceivedNotesService(String receiver) {
        this.receiver = receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    @Override
    public List<ShareNote> execute() throws DataAccessException {
        shareNoteDataAccess = ShareNoteDataAccess.getInstance();
        return shareNoteDataAccess.getAll(new ShareNoteKey(-1, receiver));
    }
}