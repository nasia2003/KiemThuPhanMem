package DAO;

import config.JDBCUtil;

import java.sql.Connection;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionCustom {
    private static Connection con = null;
    private static ConnectionCustom instance = null;
    private static final ReentrantLock lock = new ReentrantLock();
    private ConnectionCustom() {
        con = (Connection) JDBCUtil.getConnection();
    }

    public static ConnectionCustom getInstance() {
        if (instance == null) {  // Kiểm tra nhanh mà không cần lock
            lock.lock();  // Khóa để đảm bảo chỉ 1 luồng tạo instance
            try {
                if (instance == null) {  // Kiểm tra lại bên trong lock
                    instance = new ConnectionCustom();
                }
            } finally {
                lock.unlock();  // Giải phóng lock
            }
        }
        return instance;
    }

    public Connection getConnect() {
        return con;
    }
}
