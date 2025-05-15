package DAO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author robot
 */
public interface DAOinterface<T> {
    public int insert(T t);
    
    public int update(T t) throws SQLException;
    
    public int delete(String t);
    
    public ArrayList<T> selectAll();
    
    public T selectById(String t);
    
    
    int getAutoIncrement();
}
