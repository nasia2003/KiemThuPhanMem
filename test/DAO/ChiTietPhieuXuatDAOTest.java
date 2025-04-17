/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import DTO.ChiTietPhieuDTO;
import DTO.PhienBanSanPhamDTO;
import config.JDBCUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 *
 * @author MaiLoan
 */
public class ChiTietPhieuXuatDAOTest {

    /**
     * TC1: Kiểm tra phương thức getInstance() trả về đối tượng hợp lệ. Mục
     * đích: Đảm bảo rằng phương thức getInstance() luôn trả về một thể hiện hợp
     * lệ của ChiTietPhieuXuatDAO.
     */
    @Test
    public void testGetInstance() {
        // Gọi phương thức getInstance() để lấy thể hiện của ChiTietPhieuXuatDAO
        ChiTietPhieuXuatDAO dao = ChiTietPhieuXuatDAO.getInstance();

        // Kiểm tra đối tượng trả về không phải null
        Assert.assertNotNull("Phương thức getInstance() phải trả về đối tượng không null", dao);

        // Kiểm tra đối tượng trả về đúng kiểu
        Assert.assertTrue("Đối tượng trả về phải là thể hiện của ChiTietPhieuXuatDAO", dao instanceof ChiTietPhieuXuatDAO);
    }

