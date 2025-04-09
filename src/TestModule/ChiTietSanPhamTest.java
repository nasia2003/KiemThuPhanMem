package TestModule;

import DAO.ChiTietSanPhamDAO;
import DTO.ChiTietSanPhamDTO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class ChiTietSanPhamTest {
    @Test
    public void insert_ThanhCong() {
        ChiTietSanPhamDAO dao = ChiTietSanPhamDAO.getInstance();
        try {
            dao.con.setAutoCommit(false);
            ChiTietSanPhamDTO sp = new ChiTietSanPhamDTO();
            // Sản phẩm được nhập từ phiếu nhập 3, có Imei và mã phiên bản như dưới.
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

    @Test
    public void insert_SQLException() {
        ChiTietSanPhamDAO dao = ChiTietSanPhamDAO.getInstance();
        try {
            dao.con.setAutoCommit(false);

            ChiTietSanPhamDTO sp = new ChiTietSanPhamDTO();
            sp.setImei(null); // gây lỗi vì imei null
            sp.setMaphienbansp(3);
            sp.setMaphieunhap(3);

            int result = dao.insert(sp);

            Assert.assertEquals(0, result); // Khi có SQLException, hàm trả về 0
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

    @Test
    public void insert_PhieuNhapKhongTonTai() {
        ChiTietSanPhamDAO dao = ChiTietSanPhamDAO.getInstance();
        try {
            dao.con.setAutoCommit(false);
            ChiTietSanPhamDTO sp = new ChiTietSanPhamDTO();
            // Sản phẩm được nhập từ phiếu nhập 3000, có Imei và mã phiên bản như dưới.
            sp.setImei("123456789");
            sp.setMaphienbansp(3);
            sp.setMaphieunhap(3000);
            int ok = dao.insert(sp);
            Assert.assertEquals(0, ok);
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

    @Test
    public void insert_TrungImei() {
        ChiTietSanPhamDAO dao = ChiTietSanPhamDAO.getInstance();
        try {
            dao.con.setAutoCommit(false);
            ChiTietSanPhamDTO sp = new ChiTietSanPhamDTO();
            // Sản phẩm được nhập từ phiếu nhập 3000, có Imei và mã phiên bản như dưới.
            sp.setImei("107725056444797");
            sp.setMaphienbansp(3);
            sp.setMaphieunhap(3);
            int ok = dao.insert(sp);
            Assert.assertEquals(0, ok);
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
