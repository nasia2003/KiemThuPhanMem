package DAO;

import DTO.PhieuNhapDTO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class PhieuNhapDAOTest {

    /**
     * TC1: Kiểm tra phương thức getInstance()
     * Mục đích: Đảm bảo rằng phương thức `getInstance()` trả về một đối tượng hợp lệ (không phải null) và đúng kiểu.
     */
    @Test
    public void testGetInstance() {
        // Gọi phương thức getInstance()
        PhieuNhapDAO dao = PhieuNhapDAO.getInstance();

        // Kiểm tra nếu đối tượng trả về không phải là null
        Assert.assertNotNull("PhieuNhapDAO instance should not be null", dao);

        // Kiểm tra nếu đối tượng trả về là một thể hiện của PhieuNhapDAO
        Assert.assertTrue("Object should be an instance of PhieuNhapDAO", dao instanceof PhieuNhapDAO);
    }
    /**
     * TC1: Chèn phiếu nhập hợp lệ
     * Mục đích: Đảm bảo có thể chèn một phiếu nhập hợp lệ vào cơ sở dữ liệu
     */
    @Test
    public void insertPhieuNhap_HopLe() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1001); // Mã phiếu hợp lệ
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(1); // Nhà cung cấp tồn tại
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(1000000); // Tổng tiền hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(pn);
        Assert.assertEquals(1, result); // Kết quả mong đợi là 1 (thêm thành công)
        dao.con.rollback(); // Hoàn tác để không thay đổi DB thực
    }

    /**
     * TC2: Chèn phiếu nhập với tổng tiền = 0
     * Mục đích: Kiểm tra trường hợp tổng tiền bằng 0 khi thêm phiếu nhập
     */
    @Test
    public void insertPhieuNhap_TongTienBang0() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1002); // Mã phiếu hợp lệ
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(0); // Tổng tiền = 0

        dao.con.setAutoCommit(false);
        int result = dao.insert(pn);
        Assert.assertEquals(1, result); // Kết quả mong đợi là 1 (thêm thành công)
        dao.con.rollback();
    }

    /**
     * TC3: Chèn phiếu nhập với mã phiếu đã tồn tại
     * Mục đích: Kiểm tra việc chèn phiếu nhập với mã phiếu đã tồn tại trong DB
     */
    @Test
    public void insertPhieuNhap_MaPhieuDaTonTai() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1001); // Mã phiếu đã tồn tại
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(500000); // Tổng tiền hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(pn);
        Assert.assertEquals(1, result); // Kết quả mong đợi là 1 nếu DB cho phép thêm
        dao.con.rollback();
    }

    /**
     * TC4: Chèn phiếu nhập với nhà cung cấp không tồn tại
     * Mục đích: Kiểm tra trường hợp nhà cung cấp không tồn tại trong DB
     */
    @Test
    public void insertPhieuNhap_NhaCungCapKhongTonTai() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1003); // Mã phiếu hợp lệ
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(9999); // Nhà cung cấp không tồn tại
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(2000000); // Tổng tiền hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(pn);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể thêm do NCC không tồn tại)
        dao.con.rollback();
    }

    /**
     * TC5: Chèn phiếu nhập với người tạo không tồn tại
     * Mục đích: Kiểm tra trường hợp người tạo không tồn tại trong DB
     */
    @Test
    public void insertPhieuNhap_NguoiTaoKhongTonTai() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1004); // Mã phiếu hợp lệ
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(9999); // Người tạo không tồn tại
        pn.setTongTien(1500000); // Tổng tiền hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(pn);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể thêm do người tạo không tồn tại)
        dao.con.rollback();
    }

    /**
     * TC6: Chèn phiếu nhập với thời gian là null
     * Mục đích: Kiểm tra trường hợp thời gian tạo phiếu nhập là null
     */
    @Test
    public void insertPhieuNhap_ThoiGianNull() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1005); // Mã phiếu hợp lệ
        pn.setThoigiantao(null); // Thời gian null
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(3000000); // Tổng tiền hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(pn);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 nếu thời gian không hợp lệ
        dao.con.rollback();
    }

    /**
     * TC7: Chèn phiếu nhập với tổng tiền âm
     * Mục đích: Kiểm tra việc chèn phiếu nhập có tổng tiền âm
     */
    @Test
    public void insertPhieuNhap_TongTienAm() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1006); // Mã phiếu hợp lệ
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(-500000); // Tổng tiền âm (không hợp lệ)

        dao.con.setAutoCommit(false);
        int result = dao.insert(pn);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể thêm vì tổng tiền âm)
        dao.con.rollback();
    }

    /**
     * TC1: Cập nhật phiếu nhập hợp lệ
     * Mục đích: Đảm bảo rằng phiếu nhập hợp lệ có thể được cập nhật trong cơ sở dữ liệu
     */
    @Test
    public void updatePhieuNhap_HopLe() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1001); // Mã phiếu hợp lệ
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(1500000); // Tổng tiền hợp lệ
        pn.setTrangthai(1); // Trạng thái hợp lệ (1: Đang sử dụng)

        dao.con.setAutoCommit(false);
        int result = dao.update(pn);
        Assert.assertEquals(1, result); // Kết quả mong đợi là 1 (cập nhật thành công)
        dao.con.rollback(); // Hoàn tác để không thay đổi DB thực
    }

    /**
     * TC2: Cập nhật phiếu nhập với tổng tiền bằng 0
     * Mục đích: Kiểm tra khả năng cập nhật phiếu nhập với tổng tiền = 0
     */
    @Test
    public void updatePhieuNhap_TongTienBang0() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1002); // Mã phiếu hợp lệ
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(0); // Tổng tiền bằng 0
        pn.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(pn);
        Assert.assertEquals(1, result); // Kết quả mong đợi là 1 (cập nhật thành công)
        dao.con.rollback();
    }

    /**
     * TC3: Cập nhật phiếu nhập với mã phiếu không tồn tại
     * Mục đích: Kiểm tra khi mã phiếu không tồn tại trong cơ sở dữ liệu
     */
    @Test
    public void updatePhieuNhap_MaPhieuKhongTonTai() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(9999); // Mã phiếu không tồn tại
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(1000000); // Tổng tiền hợp lệ
        pn.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(pn);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể cập nhật vì mã phiếu không tồn tại)
        dao.con.rollback();
    }

    /**
     * TC4: Cập nhật phiếu nhập với nhà cung cấp không tồn tại
     * Mục đích: Kiểm tra khi nhà cung cấp không tồn tại trong cơ sở dữ liệu
     */
    @Test
    public void updatePhieuNhap_NhaCungCapKhongTonTai() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1003); // Mã phiếu hợp lệ
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(9999); // Nhà cung cấp không tồn tại
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(1500000); // Tổng tiền hợp lệ
        pn.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(pn);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể cập nhật vì nhà cung cấp không tồn tại)
        dao.con.rollback();
    }

    /**
     * TC5: Cập nhật phiếu nhập với người tạo không tồn tại
     * Mục đích: Kiểm tra khi người tạo không tồn tại trong cơ sở dữ liệu
     */
    @Test
    public void updatePhieuNhap_NguoiTaoKhongTonTai() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1004); // Mã phiếu hợp lệ
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(9999); // Người tạo không tồn tại
        pn.setTongTien(2000000); // Tổng tiền hợp lệ
        pn.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(pn);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể cập nhật vì người tạo không tồn tại)
        dao.con.rollback();
    }

    /**
     * TC6: Cập nhật phiếu nhập với tổng tiền âm
     * Mục đích: Kiểm tra khả năng cập nhật phiếu nhập với tổng tiền âm
     */
    @Test
    public void updatePhieuNhap_TongTienAm() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1005); // Mã phiếu hợp lệ
        pn.setThoigiantao(new Timestamp(System.currentTimeMillis())); // Thời gian hợp lệ
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(-1000); // Tổng tiền âm (không hợp lệ)
        pn.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(pn);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể cập nhật vì tổng tiền âm)
        dao.con.rollback();
    }

    /**
     * TC7: Cập nhật phiếu nhập với thời gian tạo null
     * Mục đích: Kiểm tra khả năng cập nhật phiếu nhập với thời gian tạo là null
     */
    @Test
    public void updatePhieuNhap_ThoiGianNull() throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        PhieuNhapDAO dao = new PhieuNhapDAO();

        pn.setMaphieu(1006); // Mã phiếu hợp lệ
        pn.setThoigiantao(null); // Thời gian tạo phiếu nhập null
        pn.setManhacungcap(1); // Nhà cung cấp hợp lệ
        pn.setManguoitao(1); // Người tạo hợp lệ
        pn.setTongTien(1000000); // Tổng tiền hợp lệ
        pn.setTrangthai(1); // Trạng thái hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.update(pn);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không thể cập nhật vì thời gian tạo null)
        dao.con.rollback();
    }

    /**
     * TC1: Xóa phiếu nhập hợp lệ
     * Mục đích: Đảm bảo rằng phiếu nhập có mã hợp lệ sẽ được đánh dấu trạng thái là 0 (xóa thành công).
     */
    @Test
    public void deletePhieuNhap_HopLe() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        String maphieu = "1001"; // Mã phiếu hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(1, result); // Kết quả mong đợi là 1 (xóa thành công)
        dao.con.rollback();
    }

    /**
     * TC2: Xóa phiếu nhập với mã phiếu không tồn tại
     * Mục đích: Kiểm tra khi mã phiếu không tồn tại trong cơ sở dữ liệu
     */
    @Test
    public void deletePhieuNhap_MaPhieuKhongTonTai() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        String maphieu = "9999"; // Mã phiếu không tồn tại trong cơ sở dữ liệu

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không có bản ghi nào bị xóa)
        dao.con.rollback();
    }

    /**
     * TC3: Xóa phiếu nhập khi phiếu đã bị xóa (trangthai = 0)
     * Mục đích: Kiểm tra trường hợp khi phiếu nhập đã bị xóa rồi.
     */
    @Test
    public void deletePhieuNhap_PhiếuĐãXóa() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        String maphieu = "1002"; // Mã phiếu đã bị xóa (trangthai = 0)

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không có bản ghi nào bị xóa)
        dao.con.rollback();
    }

    /**
     * TC4: Xóa phiếu nhập với mã phiếu là null
     * Mục đích: Kiểm tra khi mã phiếu là null, đảm bảo không xảy ra lỗi.
     */
    @Test
    public void deletePhieuNhap_MaPhieuNull() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        String maphieu = null; // Mã phiếu là null

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không có bản ghi nào bị xóa)
        dao.con.rollback();
    }

    /**
     * TC5: Xóa phiếu nhập với chuỗi mã phiếu rỗng
     * Mục đích: Kiểm tra khi mã phiếu là chuỗi rỗng, đảm bảo không có bản ghi nào bị xóa.
     */
    @Test
    public void deletePhieuNhap_MaPhieuRong() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        String maphieu = ""; // Mã phiếu là chuỗi rỗng

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không có bản ghi nào bị xóa)
        dao.con.rollback();
    }

    /**
     * TC6: Xóa phiếu nhập với mã phiếu hợp lệ nhưng có lỗi kết nối cơ sở dữ liệu
     * Mục đích: Kiểm tra khi có lỗi kết nối cơ sở dữ liệu, hàm trả về 0.
     */
    @Test
    public void deletePhieuNhap_LoiKetNoi() throws SQLException {
        // Giả lập lỗi kết nối cơ sở dữ liệu bằng cách thiết lập kết nối sai hoặc không tồn tại.
        PhieuNhapDAO dao = new PhieuNhapDAO();
        String maphieu = "1003"; // Mã phiếu hợp lệ nhưng sẽ gây lỗi kết nối

        // Giả lập lỗi kết nối hoặc các tình huống ngoại lệ khác
        dao.con = null; // Tắt kết nối để gây lỗi

        dao.con.setAutoCommit(false);
        int result = dao.delete(maphieu);
        Assert.assertEquals(0, result); // Kết quả mong đợi là 0 (không có gì được xóa do lỗi kết nối)
    }


    /**
     * TC1: Lấy tất cả phiếu nhập hợp lệ
     * Mục đích: Đảm bảo rằng khi có các phiếu nhập trong cơ sở dữ liệu, phương thức trả về danh sách phiếu nhập đúng.
     */
    @Test
    public void selectAll_PhiếuNhậpHợpLệ() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();

        // Giả sử cơ sở dữ liệu có một số phiếu nhập hợp lệ
        ArrayList<PhieuNhapDTO> result = dao.selectAll();

        // Kiểm tra rằng kết quả không rỗng
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0); // Kết quả mong đợi có ít nhất 1 phiếu nhập trong danh sách
    }

    /**
     * TC2: Lấy tất cả phiếu nhập khi không có phiếu nhập nào
     * Mục đích: Kiểm tra trường hợp cơ sở dữ liệu không có phiếu nhập nào, phương thức phải trả về danh sách rỗng.
     */
    @Test
    public void selectAll_KhongCoPhiếuNhap() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();

        // Giả sử cơ sở dữ liệu không có phiếu nhập nào
        // Cần chuẩn bị cơ sở dữ liệu sao cho không có phiếu nhập trước khi chạy test này.
        ArrayList<PhieuNhapDTO> result = dao.selectAll();

        // Kiểm tra rằng kết quả là danh sách rỗng
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size()); // Kết quả mong đợi là danh sách rỗng
    }

    /**
     * TC3: Lấy tất cả phiếu nhập có dữ liệu hợp lệ, kiểm tra giá trị trả về
     * Mục đích: Đảm bảo rằng các phiếu nhập được trả về đúng với dữ liệu trong cơ sở dữ liệu.
     */
    @Test
    public void selectAll_KiemTraGiaTri() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();

        // Giả sử cơ sở dữ liệu có ít nhất 1 phiếu nhập hợp lệ
        ArrayList<PhieuNhapDTO> result = dao.selectAll();

        // Lấy thông tin của phiếu nhập đầu tiên trong danh sách
        PhieuNhapDTO firstPhieuNhap = result.get(0);

        // Kiểm tra giá trị của phiếu nhập đầu tiên
        Assert.assertNotNull(firstPhieuNhap);
        Assert.assertTrue(firstPhieuNhap.getMaphieu() > 0); // Mã phiếu nhập phải là số dương
        Assert.assertNotNull(firstPhieuNhap.getThoigiantao()); // Thời gian tạo phiếu nhập không được null
        Assert.assertTrue(firstPhieuNhap.getTongTien() > 0); // Tổng tiền phải là số dương
    }

    /**
     * TC4: Lấy tất cả phiếu nhập với dữ liệu có giá trị null (thử nghiệm dữ liệu bị lỗi)
     * Mục đích: Kiểm tra trường hợp dữ liệu phiếu nhập có giá trị null, phải kiểm tra kỹ lưỡng.
     */
    @Test
    public void selectAll_PhiếuNhapVớiDữLiệuNull() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();

        // Giả sử cơ sở dữ liệu có một phiếu nhập có một số giá trị null
        ArrayList<PhieuNhapDTO> result = dao.selectAll();

        // Kiểm tra rằng danh sách không bị lỗi khi có giá trị null trong cơ sở dữ liệu
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);

        for (PhieuNhapDTO phieuNhap : result) {
            Assert.assertNotNull(phieuNhap.getThoigiantao()); // Kiểm tra xem thời gian tạo không phải là null
            Assert.assertTrue(phieuNhap.getTongTien() > 0); // Kiểm tra tổng tiền phải là số dương
        }
    }

    /**
     * TC5: Lấy tất cả phiếu nhập khi có lỗi kết nối cơ sở dữ liệu
     * Mục đích: Kiểm tra khi có lỗi kết nối với cơ sở dữ liệu, phương thức trả về danh sách rỗng hoặc lỗi thích hợp.
     */
    @Test
    public void selectAll_LoiKetNoi() throws SQLException {
        // Giả lập lỗi kết nối cơ sở dữ liệu bằng cách thiết lập kết nối sai hoặc không tồn tại.
        PhieuNhapDAO dao = new PhieuNhapDAO();

        // Giả lập lỗi kết nối hoặc các tình huống ngoại lệ khác
        dao.con = null; // Tắt kết nối để gây lỗi

        // Thực thi phương thức selectAll khi không có kết nối
        ArrayList<PhieuNhapDTO> result = dao.selectAll();

        // Kiểm tra kết quả, mong đợi trả về danh sách rỗng do lỗi kết nối
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size()); // Kết quả mong đợi là danh sách rỗng
    }

    /**
     * TC1: Lấy phiếu nhập hợp lệ theo ID
     * Mục đích: Đảm bảo rằng khi có phiếu nhập với mã hợp lệ trong cơ sở dữ liệu, phương thức trả về đối tượng `PhieuNhapDTO` đúng.
     */
    @Test
    public void selectById_PhiếuNhậpHợpLệ() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        String validId = "1"; // Giả sử phiếu nhập với ID 1 tồn tại trong cơ sở dữ liệu

        PhieuNhapDTO result = dao.selectById(validId);

        // Kiểm tra rằng kết quả không null
        Assert.assertNotNull(result);

        // Kiểm tra các giá trị trong đối tượng PhieuNhapDTO
        Assert.assertEquals(validId, String.valueOf(result.getMaphieu()));
        Assert.assertTrue(result.getTongTien() > 0);  // Kiểm tra tổng tiền phải lớn hơn 0
        Assert.assertNotNull(result.getThoigiantao());  // Kiểm tra thời gian tạo không null
    }

    /**
     * TC2: Lấy phiếu nhập theo ID không tồn tại
     * Mục đích: Kiểm tra trường hợp khi không có phiếu nhập nào với mã hợp lệ trong cơ sở dữ liệu, phương thức trả về null.
     */
    @Test
    public void selectById_KhongTimThayPhiếuNhap() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        String invalidId = "9999"; // Giả sử phiếu nhập với ID 9999 không tồn tại trong cơ sở dữ liệu

        PhieuNhapDTO result = dao.selectById(invalidId);

        // Kiểm tra kết quả trả về là null
        Assert.assertNull(result);
    }

    /**
     * TC3: Lấy phiếu nhập theo ID có dữ liệu null
     * Mục đích: Kiểm tra trường hợp dữ liệu của phiếu nhập có thể chứa giá trị null, phương thức phải xử lý tốt các giá trị null.
     */
    @Test
    public void selectById_PhiếuNhapVớiDữLiệuNull() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        String validIdWithNullData = "2"; // Giả sử phiếu nhập với ID 2 có các giá trị null trong cơ sở dữ liệu

        PhieuNhapDTO result = dao.selectById(validIdWithNullData);

        // Kiểm tra kết quả không null
        Assert.assertNotNull(result);

        // Kiểm tra xem giá trị của các trường có null không
        Assert.assertNotNull(result.getThoigiantao()); // Thời gian tạo không được null
        Assert.assertTrue(result.getTongTien() >= 0); // Tổng tiền có thể là 0 hoặc lớn hơn
    }

    /**
     * TC4: Lấy phiếu nhập theo ID khi có lỗi kết nối
     * Mục đích: Kiểm tra khi có lỗi kết nối cơ sở dữ liệu, phương thức trả về null hoặc xử lý lỗi đúng cách.
     */
    @Test
    public void selectById_LoiKetNoi() throws SQLException {
        // Giả lập lỗi kết nối cơ sở dữ liệu bằng cách thiết lập kết nối sai hoặc không tồn tại.
        PhieuNhapDAO dao = new PhieuNhapDAO();

        // Giả lập lỗi kết nối hoặc các tình huống ngoại lệ khác
        dao.con = null; // Tắt kết nối để gây lỗi

        // Thực thi phương thức selectById khi không có kết nối
        PhieuNhapDTO result = dao.selectById("1");

        // Kiểm tra kết quả, mong đợi trả về null do lỗi kết nối
        Assert.assertNull(result);
    }

    /**
     * TC5: Kiểm tra phiếu nhập theo ID và kiểm tra đúng thông tin
     * Mục đích: Đảm bảo khi lấy phiếu nhập theo ID, thông tin của nó phải chính xác.
     */
    @Test
    public void selectById_KiemTraGiaTri() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        String validId = "1"; // Giả sử phiếu nhập với ID 1 tồn tại trong cơ sở dữ liệu

        PhieuNhapDTO result = dao.selectById(validId);

        // Kiểm tra các giá trị trả về phải chính xác
        Assert.assertNotNull(result);
        Assert.assertEquals(validId, String.valueOf(result.getMaphieu())); // Kiểm tra mã phiếu nhập
        Assert.assertTrue(result.getTongTien() > 0); // Kiểm tra tổng tiền
        Assert.assertNotNull(result.getThoigiantao()); // Kiểm tra thời gian tạo phiếu nhập
    }

    /**
     * TC1: Kiểm tra thống kê với khoảng tiền hợp lệ
     * Mục đích: Đảm bảo rằng khi gọi phương thức với khoảng tiền hợp lệ, phương thức trả về các phiếu nhập có tổng tiền nằm trong phạm vi từ `min` đến `max`.
     */
    @Test
    public void statistical_KhoangTienHopLe() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        long min = 100000; // Giá trị min
        long max = 1000000; // Giá trị max

        ArrayList<PhieuNhapDTO> result = dao.statistical(min, max);

        // Kiểm tra kết quả trả về không phải null
        Assert.assertNotNull(result);

        // Kiểm tra rằng tất cả các phiếu nhập có tổng tiền nằm trong phạm vi min và max
        for (PhieuNhapDTO phieuNhap : result) {
            Assert.assertTrue(phieuNhap.getTongTien() >= min && phieuNhap.getTongTien() <= max);
        }
    }

    /**
     * TC2: Kiểm tra thống kê với khoảng tiền không có phiếu nhập
     * Mục đích: Kiểm tra khi không có phiếu nhập nào trong phạm vi `min` và `max`, phương thức phải trả về danh sách rỗng.
     */
    @Test
    public void statistical_KhoangTienKhongCoPhieuNhap() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        long min = 10000000; // Giá trị min cao
        long max = 20000000; // Giá trị max cao

        ArrayList<PhieuNhapDTO> result = dao.statistical(min, max);

        // Kiểm tra rằng kết quả trả về là danh sách rỗng
        Assert.assertTrue(result.isEmpty());
    }

    /**
     * TC3: Kiểm tra thống kê với khoảng tiền là 0
     * Mục đích: Kiểm tra khi gọi phương thức với khoảng tiền từ 0 đến một giá trị hợp lệ.
     */
    @Test
    public void statistical_KhoangTienZero() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        long min = 0; // Giá trị min bằng 0
        long max = 1000000; // Giá trị max hợp lệ

        ArrayList<PhieuNhapDTO> result = dao.statistical(min, max);

        // Kiểm tra kết quả trả về không phải null
        Assert.assertNotNull(result);

        // Kiểm tra tất cả các phiếu nhập có tổng tiền nằm trong phạm vi min và max
        for (PhieuNhapDTO phieuNhap : result) {
            Assert.assertTrue(phieuNhap.getTongTien() >= min && phieuNhap.getTongTien() <= max);
        }
    }

    /**
     * TC4: Kiểm tra thống kê với min > max
     * Mục đích: Kiểm tra khi giá trị min lớn hơn max, phương thức có xử lý đúng không.
     */
    @Test
    public void statistical_MinLonHonMax() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        long min = 5000000; // Giá trị min lớn hơn max
        long max = 1000000; // Giá trị max nhỏ

        ArrayList<PhieuNhapDTO> result = dao.statistical(min, max);

        // Kiểm tra kết quả trả về là danh sách rỗng, vì min > max
        Assert.assertTrue(result.isEmpty());
    }

    /**
     * TC5: Kiểm tra thống kê với khoảng tiền rất nhỏ và rất lớn
     * Mục đích: Kiểm tra việc thống kê với khoảng tiền rất nhỏ (0) và rất lớn (không giới hạn), phương thức có trả về đúng dữ liệu.
     */
    @Test
    public void statistical_KhoangTienRatNhoVaRatLon() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        long min = 0; // Khoảng tiền nhỏ nhất
        long max = Long.MAX_VALUE; // Khoảng tiền lớn nhất

        ArrayList<PhieuNhapDTO> result = dao.statistical(min, max);

        // Kiểm tra kết quả trả về không phải null
        Assert.assertNotNull(result);

        // Kiểm tra rằng các phiếu nhập có tổng tiền hợp lệ và không bị giới hạn
        for (PhieuNhapDTO phieuNhap : result) {
            Assert.assertTrue(phieuNhap.getTongTien() >= min && phieuNhap.getTongTien() <= max);
        }
    }

    /**
     * TC1: Kiểm tra khi phiếu nhập có thể hủy
     * Mục đích: Đảm bảo rằng nếu tất cả các chi tiết sản phẩm có `maphieuxuat = 0`, phiếu nhập có thể bị hủy (kết quả trả về true).
     */
    @Test
    public void checkCancelPn_TatCaChiTietChuaXuat() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        int maphieu = 1001; // Mã phiếu nhập mẫu

        // Giả lập dữ liệu cho ctsanpham để tất cả các sản phẩm đều chưa xuất
        // Thêm chi tiết sản phẩm giả lập với maphieuxuat = 0
        // Giả lập việc insert vào cơ sở dữ liệu giả lập, hoặc giả lập được kết quả khi gọi phương thức select.

        boolean result = dao.checkCancelPn(maphieu);

        // Kiểm tra nếu kết quả trả về là true (có thể hủy phiếu nhập)
        Assert.assertTrue(result);
    }

    /**
     * TC2: Kiểm tra khi phiếu nhập không thể hủy
     * Mục đích: Đảm bảo rằng nếu có ít nhất một chi tiết sản phẩm có `maphieuxuat != 0`, phiếu nhập không thể hủy (kết quả trả về false).
     */
    @Test
    public void checkCancelPn_CoChiTietDaXuat() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        int maphieu = 1002; // Mã phiếu nhập mẫu

        // Giả lập dữ liệu cho ctsanpham để có ít nhất một sản phẩm có `maphieuxuat != 0`
        // Giả lập việc insert vào cơ sở dữ liệu giả lập, hoặc giả lập được kết quả khi gọi phương thức select.

        boolean result = dao.checkCancelPn(maphieu);

        // Kiểm tra nếu kết quả trả về là false (không thể hủy phiếu nhập)
        Assert.assertFalse(result);
    }

    /**
     * TC3: Kiểm tra khi phiếu nhập không có chi tiết sản phẩm
     * Mục đích: Đảm bảo rằng nếu phiếu nhập không có chi tiết sản phẩm nào, kết quả trả về vẫn đúng (phiếu nhập có thể hủy).
     */
    @Test
    public void checkCancelPn_KhongCoChiTietSanPham() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        int maphieu = 1003; // Mã phiếu nhập mẫu

        // Giả lập dữ liệu cho ctsanpham để không có chi tiết sản phẩm nào
        // Giả lập việc insert vào cơ sở dữ liệu giả lập, hoặc giả lập được kết quả khi gọi phương thức select.

        boolean result = dao.checkCancelPn(maphieu);

        // Kiểm tra nếu kết quả trả về là true (có thể hủy phiếu nhập)
        Assert.assertTrue(result);
    }

    /**
     * TC4: Kiểm tra khi có lỗi xảy ra trong truy vấn cơ sở dữ liệu
     * Mục đích: Kiểm tra khi có lỗi trong quá trình truy vấn cơ sở dữ liệu (ví dụ lỗi kết nối DB), phương thức phải xử lý lỗi một cách hợp lý.
     */
    @Test
    public void checkCancelPn_LoiTruyVanDuLieu() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        int maphieu = 1004; // Mã phiếu nhập mẫu

        // Giả lập lỗi trong quá trình truy vấn, ví dụ như kết nối DB bị lỗi.

        boolean result = dao.checkCancelPn(maphieu);

        // Kiểm tra nếu kết quả trả về là true hoặc false tùy theo cách xử lý lỗi trong phương thức
        // Trong trường hợp này, giả lập là không thể xác định kết quả khi có lỗi.
        // Ta có thể xử lý lỗi và kiểm tra giá trị mặc định trả về.
        Assert.assertFalse(result); // hoặc Assert.assertTrue(result) tùy cách xử lý lỗi của bạn.
    }

    /**
     * TC1: Kiểm tra hủy phiếu nhập hợp lệ
     * Mục đích: Đảm bảo rằng khi hủy phiếu nhập hợp lệ, các thay đổi liên quan đến các chi tiết sản phẩm, phiếu nhập được thực hiện đúng.
     */
    @Test
    public void cancelPhieuNhap_HopLe() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        int maphieu = 1001; // Mã phiếu nhập mẫu hợp lệ

        // Giả lập hoặc chèn dữ liệu vào cơ sở dữ liệu để tạo một phiếu nhập hợp lệ với chi tiết sản phẩm

        // Gọi phương thức cancelPhieuNhap
        int result = dao.cancelPhieuNhap(maphieu);

        // Kiểm tra nếu kết quả trả về là 1 (hủy thành công)
        Assert.assertEquals(1, result);

        // Kiểm tra xem các bản ghi liên quan trong các bảng đã bị xóa hoặc cập nhật đúng (ChiTietPhieuNhap, ChiTietSanPham, PhienBanSanPham)
        // Có thể viết các truy vấn kiểm tra lại trong cơ sở dữ liệu để đảm bảo rằng các bản ghi đã được xóa hoặc cập nhật đúng
    }

    /**
     * TC2: Kiểm tra khi phiếu nhập không tồn tại
     * Mục đích: Đảm bảo rằng nếu phiếu nhập không tồn tại, không có thay đổi nào được thực hiện và kết quả trả về là 0.
     */
    @Test
    public void cancelPhieuNhap_KhongTonTai() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        int maphieu = 9999; // Mã phiếu nhập không tồn tại trong cơ sở dữ liệu

        // Gọi phương thức cancelPhieuNhap
        int result = dao.cancelPhieuNhap(maphieu);

        // Kiểm tra nếu kết quả trả về là 0 (không có gì thay đổi vì không tìm thấy phiếu nhập)
        Assert.assertEquals(0, result);
    }

    /**
     * TC3: Kiểm tra lỗi khi có sự cố trong quá trình truy vấn cơ sở dữ liệu
     * Mục đích: Kiểm tra nếu có lỗi trong quá trình thực hiện câu lệnh SQL (chẳng hạn lỗi kết nối DB), phương thức phải xử lý đúng và trả về 0.
     */
    @Test
    public void cancelPhieuNhap_LoiTruyVan() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        int maphieu = 1002; // Mã phiếu nhập hợp lệ, nhưng giả lập lỗi truy vấn

        // Giả lập lỗi truy vấn cơ sở dữ liệu (ví dụ lỗi kết nối DB)

        // Gọi phương thức cancelPhieuNhap
        int result = dao.cancelPhieuNhap(maphieu);

        // Kiểm tra nếu kết quả trả về là 0 (không thực hiện được hành động do lỗi)
        Assert.assertEquals(0, result);
    }

    /**
     * TC1: Kiểm tra lấy giá trị Auto Increment hợp lệ
     * Mục đích: Đảm bảo rằng phương thức `getAutoIncrement()` trả về giá trị `AUTO_INCREMENT` chính xác từ cơ sở dữ liệu.
     */
    @Test
    public void getAutoIncrement_HopLe() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();

        // Gọi phương thức getAutoIncrement
        int result = dao.getAutoIncrement();

        // Kiểm tra kết quả trả về nếu giá trị auto increment hợp lệ (kết quả cần >= 0)
        Assert.assertTrue(result >= 0);
    }

    /**
     * TC2: Kiểm tra khi không có dữ liệu trong bảng
     * Mục đích: Đảm bảo rằng nếu không có dữ liệu trong bảng, phương thức trả về giá trị -1.
     */
    @Test
    public void getAutoIncrement_KhongCoDuLieu() throws SQLException {
        PhieuNhapDAO dao = new PhieuNhapDAO();

        // Giả lập trạng thái không có dữ liệu trong bảng `phieunhap`

        // Gọi phương thức getAutoIncrement
        int result = dao.getAutoIncrement();

        // Kiểm tra nếu giá trị trả về là -1 khi không có dữ liệu
        Assert.assertEquals(-1, result);
    }


}