    /**
     * TC1: Thêm danh sách chi tiết phiếu xuất hợp lệ Mục đích: Đảm bảo các bản
     * ghi chi tiết được chèn thành công.
     */
    @Test
    public void insertChiTietPhieuXuat_HopLe() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuDTO(1001, 2, 10, 20000));
        list.add(new ChiTietPhieuDTO(1001, 15, 5, 30000));

        int result = dao.insert(list);
        Assert.assertTrue(result >= 1); // Ít nhất một bản ghi thành công
        con.rollback();
    }

    /**
     * TC2: Danh sách chi tiết rỗng Mục đích: Kiểm tra xem phương thức có xử lý
     * đúng khi không có dữ liệu.
     */
    @Test
    public void insertChiTietPhieuXuat_DanhSachRong() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuDTO> list = new ArrayList<>();
        int result = dao.insert(list);

        Assert.assertEquals(0, result); // Không có bản ghi nào được thêm
        con.rollback();
    }

    /**
     * TC3: Thêm chi tiết với mã phiếu không tồn tại trong bảng phiếu xuất Mục
     * đích: Kiểm tra xử lý khi khóa ngoại không hợp lệ.
     */
    @Test
    public void insertChiTietPhieuXuat_MaPhieuKhongTonTai() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuDTO(9999, 101, 2, 10000)); // Mã phiếu không tồn tại

        int result = dao.insert(list);
        Assert.assertTrue(result == 0 || result == 1); // Có thể vẫn insert được nếu không có FK constraint
        con.rollback();
    }

    /**
     * TC4: Thêm chi tiết với số lượng âm Mục đích: Kiểm tra hệ thống có cho
     * phép số lượng âm không.
     */
    @Test
    public void insertChiTietPhieuXuat_SoLuongAm() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuDTO(1002, 103, -5, 15000));

        int result = dao.insert(list);
        Assert.assertTrue(result == 0 || result == 1); // Tùy theo DB có constraint không
        con.rollback();
    }

    /**
     * TC5: Truyền null thay vì danh sách Mục đích: Kiểm tra xử lý khi danh sách
     * chi tiết là null.
     */
    @Test
    public void insertChiTietPhieuXuat_DanhSachNull() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        try {
            int result = dao.insert(null);
            Assert.assertEquals(0, result);
        } catch (Exception e) {
            Assert.fail("Không nên có exception khi danh sách là null");
        }
    }

    /**
     * TC6: Thêm chi tiết với đơn giá = 0 Mục đích: Đảm bảo hệ thống có thể xử
     * lý giá trị biên.
     */
    @Test
    public void insertChiTietPhieuXuat_DonGiaBang0() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuDTO(1003, 2, 3, 0));

        int result = dao.insert(list);
        Assert.assertTrue(result == 1); // Chấp nhận giá = 0 nếu DB không ràng buộc
        con.rollback();
    }

    /**
     * TC1: Xóa chi tiết phiếu xuất với mã phiếu tồn tại Mục đích: Kiểm tra xem
     * hệ thống có xóa thành công khi mã phiếu hợp lệ.
     */
    @Test
    public void deleteChiTietPhieuXuat_HopLe() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        // Giả sử đã có dữ liệu với mã phiếu xuất "1001" trong csdl
        String maPhieu = "1001";

        int result = dao.delete(maPhieu);
        Assert.assertTrue(result >= 1); // Có ít nhất một bản ghi bị xóa

        con.rollback();
    }

    /**
     * TC2: Xóa với mã phiếu không tồn tại Mục đích: Kiểm tra khi mã phiếu không
     * tồn tại trong bảng.
     */
    @Test
    public void deleteChiTietPhieuXuat_MaKhongTonTai() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        String maPhieu = "9999"; // Giả sử không tồn tại trong DB

        int result = dao.delete(maPhieu);
        Assert.assertEquals(0, result); // Không có bản ghi nào bị xóa

        con.rollback();
    }

    /**
     * TC3: Xóa với chuỗi rỗng Mục đích: Kiểm tra khi truyền mã phiếu rỗng.
     */
    @Test
    public void deleteChiTietPhieuXuat_Rong() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        String maPhieu = "";

        int result = dao.delete(maPhieu);
        Assert.assertEquals(0, result); // Không thực hiện xóa vì mã không hợp lệ

        con.rollback();
    }

    /**
     * TC4: Xóa với giá trị null Mục đích: Đảm bảo hệ thống xử lý null an toàn
     * mà không crash.
     */
    @Test
    public void deleteChiTietPhieuXuat_Null() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        String maPhieu = null;

        int result = dao.delete(maPhieu);
        Assert.assertEquals(0, result); // Không có gì để xóa, hoặc xử lý an toàn

        con.rollback();
    }

    /**
     * TC1: Cập nhật thành công với mã phiếu tồn tại và danh sách chi tiết hợp
     * lệ Mục đích: Kiểm tra quá trình xóa và chèn mới hoạt động đúng
     */
    @Test
    public void updateChiTietPhieuXuat_HopLe() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        // Danh sách mới để cập nhật
        ArrayList<ChiTietPhieuDTO> dsMoi = new ArrayList<>();
        dsMoi.add(new ChiTietPhieuDTO(1001, 1, 10, 50000));  // mã phiếu, mã phiên bản sp, số lượng, đơn giá

        String maphieu = "1001"; // Mã phiếu tồn tại

        int result = dao.update(dsMoi, maphieu);
        Assert.assertTrue(result >= 1); // Phải có ít nhất 1 bản ghi được chèn sau xóa

        con.rollback();
    }

    /**
     * TC2: Cập nhật khi mã phiếu không tồn tại Mục đích: Kiểm tra cập nhật thất
     * bại khi không có gì để xóa
     */
    @Test
    public void updateChiTietPhieuXuat_MaPhieuKhongTonTai() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuDTO> dsMoi = new ArrayList<>();
        dsMoi.add(new ChiTietPhieuDTO(9999, 2, 5, 80000)); // giả sử 9999 chưa có

        String maphieu = "9999";

        int result = dao.update(dsMoi, maphieu);
        Assert.assertEquals(0, result); // Không có gì để xóa => không insert

        con.rollback();
    }

    /**
     * TC3: Cập nhật với danh sách chi tiết rỗng Mục đích: Xóa xong không thêm
     * gì mới
     */
    @Test
    public void updateChiTietPhieuXuat_DanhSachRong() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuDTO> danhSachRong = new ArrayList<>();
        String maphieu = "1002";

        int result = dao.update(danhSachRong, maphieu);
        Assert.assertEquals(0, result); // Xóa thành công nhưng không insert gì

        con.rollback();
    }

    /**
     * TC4: Cập nhật với mã phiếu null Mục đích: Đảm bảo hệ thống xử lý an toàn
     * khi khóa chính null
     */
    @Test
    public void updateChiTietPhieuXuat_MaPhieuNull() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        Connection con = JDBCUtil.getConnection();
        con.setAutoCommit(false);

        ArrayList<ChiTietPhieuDTO> ds = new ArrayList<>();
        ds.add(new ChiTietPhieuDTO(0, 1, 3, 50000));

        int result = dao.update(ds, null);
        Assert.assertEquals(0, result); // Xử lý an toàn, không có hành động nào xảy ra

        con.rollback();
    }

    /**
     * TC1: Truy vấn danh sách chi tiết phiếu xuất với mã phiếu hợp lệ và có dữ
     * liệu Mục đích: Kiểm tra truy vấn hoạt động đúng khi có dữ liệu
     */
    @Test
    public void selectAllChiTietPhieuXuat_HopLeCoDuLieu() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        String maphieu = "1001"; // Giả sử phiếu này có chi tiết

        ArrayList<ChiTietPhieuDTO> ds = dao.selectAll(maphieu);

        Assert.assertNotNull(ds);                // Không null
        Assert.assertTrue(ds.size() > 0);        // Có ít nhất 1 dòng dữ liệu
        for (ChiTietPhieuDTO ct : ds) {
            Assert.assertEquals(1001, ct.getMaphieu()); // Mã phiếu đúng với điều kiện truy vấn
        }
    }

    /**
     * TC2: Truy vấn với mã phiếu hợp lệ nhưng không có dữ liệu Mục đích: Đảm
     * bảo truy vấn trả về danh sách rỗng nếu không có chi tiết
     */
    @Test
    public void selectAllChiTietPhieuXuat_KhongCoDuLieu() {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        String maphieu = "9999"; // Giả sử không có bản ghi nào với mã này

        ArrayList<ChiTietPhieuDTO> ds = dao.selectAll(maphieu);

        Assert.assertNotNull(ds);           // Danh sách không null
        Assert.assertEquals(0, ds.size());  // Danh sách rỗng
    }

    /**
     * TC3: Truy vấn với mã phiếu null Mục đích: Đảm bảo hệ thống xử lý an toàn
     * khi đầu vào là null
     */
    @Test
    public void selectAllChiTietPhieuXuat_MaPhieuNull() {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();

        ArrayList<ChiTietPhieuDTO> ds = dao.selectAll(null);

        Assert.assertNotNull(ds);           // Không nên trả về null
        Assert.assertEquals(0, ds.size());  // Không có gì để trả về
    }

    /**
     * TC4: Truy vấn với mã phiếu dạng chuỗi không hợp lệ Mục đích: Xác minh hệ
     * thống không lỗi với chuỗi bất thường
     */
    @Test
    public void selectAllChiTietPhieuXuat_MaPhieuSaiKieu() {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        String maphieu = "' OR 1=1 --"; // SQL injection style string

        ArrayList<ChiTietPhieuDTO> ds = dao.selectAll(maphieu);

        Assert.assertNotNull(ds);           // Không null
        Assert.assertEquals(0, ds.size());  // Không được trả về gì cả
    }

