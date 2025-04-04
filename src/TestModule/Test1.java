package TestModule;

import DAO.ChiTietSanPhamDAO;
import DTO.ChiTietSanPhamDTO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class Test1 {
    @Test
    public void a() {
        ChiTietSanPhamDAO dao = ChiTietSanPhamDAO.getInstance();
        try {
            dao.con.setAutoCommit(false);
            ChiTietSanPhamDTO sp = new ChiTietSanPhamDTO();
            sp.setImei("123456789");
            sp.setMaphienbansp(3);
            sp.setMaphieunhap(3);
            int ok = dao.insert(sp);
            Assert.assertEquals(1, ok);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dao.con.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
