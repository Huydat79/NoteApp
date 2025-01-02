package com.project.dataaccess;

import com.project.connection.MySQLDatabaseConnection;
import com.project.entity.Note;
import com.project.entity.NoteFilter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai các phương thức thao tác dữ liệu với Note
 */
public class NoteDataAccess implements BasicDataAccess<Note, NoteKey, UserKey> {
    private final Connection connection;
    
    protected BasicDataAccess<NoteFilter, NoteFilterKey, NoteKey> filterDataAccess;

    /**
     * Khởi tạo và lấy connection tới Database
     */
    private NoteDataAccess() {
        connection = MySQLDatabaseConnection.getConnection();
        this.filterDataAccess = NoteFilterDataAccess.getInstance();
    }

    private static class SingletonHelper {
        private static final NoteDataAccess INSTANCE = new NoteDataAccess();
    }    
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    
    public static NoteDataAccess getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    private int getUserIdFromAuthor(String author) throws DataAccessException {
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
    public List<Note> getAll() throws DataAccessException {
        List<Note> notes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT nt.NOTE_ID AS ID, us.USERNAME AS AUTHOR, nt.HEADER, nt.CONTENT, nt.LASTMODIFIED, nt.LASTMODIFIEDDATE "
                + "FROM notes nt "
                + "JOIN users us ON nt.user_id = us.user_id "
                + "ORDER BY nt.NOTE_ID, nt.LASTMODIFIED desc, nt.LASTMODIFIEDDATE desc";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                Note note = new Note();
                //Set dữ liệu từ hàng vào note
                note.setId(resultSet.getInt("ID"));
                note.setAuthor(resultSet.getString("AUTHOR"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(resultSet.getString("CONTENT"));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                String dateStr = resultSet.getString("LASTMODIFIEDDATE");
                if (dateStr != null && !dateStr.trim().isEmpty()) {
                note.setLastModifiedDate(Date.valueOf(dateStr));
                } else {
                note.setLastModifiedDate(null); // Hoặc một giá trị mặc định nếu cần
                }               
                note.setFilters(filterDataAccess.getAll(new NoteKey(note.getId())));
                //Thêm note vào list
                notes.add(note);
            }
            return notes;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new FailedExecuteException();
        }       
    }
    
    @Override
    public List<Note> getAll(UserKey referKey) throws DataAccessException {
        List<Note> notes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT nt.NOTE_ID as ID, us.USERNAME AS AUTHOR, nt.HEADER, nt.CONTENT, nt.LASTMODIFIED, nt.LASTMODIFIEDDATE "
                + "FROM notes nt "
                + "JOIN users us ON nt.USER_ID = us.user_id "
                + "WHERE us.username =? "
                + "ORDER BY nt.NOTE_ID, nt.LASTMODIFIED desc, nt.LASTMODIFIEDDATE desc  ";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, referKey.getUsername());
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                Note note = new Note();
                //Set dữ liệu từ hàng vào note
                note.setId(resultSet.getInt("ID"));
                note.setAuthor(resultSet.getString("AUTHOR"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(resultSet.getString("CONTENT"));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setFilters(filterDataAccess.getAll(new NoteKey(note.getId())));
                //Thêm note vào list
                notes.add(note);
            }
            return notes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }

    @Override
    public Note get(NoteKey key) throws DataAccessException {
        Note note = new Note();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT nt.NOTE_ID as ID, us.USERNAME AS AUTHOR, nt.HEADER, nt.CONTENT, nt.LASTMODIFIED, nt.LASTMODIFIEDDATE "
                + "FROM notes nt "
                + "JOIN users us ON nt.USER_ID = us.user_id "
                + "WHERE nt.note_id = ? "
                + "ORDER BY nt.NOTE_ID, nt.LASTMODIFIED desc, nt.LASTMODIFIEDDATE desc ";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, key.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Set dữ liệu từ kết quả vào note
                note.setId(resultSet.getInt("ID"));
                note.setAuthor(resultSet.getString("AUTHOR"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(resultSet.getString("CONTENT"));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setFilters(filterDataAccess.getAll(new NoteKey(note.getId())));
            }
            if(note.isDefaultValue()) {
                throw new NotExistDataException("This note is not exist!");
            }
            return note;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new FailedExecuteException();
        }
    }

    @Override
    public Note add(Note note) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        
        int userId = getUserIdFromAuthor(note.getAuthor());

        //Câu truy vấn SQL
        String query = "INSERT INTO NOTES(NOTE_ID, USER_ID, HEADER, CONTENT, LASTMODIFIED, LASTMODIFIEDDATE) VALUES(?,?,?,?,?,?)";
        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query, 
                    PreparedStatement.RETURN_GENERATED_KEYS); 
            preparedStatement.setInt(1, note.getId());
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, note.getHeader());
            preparedStatement.setString(4, note.getContent());
            preparedStatement.setInt(5, note.getLastModified());
            preparedStatement.setDate(6, note.getLastModifiedDate());
            for(NoteFilter filter: note.getFilters()) {
                filterDataAccess.add(filter, new NoteFilterKey(note.getId(), filter.getFilterContent()));
            }
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) { // Di chuyển con trỏ đến dòng đầu tiên
                note.setId(resultSet.getInt(1)); // Lấy ID được sinh tự động
            } else {
                throw new FailedExecuteException("No ID generated for the new note");
            }
            
            return note;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public Note add(Note note, NoteKey key) throws DataAccessException {
        return this.add(note);
    }

    @Override
    public void update(Note note) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        
        int userId = getUserIdFromAuthor(note.getAuthor());

        //Câu truy vấn SQL
        String query = "UPDATE NOTES SET user_id = ?, HEADER = ?, CONTENT = ?, LASTMODIFIED = ?, " +
            "LASTMODIFIEDDATE = ? WHERE NOTE_ID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);            
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, note.getHeader());
            preparedStatement.setString(3, note.getContent());            
            preparedStatement.setInt(4, note.getLastModified());
            preparedStatement.setDate(5, note.getLastModifiedDate());
            preparedStatement.setInt(6, note.getId());
            //Kiểm tra có add các thông tin vừa rồi được ko, nếu có thì add filter
            filterDataAccess.deleteAll(new NoteKey(note.getId()));
            for(NoteFilter filter: note.getFilters()) {
                filterDataAccess.add(filter, new NoteFilterKey(note.getId(), filter.getFilterContent()));
            }
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(Note note, NoteKey key) throws DataAccessException {
        this.update(note);
    }
    
    @Override
    public void delete(NoteKey key) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM NOTES WHERE NOTE_ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, key.getId());
            //Xóa các filter
            filterDataAccess.deleteAll(key);
            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void deleteAll(UserKey referKey) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        
        int userId = getUserIdFromAuthor(referKey.getUsername());

        //Câu truy vấn SQL
        String query = "DELETE FROM NOTES WHERE user_id = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            //Xóa các filter của các Note tương ứng
            for(Note note: this.getAll(referKey)) {
                filterDataAccess.deleteAll(new NoteKey(note.getId()));
            }
            
            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}