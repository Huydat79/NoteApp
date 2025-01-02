package com.project.dataaccess;

import com.project.connection.MySQLDatabaseConnection;
import com.project.entity.Note;
import com.project.entity.ShareNote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai các phương thức thao tác dữ liệu với ShareNote
 */

public class ShareNoteDataAccess implements BasicDataAccess<ShareNote, ShareNoteKey, ShareNoteKey> {
    private final Connection connection;
    
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;

    private ShareNoteDataAccess() {
        connection = MySQLDatabaseConnection.getConnection();
        noteDataAccess = NoteDataAccess.getInstance();
    }
    
    private static class SingletonHelper {
        public static final ShareNoteDataAccess INSTANCE = new ShareNoteDataAccess();
    }
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static ShareNoteDataAccess getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private int getUserIdFromReceiver(String author) throws DataAccessException {
        if (connection == null) {
            throw new FailedExecuteException();
        }
        String query = "SELECT user_id FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, author);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            } else {
                throw new NotExistDataException("User not found for username: " + author);
            }
        } catch (SQLException ex) {

            throw new FailedExecuteException();
        }
}
    
    @Override
    public List<ShareNote> getAll() throws DataAccessException {
        List<ShareNote> shareNotes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT sh.NOTE_ID, us.USERNAME AS RECEIVER, SHARETYPE "
                + "FROM sharenotes sh, users us, notes nt "
                + "WHERE sh.RECEIVER_ID = us.USER_ID AND sh.NOTE_ID = nt.NOTE_ID "
                + "ORDER BY note_id";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                int noteId = resultSet.getInt("NOTE_ID");
                Note note = noteDataAccess.get(new NoteKey(noteId));
                shareNote.setNote(note);
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
                //Thêm note vào list
                shareNotes.add(shareNote);
            }
            return shareNotes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }
    
    @Override
    public List<ShareNote> getAll(ShareNoteKey referKey) throws DataAccessException {
        List<ShareNote> shareNotes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT sh.NOTE_ID, us.USERNAME AS RECEIVER, SHARETYPE "
                + "FROM sharenotes sh, users us, notes nt "
                + "WHERE sh.RECEIVER_ID = us.USER_ID AND sh.NOTE_ID = nt.NOTE_ID "
                + "AND us.USERNAME = ? ORDER BY sh.NOTE_ID";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, referKey.getReceiver());
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                int noteId = resultSet.getInt("NOTE_ID");
                Note note = noteDataAccess.get(new NoteKey(noteId));
                shareNote.setNote(note);
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
                //Thêm note vào list
                shareNotes.add(shareNote);
            }
            return shareNotes;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new FailedExecuteException();
        }       
    }

    @Override
    public ShareNote get(ShareNoteKey key) throws DataAccessException {
        ShareNote shareNote = new ShareNote();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT sh.NOTE_ID, us.USERNAME AS RECEIVER, SHARETYPE "
                + "FROM sharenotes sh, users us, notes nt "
                + "WHERE sh.RECEIVER_ID = us.USER_ID AND sh.NOTE_ID = nt.NOTE_ID "
                + "AND nt.NOTE_ID = ? AND us.USERNAME = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, key.getNoteId());
            preparedStatement.setString(2, key.getReceiver());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {                
                int noteId = resultSet.getInt("NOTE_ID");
                Note note = noteDataAccess.get(new NoteKey(noteId));
                shareNote.setNote(note);
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
            }
            if(shareNote.isDefaultValue()) {
                throw new NotExistDataException("This sharenote is not exist!");
            }
            return shareNote;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new FailedExecuteException();
        }
    }

    @Override
    public ShareNote add(ShareNote shareNote) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        
        int receiverId = getUserIdFromReceiver(shareNote.getReceiver());
        //Câu truy vấn SQL
        String query = "INSERT INTO SHARENOTES(NOTE_ID, RECEIVER_ID, SHARETYPE)"
                + "VALUES(?, ?, ?)";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, shareNote.getId());
            preparedStatement.setInt(2, receiverId);
            preparedStatement.setString(3, shareNote.getShareType().toString());

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            return shareNote;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public ShareNote add(ShareNote shareNote, ShareNoteKey key) throws DataAccessException {
        return this.add(shareNote);
    }

    @Override
    public void update(ShareNote shareNote) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        
        int receiverId = getUserIdFromReceiver(shareNote.getReceiver());

        
        //Câu truy vấn SQL
        String query = "UPDATE SHARENOTES SET SHARETYPE = ? "
                + "WHERE NOTE_ID = ? AND RECEIVER_ID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, shareNote.getShareType().toString());
            preparedStatement.setInt(2, shareNote.getId());
            preparedStatement.setInt(3, receiverId);

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(ShareNote shareNote, ShareNoteKey key) throws DataAccessException {
        this.update(shareNote);
    }

    @Override
    public void delete(ShareNoteKey key) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        
        int receiverId = getUserIdFromReceiver(key.getReceiver());

        //Câu truy vấn SQL
        String query = "DELETE FROM SHARENOTES WHERE NOTE_ID = ? AND RECEIVER_ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, key.getNoteId());
            preparedStatement.setInt(2, receiverId);

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void deleteAll(ShareNoteKey referKey) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM SHARENOTES WHERE NOTE_ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, referKey.getNoteId());

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}