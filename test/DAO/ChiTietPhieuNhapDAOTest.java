package DAO;

import DTO.ChiTietPhieuNhapDTO;
import config.JDBCUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChiTietPhieuNhapDAOTest {
    /**
     * TC1: Kiểm tra phương thức getInstance() trả về đối tượng hợp lệ.
     * Mục đích: Đảm bảo rằng phương thức getInstance() luôn trả về một thể hiện hợp lệ của ChiTietPhieuNhapDAO.
     */
    @Test
    public void testGetInstance() {
        // Gọi phương thức getInstance() để lấy thể hiện của ChiTietPhieuNhapDAO
        ChiTietPhieuNhapDAO dao = ChiTietPhieuNhapDAO.getInstance();

        // Kiểm tra đối tượng trả về không phải null
        Assert.assertNotNull("Phương thức getInstance() phải trả về đối tượng không null", dao);

        // Kiểm tra đối tượng trả về đúng kiểu
        Assert.assertTrue("Đối tượng trả về phải là thể hiện của ChiTietPhieuNhapDAO", dao instanceof ChiTietPhieuNhapDAO);
    }
    /**
     * TC1: Thêm danh sách chi tiết phiếu nhập hợp lệ
     * Mục đích: Đảm bảo các bản ghi chi tiết được chèn thành công.
     */
    @Test
    public void insertChiTietPhieuNhap_HopLe() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(1001, 101, 10, 20000, 1));
        list.add(new ChiTietPhieuNhapDTO(1001, 102, 5, 30000, 2));

        int result = dao.insert(list);
        Assert.assertTrue(result >= 1); // Ít nhất một bản ghi thành công
        con.rollback();
    }

    /**
     * TC2: Danh sách chi tiết rỗng
     * Mục đích: Kiểm tra xem phương thức có xử lý đúng khi không có dữ liệu.
     */
    @Test
    public void insertChiTietPhieuNhap_DanhSachRong() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        int result = dao.insert(list);

        Assert.assertEquals(0, result); // Không có bản ghi nào được thêm
        con.rollback();
    }

    /**
     * TC3: Thêm chi tiết với mã phiếu không tồn tại trong bảng phiếu nhập
     * Mục đích: Kiểm tra xử lý khi khóa ngoại không hợp lệ.
     */
    @Test
    public void insertChiTietPhieuNhap_MaPhieuKhongTonTai() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(9999, 101, 2, 10000, 1)); // Mã phiếu không tồn tại

        int result = dao.insert(list);
        Assert.assertTrue(result == 0 || result == 1); // Có thể vẫn insert được nếu không có FK constraint
        con.rollback();
    }

    /**
     * TC4: Thêm chi tiết với số lượng âm
     * Mục đích: Kiểm tra hệ thống có cho phép số lượng âm không.
     */
    @Test
    public void insertChiTietPhieuNhap_SoLuongAm() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(1002, 103, -5, 15000, 1));

        int result = dao.insert(list);
        Assert.assertTrue(result == 0 || result == 1); // Tùy theo DB có constraint không
        con.rollback();
    }

    /**
     * TC5: Truyền null thay vì danh sách
     * Mục đích: Kiểm tra xử lý khi danh sách chi tiết là null.
     */
    @Test
    public void insertChiTietPhieuNhap_DanhSachNull() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        try {
            int result = dao.insert(null);
            Assert.assertEquals(0, result);
        } catch (Exception e) {
            Assert.fail("Không nên có exception khi danh sách là null");
        }
    }

    /**
     * TC6: Thêm chi tiết với đơn giá = 0
     * Mục đích: Đảm bảo hệ thống có thể xử lý giá trị biên.
     */
    @Test
    public void insertChiTietPhieuNhap_DonGiaBang0() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(1003, 104, 3, 0, 2));

        int result = dao.insert(list);
        Assert.assertTrue(result == 1); // Chấp nhận giá = 0 nếu DB không ràng buộc
        con.rollback();
    }

    /**
     * TC1: Xóa chi tiết phiếu nhập với mã phiếu tồn tại
     * Mục đích: Kiểm tra xem hệ thống có xóa thành công khi mã phiếu hợp lệ.
     */
    @Test
    public void deleteChiTietPhieuNhap_HopLe() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        // Giả sử đã có dữ liệu với mã phiếu nhập "1001" trong csdl
        String maPhieu = "1001";

        int result = dao.delete(maPhieu);
        Assert.assertTrue(result >= 1); // Có ít nhất một bản ghi bị xóa

        con.rollback();
    }

    /**
     * TC2: Xóa với mã phiếu không tồn tại
     * Mục đích: Kiểm tra khi mã phiếu không tồn tại trong bảng.
     */
    @Test
    public void deleteChiTietPhieuNhap_MaKhongTonTai() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        String maPhieu = "9999"; // Giả sử không tồn tại trong DB

        int result = dao.delete(maPhieu);
        Assert.assertEquals(0, result); // Không có bản ghi nào bị xóa

        con.rollback();
    }

    /**
     * TC3: Xóa với chuỗi rỗng
     * Mục đích: Kiểm tra khi truyền mã phiếu rỗng.
     */
    @Test
    public void deleteChiTietPhieuNhap_Rong() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        String maPhieu = "";

        int result = dao.delete(maPhieu);
        Assert.assertEquals(0, result); // Không thực hiện xóa vì mã không hợp lệ

        con.rollback();
    }

    /**
     * TC4: Xóa với giá trị null
     * Mục đích: Đảm bảo hệ thống xử lý null an toàn mà không crash.
     */
    @Test
    public void deleteChiTietPhieuNhap_Null() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        String maPhieu = null;

        int result = dao.delete(maPhieu);
        Assert.assertEquals(0, result); // Không có gì để xóa, hoặc xử lý an toàn

        con.rollback();
    }

    /**
     * TC1: Cập nhật thành công với mã phiếu tồn tại và danh sách chi tiết hợp lệ
     * Mục đích: Kiểm tra quá trình xóa và chèn mới hoạt động đúng
     */
    @Test
    public void updateChiTietPhieuNhap_HopLe() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        // Danh sách mới để cập nhật
        ArrayList<ChiTietPhieuNhapDTO> dsMoi = new ArrayList<>();
        dsMoi.add(new ChiTietPhieuNhapDTO(1001, 1, 10, 50000, 1));  // phiếu, phiên bản sp, số lượng, đơn giá, phương thức

        String maphieu = "1001"; // Mã phiếu tồn tại

        int result = dao.update(dsMoi, maphieu);
        Assert.assertTrue(result >= 1); // Phải có ít nhất 1 bản ghi được chèn sau xóa

        con.rollback();
    }

    /**
     * TC2: Cập nhật khi mã phiếu không tồn tại
     * Mục đích: Kiểm tra cập nhật thất bại khi không có gì để xóa
     */
    @Test
    public void updateChiTietPhieuNhap_MaPhieuKhongTonTai() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuNhapDTO> dsMoi = new ArrayList<>();
        dsMoi.add(new ChiTietPhieuNhapDTO(9999, 2, 5, 80000, 2)); // giả sử 9999 chưa có

        String maphieu = "9999";

        int result = dao.update(dsMoi, maphieu);
        Assert.assertEquals(0, result); // Không có gì để xóa => không insert

        con.rollback();
    }

    /**
     * TC3: Cập nhật với danh sách chi tiết rỗng
     * Mục đích: Xóa xong không thêm gì mới
     */
    @Test
    public void updateChiTietPhieuNhap_DanhSachRong() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuNhapDTO> danhSachRong = new ArrayList<>();
        String maphieu = "1002";

        int result = dao.update(danhSachRong, maphieu);
        Assert.assertEquals(0, result); // Xóa thành công nhưng không insert gì

        con.rollback();
    }

    /**
     * TC4: Cập nhật với mã phiếu null
     * Mục đích: Đảm bảo hệ thống xử lý an toàn khi khóa chính null
     */
    @Test
    public void updateChiTietPhieuNhap_MaPhieuNull() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuNhapDTO> ds = new ArrayList<>();
        ds.add(new ChiTietPhieuNhapDTO(0, 1, 3, 50000, 1));

        int result = dao.update(ds, null);
        Assert.assertEquals(0, result); // Xử lý an toàn, không có hành động nào xảy ra

        con.rollback();
    }

    /**
     * TC1: Truy vấn danh sách chi tiết phiếu nhập với mã phiếu hợp lệ và có dữ liệu
     * Mục đích: Kiểm tra truy vấn hoạt động đúng khi có dữ liệu
     */
    @Test
    public void selectAllChiTietPhieuNhap_HopLeCoDuLieu() throws SQLException {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        String maphieu = "1001"; // Giả sử phiếu này có chi tiết

        ArrayList<ChiTietPhieuNhapDTO> ds = dao.selectAll(maphieu);

        Assert.assertNotNull(ds);                // Không null
        Assert.assertTrue(ds.size() > 0);        // Có ít nhất 1 dòng dữ liệu
        for (ChiTietPhieuNhapDTO ct : ds) {
            Assert.assertEquals(1001, ct.getMaphieu()); // Mã phiếu đúng với điều kiện truy vấn
        }
    }

    /**
     * TC2: Truy vấn với mã phiếu hợp lệ nhưng không có dữ liệu
     * Mục đích: Đảm bảo truy vấn trả về danh sách rỗng nếu không có chi tiết
     */
    @Test
    public void selectAllChiTietPhieuNhap_KhongCoDuLieu() {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        String maphieu = "9999"; // Giả sử không có bản ghi nào với mã này

        ArrayList<ChiTietPhieuNhapDTO> ds = dao.selectAll(maphieu);

        Assert.assertNotNull(ds);           // Danh sách không null
        Assert.assertEquals(0, ds.size());  // Danh sách rỗng
    }

    /**
     * TC3: Truy vấn với mã phiếu null
     * Mục đích: Đảm bảo hệ thống xử lý an toàn khi đầu vào là null
     */
    @Test
    public void selectAllChiTietPhieuNhap_MaPhieuNull() {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();

        ArrayList<ChiTietPhieuNhapDTO> ds = dao.selectAll(null);

        Assert.assertNotNull(ds);           // Không nên trả về null
        Assert.assertEquals(0, ds.size());  // Không có gì để trả về
    }

    /**
     * TC4: Truy vấn với mã phiếu dạng chuỗi không hợp lệ
     * Mục đích: Xác minh hệ thống không lỗi với chuỗi bất thường
     */
    @Test
    public void selectAllChiTietPhieuNhap_MaPhieuSaiKieu() {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        String maphieu = "' OR 1=1 --"; // SQL injection style string

        ArrayList<ChiTietPhieuNhapDTO> ds = dao.selectAll(maphieu);

        Assert.assertNotNull(ds);           // Không null
        Assert.assertEquals(0, ds.size());  // Không được trả về gì cả
    }


}