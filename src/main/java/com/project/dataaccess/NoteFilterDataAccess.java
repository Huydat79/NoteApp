package com.project.dataaccess;

import com.project.connection.MySQLDatabaseConnection;
import com.project.entity.NoteFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class NoteFilterDataAccess implements BasicDataAccess<NoteFilter, NoteFilterKey, NoteKey> {
    private final Connection connection;
    
    /**
     * Khởi tạo và lấy connection tới Database
     */
    private NoteFilterDataAccess() {
        connection = MySQLDatabaseConnection.getConnection();

    }

    private static class SingletonHelper {
        private static final NoteFilterDataAccess INSTANCE = new NoteFilterDataAccess();
    }    
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static NoteFilterDataAccess getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    @Override 
    public List<NoteFilter> getAll() throws DataAccessException {
        List<NoteFilter> noteFilters = new ArrayList<>();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT * FROM notefilters ORDER BY note_id, filter";

        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang noteFilter và thêm vào list
            while (resultSet.next()) {
                NoteFilter noteFilter = new NoteFilter();
                //Set dữ liệu cho noteFilter
                noteFilter.setFilterContent(resultSet.getString("FILTER"));
                noteFilters.add(noteFilter);
            }    
            //Nếu noteFilters rỗng thì ném ngoại lệ là danh sách trống
            return noteFilters;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override 
    public List<NoteFilter> getAll(NoteKey referKey) throws DataAccessException {
        List<NoteFilter> noteFilters = new ArrayList<>();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT filter FROM "
                + "notefilters nf, notes nt "
                + "WHERE nf.note_id = nt.note_id AND nt.note_id = ?";

        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, referKey.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang noteFilter và thêm vào list
            while (resultSet.next()) {
                NoteFilter noteFilter = new NoteFilter();
                //Set dữ liệu cho noteFilter
                noteFilter.setFilterContent(resultSet.getString("FILTER"));
                noteFilters.add(noteFilter);
            }    
            //Nếu noteFilters rỗng thì ném ngoại lệ là danh sách trống
            return noteFilters;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public NoteFilter get(NoteFilterKey key) throws DataAccessException {
        NoteFilter noteFilter = new NoteFilter();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT filter FROM "
                + "notefilters nf, notes nt"
                + " WHERE nf.note_id = nt.note_id AND nt.note_id = ? AND filter = ?";

        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, key.getNoteId());
            preparedStatement.setString(2, key.getFilter());
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang noteFilter và thêm vào list
            while (resultSet.next()) {
                //Set dữ liệu cho noteFilter
                noteFilter.setFilterContent(resultSet.getString("FILTER"));
            }    
            //Nếu không tồn tại thì ném ngoại lệ
            if(noteFilter.isDefaultValue()) {
                throw new NotExistDataException("Block is not exist!");
            }   
            return noteFilter;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public NoteFilter add(NoteFilter noteFilter) throws DataAccessException {
        throw new FailedExecuteException();
    }
    
    @Override
    public NoteFilter add(NoteFilter noteFilter, NoteFilterKey key) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "INSERT INTO notefilters(note_id, filter) "
                + "VALUES(?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, key.getNoteId());
            preparedStatement.setString(2, key.getFilter());
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            return noteFilter;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(NoteFilter noteFilter) throws DataAccessException {
        throw new FailedExecuteException();
    }
    
    @Override
    public void update(NoteFilter noteFilter, NoteFilterKey key) throws DataAccessException {
        throw new FailedExecuteException();
    }
    
    @Override
    public void delete(NoteFilterKey key) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM notefilters WHERE note_id = ? AND filter = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, key.getNoteId());
            preparedStatement.setString(2, key.getFilter());

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void deleteAll(NoteKey referKey) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM notefilters WHERE note_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, referKey.getId());
            

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}