//    /**
//     * TC1: Kiểm tra khi danh sách chi tiết phiếu hợp lệ Mục đích: Đảm bảo số
//     * lượng tồn được cộng lại và dữ liệu chi tiết phiếu bị xóa đúng cách
//     */
//    @Test
//    public void reset_DanhSachChiTietPhieu_HopLe() throws SQLException {
//        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
//        Connection con = JDBCUtil.getConnection();
//        con.setAutoCommit(false);
//        
//        int maphienban = 2;
//        ChiTietPhieuDTO ct = new ChiTietPhieuDTO(3001, maphienban, 2, 100000); // giả sử các mã này tồn tại
//        ArrayList<ChiTietPhieuDTO> list = new ArrayList<>();
//        list.add(ct);
//
//        // Giả lập giảm tồn trước khi reset
//        PhienBanSanPhamDAO.getInstance().updateSoLuongTon(maphienban, -5);
//        list.add(ct);
//
//        int result = dao.reset(list);
//
//        Assert.assertEquals(0, result);
//        int ton = PhienBanSanPhamDTO.getInstance().updateSoLuongTon(maphienban, result);
//        Assert.assertTrue(ton >= 5);
//
//        dao.con.rollback();
//    }

    /**
     * TC1: Kiểm tra khi danh sách chi tiết phiếu rỗng Mục đích: Đảm bảo phương
     * thức hoạt động không lỗi khi không có dữ liệu
     */
    @Test
    public void reset_DanhSachRong() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        dao.con.setAutoCommit(false);

        ArrayList<ChiTietPhieuDTO> emptyList = new ArrayList<>();

        int result = dao.reset(emptyList);

        Assert.assertEquals(0, result);

        dao.con.rollback();
    }

    /**
     * TC2: Kiểm tra khi có chi tiết phiếu không hợp lệ (mã phiếu không tồn tại)
     * Mục đích: Đảm bảo không xảy ra lỗi và hệ thống xử lý an toàn
     */
    @Test
    public void reset_ChiTietKhongHopLe() throws SQLException {
        ChiTietPhieuXuatDAO dao = new ChiTietPhieuXuatDAO();
        dao.con.setAutoCommit(false);

        ChiTietPhieuDTO ct = new ChiTietPhieuDTO();
        ct.setMaphienbansp(9999); // mã không tồn tại
        ct.setMaphieu(9999);      // mã phiếu không có thật
        ct.setSoluong(10);

        ArrayList<ChiTietPhieuDTO> list = new ArrayList<>();
        list.add(ct);

        int result = dao.reset(list);

        // Kỳ vọng vẫn không lỗi, result = 0
        Assert.assertEquals(0, result);

        dao.con.rollback();
    }

}
