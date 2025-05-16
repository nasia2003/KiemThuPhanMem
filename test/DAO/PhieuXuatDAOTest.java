/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import DTO.PhieuXuatDTO;
import org.junit.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author MaiLoan
 */

public class PhieuXuatDAOTest {

    /**
     * TC0: Kiểm tra phương thức getInstance()
     * Mục đích: Đảm bảo rằng phương thức `getInstance()` trả về một đối tượng hợp lệ (không phải null) và đúng kiểu.
     */
    @Test
    public void testGetInstance() {
        // Gọi phương thức getInstance()
        PhieuXuatDAO dao = PhieuXuatDAO.getInstance();

        // Kiểm tra nếu đối tượng trả về không phải là null
        Assert.assertNotNull("PhieuXuatDAO instance should not be null", dao);

        // Kiểm tra nếu đối tượng trả về là một thể hiện của PhieuXuatDAO
        Assert.assertTrue("Object should be an instance of PhieuXuatDAO", dao instanceof PhieuXuatDAO);
    }
    /**
     * PX_1: Chèn phiếu xuất hợp lệ
     * Mục đích: Đảm bảo có thể chèn một phiếu xuất hợp lệ vào cơ sở dữ liệu
     */
    @Test
    public void insertPhieuXuat_HopLe() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1001); // Mã phiếu hợp lệ
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(1); // Khách hàng tồn tại
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(1000000); // Tổng tiền hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(px);
        Assert.assertEquals(1, result); // Kết quả mong đợi là 1 (thêm thành công)
        dao.con.rollback(); // Hoàn tác để không thay đổi DB thực
    }

    /**
     * PX_2: Chèn phiếu xuất với tổng tiền = 0
     * Mục đích: Kiểm tra trường hợp tổng tiền bằng 0 khi thêm phiếu xuất
     */
    @Test
    public void insertPhieuXuat_TongTienBang0() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1002); // Mã phiếu hợp lệ
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(0); // Tổng tiền = 0

        dao.con.setAutoCommit(false);
        int result = dao.insert(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (thêm không thành công)
        dao.con.rollback();
    }

    /**
     * PX_3: Chèn phiếu xuất với mã phiếu đã tồn tại
     * Mục đích: Kiểm tra việc chèn phiếu xuất với mã phiếu đã tồn tại trong DB
     */
    @Test
    public void insertPhieuXuat_MaPhieuDaTonTai() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1001); // Mã phiếu đã tồn tại
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(500000); // Tổng tiền hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (Không chèn được vào DB)
        dao.con.rollback();
    }

    /**
     * PX_4: Chèn phiếu xuất với khách hàng không tồn tại
     * Mục đích: Kiểm tra trường hợp khách hàng không tồn tại trong DB
     */
    @Test
    public void insertPhieuXuat_KhachHangKhongTonTai() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1003); // Mã phiếu hợp lệ
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(9999); // Khách hàng không tồn tại
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(2000000); // Tổng tiền hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể thêm do Khách hàng không tồn tại)
        dao.con.rollback();
    }

    /**
     * PX_5: Chèn phiếu xuất với người tạo không tồn tại
     * Mục đích: Kiểm tra trường hợp người tạo không tồn tại trong DB
     */
    @Test
    public void insertPhieuXuat_NguoiTaoKhongTonTai() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1004); // Mã phiếu hợp lệ
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(9999); // Người tạo không tồn tại
        px.setTongTien(1500000); // Tổng tiền hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể thêm do người tạo không tồn tại)
        dao.con.rollback();
    }

    /**
     * PX_6: Chèn phiếu xuất với thời gian là null
     * Mục đích: Kiểm tra trường hợp thời gian tạo phiếu xuất là null
     */
    @Test
    public void insertPhieuXuat_ThoiGianNull() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1005); // Mã phiếu hợp lệ
        px.setThoigiantao(null); // Thời gian null
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(3000000); // Tổng tiền hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 nếu thời gian không hợp lệ
        dao.con.rollback();
    }

    /**
     * PX_7: Chèn phiếu xuất với tổng tiền âm
     * Mục đích: Kiểm tra việc chèn phiếu xuất có tổng tiền âm
     */
    @Test
    public void insertPhieuXuat_TongTienAm() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1006); // Mã phiếu hợp lệ
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(-500000); // Tổng tiền âm (không hợp lệ)

        dao.con.setAutoCommit(false);
        int result = dao.insert(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể thêm vì tổng tiền âm)
        dao.con.rollback();
    }

    /**
     * PX_8: Cập nhật phiếu xuất hợp lệ
     * Mục đích: Đảm bảo rằng phiếu xuất hợp lệ có thể được cập nhật trong cơ sở dữ liệu
     */
    @Test
    public void updatePhieuXuat_HopLe() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1001); // Mã phiếu hợp lệ
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(1500000); // Tổng tiền hợp lệ
        px.setTrangthai(1); // Trạng thái hợp lệ (1: Đang sử dụng)

        dao.con.setAutoCommit(false);
        int result = dao.update(px);
        Assert.assertEquals(1, result); // Kết quả mong đợi là 1 (cập nhật thành công)
        dao.con.rollback(); // Hoàn tác để không thay đổi DB thực
    }

    /**
     * PX_9: Cập nhật phiếu xuất với tổng tiền bằng 0
     * Mục đích: Kiểm tra khả năng cập nhật phiếu xuất với tổng tiền = 0
     */
    @Test
    public void updatePhieuXuat_TongTienBang0() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1002); // Mã phiếu hợp lệ
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(0); // Tổng tiền bằng 0
        px.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (cập nhật không thành công)
        dao.con.rollback();
    }

    /**
     * PX_10: Cập nhật phiếu xuất với mã phiếu không tồn tại
     * Mục đích: Kiểm tra khi mã phiếu không tồn tại trong cơ sở dữ liệu
     */
    @Test
    public void updatePhieuXuat_MaPhieuKhongTonTai() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(9999); // Mã phiếu không tồn tại
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(1000000); // Tổng tiền hợp lệ
        px.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể cập nhật vì mã phiếu không tồn tại)
        dao.con.rollback();
    }

    /**
     * PX_11: Cập nhật phiếu xuất với khách hàng không tồn tại
     * Mục đích: Kiểm tra khi khách hàng không tồn tại trong cơ sở dữ liệu
     */
    @Test
    public void updatePhieuXuat_KhachHangKhongTonTai() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1003); // Mã phiếu hợp lệ
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(9999); // Khách hàng không tồn tại
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(1500000); // Tổng tiền hợp lệ
        px.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể cập nhật vì khách hàng không tồn tại)
        dao.con.rollback();
    }

    /**
     * PX_12: Cập nhật phiếu xuất với người tạo không tồn tại
     * Mục đích: Kiểm tra khi người tạo không tồn tại trong cơ sở dữ liệu
     */
    @Test
    public void updatePhieuXuat_NguoiTaoKhongTonTai() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1004); // Mã phiếu hợp lệ
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(9999); // Người tạo không tồn tại
        px.setTongTien(2000000); // Tổng tiền hợp lệ
        px.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể cập nhật vì người tạo không tồn tại)
        dao.con.rollback();
    }

    /**
     * PX_13: Cập nhật phiếu xuất với tổng tiền âm
     * Mục đích: Kiểm tra khả năng cập nhật phiếu xuất với tổng tiền âm
     */
    @Test
    public void updatePhieuXuat_TongTienAm() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1004); // Mã phiếu hợp lệ
        px.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(-1000); // Tổng tiền âm (không hợp lệ)
        px.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể cập nhật vì tổng tiền âm)
        dao.con.rollback();
    }

    /**
     * PX_14: Cập nhật phiếu xuất với thời gian tạo null
     * Mục đích: Kiểm tra khả năng cập nhật phiếu xuất với thời gian tạo là null
     */
    @Test
    public void updatePhieuXuat_ThoiGianNull() throws SQLException {
        PhieuXuatDTO px = new PhieuXuatDTO();
        PhieuXuatDAO dao = new PhieuXuatDAO();

        px.setMaphieu(1006); // Mã phiếu hợp lệ
        px.setThoigiantao(null); // Thời gian tạo phiếu xuất null
        px.setMakh(1); // Khách hàng hợp lệ
        px.setManguoitao(1); // Người tạo hợp lệ
        px.setTongTien(1000000); // Tổng tiền hợp lệ
        px.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(px);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể cập nhật vì thời gian tạo null)
        dao.con.rollback();
    }

    /**
     * PX_15: Xóa phiếu xuất hợp lệ
     * Mục đích: Đảm bảo rằng phiếu xuất có mã hợp lệ sẽ được đánh dấu trạng thái là 0 (xóa thành công).
     */
    @Test
    public void deletePhieuXuat_HopLe() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        String maphieu = "1001"; // Mã phiếu hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(1, result); // Kết quả mong đợi là 1 (xóa thành công)
        dao.con.rollback();
    }

    /**
     * PX_16: Xóa phiếu xuất với mã phiếu không tồn tại
     * Mục đích: Kiểm tra khi mã phiếu không tồn tại trong cơ sở dữ liệu
     */
    @Test
    public void deletePhieuXuat_MaPhieuKhongTonTai() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        String maphieu = "9999"; // Mã phiếu không tồn tại trong cơ sở dữ liệu

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không có bản ghi nào bị xóa)
        dao.con.rollback();
    }

    /**
     * PX_17: Xóa phiếu xuất khi phiếu đã bị xóa (trangthai = 0)
     * Mục đích: Kiểm tra trường hợp khi phiếu xuất đã bị xóa rồi.
     */
    @Test
    public void deletePhieuXuat_PhieuDaXoa() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        String maphieu = "1002"; // Mã phiếu đã bị xóa (trangthai = 0)

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không có bản ghi nào bị xóa)
        dao.con.rollback();
    }

    /**
     * PX_18: Xóa phiếu xuất với mã phiếu là null
     * Mục đích: Kiểm tra khi mã phiếu là null, đảm bảo không xảy ra lỗi.
     */
    @Test
    public void deletePhieuXuat_MaPhieuNull() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        String maphieu = null; // Mã phiếu là null

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không có bản ghi nào bị xóa)
        dao.con.rollback();
    }

    /**
     * PX_19: Xóa phiếu xuất với chuỗi mã phiếu rỗng
     * Mục đích: Kiểm tra khi mã phiếu là chuỗi rỗng, đảm bảo không có bản ghi nào bị xóa.
     */
    @Test
    public void deletePhieuXuat_MaPhieuRong() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        String maphieu = ""; // Mã phiếu là chuỗi rỗng

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không có bản ghi nào bị xóa)
        dao.con.rollback();
    }


    /**
     * PX_20: Lấy tất cả phiếu xuất
     * Mục đích: Đảm bảo rằng khi có các phiếu xuất trong cơ sở dữ liệu, phương thức trả về danh sách phiếu xuất đúng.
     */
    @Test
    public void selectAll_PhieuXuatHopLe() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();

        // Giả sử cơ sở dữ liệu có một số phiếu xuất hợp lệ
        ArrayList<PhieuXuatDTO> result = dao.selectAll();

        // Kiểm tra rằng kết quả không rỗng
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0); // Kết quả mong đợi có ít nhất 1 phiếu xuất trong danh sách
    }

    /**
     * PX_21: Lấy tất cả phiếu xuất khi không có phiếu xuất nào
     * Mục đích: Kiểm tra trường hợp cơ sở dữ liệu không có phiếu xuất nào, phương thức phải trả về danh sách rỗng.
     */
    @Test
    public void selectAll_KhongCoPhieuXuat() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();

        // Giả sử cơ sở dữ liệu không có phiếu xuất nào
        // Cần chuẩn bị cơ sở dữ liệu sao cho không có phiếu xuất trước khi chạy test này.
        ArrayList<PhieuXuatDTO> result = dao.selectAll();

        // Kiểm tra rằng kết quả là danh sách rỗng
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size()); // Kết quả mong đợi là danh sách rỗng
    }

    /**
     * PX_22: Lấy tất cả phiếu xuất có dữ liệu hợp lệ, kiểm tra giá trị trả về
     * Mục đích: Đảm bảo rằng các phiếu xuất được trả về đúng với dữ liệu trong cơ sở dữ liệu.
     */
    @Test
    public void selectAll_KiemTraGiaTri() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();

        // Giả sử cơ sở dữ liệu có ít nhất 1 phiếu xuất hợp lệ
        ArrayList<PhieuXuatDTO> result = dao.selectAll();

        // Lấy thông tin của phiếu xuất đầu tiên trong danh sách
        PhieuXuatDTO firstPhieuXuat = result.get(0);

        // Kiểm tra giá trị của phiếu xuất đầu tiên
        Assert.assertNotNull(firstPhieuXuat);
        Assert.assertTrue(firstPhieuXuat.getMaphieu() > 0); // Mã phiếu xuất phải là số dương
        Assert.assertNotNull(firstPhieuXuat.getThoigiantao()); // Thời gian tạo phiếu xuất không được null
        Assert.assertTrue(firstPhieuXuat.getTongTien() > 0); // Tổng tiền phải là số dương
    }

    /**
     * PX_23: Lấy tất cả phiếu xuất với dữ liệu có giá trị null (thử nghiệm dữ liệu bị lỗi)
     * Mục đích: Kiểm tra trường hợp dữ liệu phiếu xuất có giá trị null, phải kiểm tra kỹ lưỡng.
     */
    @Test
    public void selectAll_PhieuXuatDuLieuNull() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();

        // Giả sử cơ sở dữ liệu có một phiếu xuất có một số giá trị null
        ArrayList<PhieuXuatDTO> result = dao.selectAll();

        // Kiểm tra rằng danh sách không bị lỗi khi có giá trị null trong cơ sở dữ liệu
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);

        for (PhieuXuatDTO phieuNhap : result) {
            Assert.assertNotNull(phieuNhap.getThoigiantao()); // Kiểm tra xem thời gian tạo không phải là null
            Assert.assertTrue(phieuNhap.getTongTien() > 0); // Kiểm tra tổng tiền phải là số dương
        }
    }

    /**
     * PX_24: Lấy phiếu xuất hợp lệ theo ID
     * Mục đích: Đảm bảo rằng khi có phiếu xuất với mã hợp lệ trong cơ sở dữ liệu, phương thức trả về đối tượng `PhieuXuatDTO` đúng.
     */
    @Test
    public void selectById_PhieuXuatHopLe() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        String validId = "1"; // Giả sử phiếu xuất với ID 1 tồn tại trong cơ sở dữ liệu

        PhieuXuatDTO result = dao.selectById(validId);

        // Kiểm tra rằng kết quả không null
        Assert.assertNotNull(result);

        // Kiểm tra các giá trị trong đối tượng PhieuXuatDTO
        Assert.assertEquals(validId, String.valueOf(result.getMaphieu()));
        Assert.assertTrue(result.getTongTien() > 0);  // Kiểm tra tổng tiền phải lớn hơn 0
        Assert.assertNotNull(result.getThoigiantao());  // Kiểm tra thời gian tạo không null
    }

    /**
     * PX_25: Lấy phiếu xuất theo ID không tồn tại
     * Mục đích: Kiểm tra trường hợp khi không có phiếu xuất nào với mã hợp lệ trong cơ sở dữ liệu, phương thức trả về null.
     */
    @Test
    public void selectById_KhongTimThayPhieuXuat() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        String invalidId = "9999"; // Giả sử phiếu xuất với ID 9999 không tồn tại trong cơ sở dữ liệu

        PhieuXuatDTO result = dao.selectById(invalidId);

        // Kiểm tra kết quả trả về là null
        Assert.assertNull(result);
    }

    /**
     * PX_26: Lấy phiếu xuất theo ID có dữ liệu null
     * Mục đích: Kiểm tra trường hợp dữ liệu của phiếu xuất có thể chứa giá trị null, phương thức phải xử lý tốt các giá trị null.
     */
    @Test
    public void selectById_PhieuXuatVoiDuLieuNull() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        String validIdWithNullData = "2"; // Giả sử phiếu xuất với ID 2 có các giá trị null trong cơ sở dữ liệu

        PhieuXuatDTO result = dao.selectById(validIdWithNullData);

        // Kiểm tra kết quả không null
        Assert.assertNotNull(result);

        // Kiểm tra xem giá trị của các trường có null không
        Assert.assertNotNull(result.getThoigiantao()); // Thời gian tạo không được null
        Assert.assertTrue(result.getTongTien() > 0); // Tổng tiền dương
    }

    /**
     * PX_27: Lấy phiếu xuất theo ID khi có lỗi kết nối
     * Mục đích: Kiểm tra khi có lỗi kết nối cơ sở dữ liệu, phương thức trả về null hoặc xử lý lỗi đúng cách.
     */
    @Test
    public void selectById_LoiKetNoi() throws SQLException {
        // Giả lập lỗi kết nối cơ sở dữ liệu bằng cách thiết lập kết nối sai hoặc không tồn tại.
        PhieuXuatDAO dao = new PhieuXuatDAO();

        // Giả lập lỗi kết nối hoặc các tình huống ngoại lệ khác
        dao.con = null; // Tắt kết nối để gây lỗi

        // Thực thi phương thức selectById khi không có kết nối
        PhieuXuatDTO result = dao.selectById("1");

        // Kiểm tra kết quả, mong đợi trả về null do lỗi kết nối
        Assert.assertNull(result);
    }
//
//    /**
//     * PX_28: Kiểm tra phiếu xuất theo ID và kiểm tra đúng thông tin
//     * Mục đích: Đảm bảo khi lấy phiếu xuất theo ID, thông tin của nó phải chính xác.
//     */
//    @Test
//    public void selectById_KiemTraGiaTri() throws SQLException {
//        PhieuXuatDAO dao = new PhieuXuatDAO();
//        String validId = "1"; // Giả sử phiếu xuất với ID 1 tồn tại trong cơ sở dữ liệu
//
//        PhieuXuatDTO result = dao.selectById(validId);
//
//        // Kiểm tra các giá trị trả về phải chính xác
//        Assert.assertNotNull(result);
//        Assert.assertEquals(validId, String.valueOf(result.getMaphieu())); // Kiểm tra mã phiếu xuất
//        Assert.assertTrue(result.getTongTien() > 0); // Kiểm tra tổng tiền
//        Assert.assertNotNull(result.getThoigiantao()); // Kiểm tra thời gian tạo phiếu xuất
//    }
//
    /**
     * PX_28: Kiểm tra hủy phiếu xuất hợp lệ
     * Mục đích: Đảm bảo rằng khi hủy phiếu xuất hợp lệ, các thay đổi liên quan đến các chi tiết sản phẩm, phiếu xuất được thực hiện đúng.
     */
    @Test
    public void cancelPhieuXuat_HopLe() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        int maphieu = 1001; // Mã phiếu xuất mẫu hợp lệ

        // Giả lập hoặc chèn dữ liệu vào cơ sở dữ liệu để tạo một phiếu xuất hợp lệ với chi tiết sản phẩm

        // Gọi phương thức cancel
        PhieuXuatDTO result = dao.cancel(maphieu);

        // Kiểm tra nếu kết quả trả về là 1 (hủy thành công)
        Assert.assertEquals(1, result);

        // Kiểm tra xem các bản ghi liên quan trong các bảng đã bị xóa hoặc cập nhật đúng (ChiTietPhieuXuat, ChiTietSanPham, PhienBanSanPham)
        // Có thể viết các truy vấn kiểm tra lại trong cơ sở dữ liệu để đảm bảo rằng các bản ghi đã được xóa hoặc cập nhật đúng
    }

    /**
     * PX_29: Kiểm tra hủy phiếu khi phiếu xuất không tồn tại
     * Mục đích: Đảm bảo rằng nếu phiếu xuất không tồn tại, không có thay đổi nào được thực hiện và kết quả trả về là 0.
     */
    @Test
    public void cancelPhieuXuat_KhongTonTai() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        int maphieu = 9999; // Mã phiếu xuất không tồn tại trong cơ sở dữ liệu

        // Gọi phương thức cancelPhieuXuat
        PhieuXuatDTO result = dao.cancel(maphieu);

        // Kiểm tra nếu kết quả trả về là 0 (không có gì thay đổi vì không tìm thấy phiếu xuất)
        Assert.assertEquals(0, result);
    }

    /**
     * PX_30: Kiểm tra lỗi khi có sự cố trong quá trình truy vấn cơ sở dữ liệu
     * Mục đích: Kiểm tra nếu có lỗi trong quá trình thực hiện câu lệnh SQL (chẳng hạn lỗi kết nối DB), phương thức phải xử lý đúng và trả về 0.
     */
    @Test
    public void cancelPhieuXuat_LoiTruyVan() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        int maphieu = 1002; // Mã phiếu xuất hợp lệ, nhưng giả lập lỗi truy vấn

        // Giả lập lỗi truy vấn cơ sở dữ liệu (ví dụ lỗi kết nối DB)

        // Gọi phương thức cancelPhieuXuat
        PhieuXuatDTO result = dao.cancel(maphieu);

        // Kiểm tra nếu kết quả trả về là 0 (không thực hiện được hành động do lỗi)
        Assert.assertEquals(0, result);
    }

    /**
     * PX_31: Kiểm tra khi khách hàng có phiếu xuất
     * Mục đích: Đảm bảo phương thức trả về đúng danh sách phiếu xuất của khách hàng
     */
    @Test
    public void selectAllofKH_CoPhieuXuat() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        dao.con.setAutoCommit(false);

        int makh = 2001; // mã khách hàng giả lập

        // Insert 1 phiếu xuất mẫu trước để đảm bảo có dữ liệu
        PhieuXuatDTO px = new PhieuXuatDTO();
        px.setMakh(makh);
        px.setMaphieu(9999); // mã giả định
        px.setManguoitao(100);
        px.setThoigiantao(new Timestamp(System.currentTimeMillis()));
        px.setTongTien(1000000L);
        px.setTrangthai(1);
        dao.insert(px);

        ArrayList<PhieuXuatDTO> list = dao.selectAllofKH(makh);

        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
        Assert.assertEquals(makh, list.get(0).getMakh());

        dao.con.rollback();
    }

    /**
     * PX_32: Kiểm tra khi khách hàng không có phiếu xuất
     * Mục đích: Phương thức phải trả về danh sách rỗng
     */
    @Test
    public void selectAllofKH_KhongCoPhieuXuat() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        dao.con.setAutoCommit(false);

        int makh = 9999; // giả sử chưa có phiếu xuất nào

        ArrayList<PhieuXuatDTO> list = dao.selectAllofKH(makh);

        Assert.assertNull(list);
        Assert.assertTrue(list.isEmpty());

        dao.con.rollback();
    }

    /**
     * PX_33: Kiểm tra khi truyền mã khách hàng không hợp lệ (âm)
     * Mục đích: Tránh lỗi SQL hoặc kết quả sai
     */
    @Test
    public void selectAllofKH_MaKHKhongHopLe() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();
        dao.con.setAutoCommit(false);

        int makh = -1;

        ArrayList<PhieuXuatDTO> list = dao.selectAllofKH(makh);

        Assert.assertNull(list);
        Assert.assertTrue(list.isEmpty());

        dao.con.rollback();
    }

    /**
     * PX_34: Kiểm tra lấy giá trị Auto Increment hợp lệ
     * Mục đích: Đảm bảo rằng phương thức `getAutoIncrement()` trả về giá trị `AUTO_INCREMENT` chính xác từ cơ sở dữ liệu.
     */
    @Test
    public void getAutoIncrement_HopLe() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();

        // Gọi phương thức getAutoIncrement
        int result = dao.getAutoIncrement();

        // Kiểm tra kết quả trả về nếu giá trị auto increment hợp lệ (kết quả cần >= 0)
        Assert.assertTrue(result >= 0);
    }

    /**
     * PX_35: Kiểm tra khi không có dữ liệu trong bảng
     * Mục đích: Đảm bảo rằng nếu không có dữ liệu trong bảng, phương thức trả về giá trị -1.
     */
    @Test
    public void getAutoIncrement_KhongCoDuLieu() throws SQLException {
        PhieuXuatDAO dao = new PhieuXuatDAO();

        // Giả lập trạng thái không có dữ liệu trong bảng `phieunhap`

        // Gọi phương thức getAutoIncrement
        int result = dao.getAutoIncrement();

        // Kiểm tra nếu giá trị trả về là -1 khi không có dữ liệu
        Assert.assertEquals(-1, result);
    }


}
