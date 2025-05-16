package DAO;

import DTO.KhachHangDTO;
import org.junit.*;
import java.sql.*;
import java.util.ArrayList;


import static org.junit.Assert.*;

public class KhachHangDAOTest {

    @Test
    public void testGetInstance() {
        // Gọi phương thức getInstance()
        KhachHangDAO dao = KhachHangDAO.getInstance();

        // Kiểm tra nếu đối tượng trả về không phải là null
        Assert.assertNotNull("KhachHangDAO instance should not be null", dao);

        // Kiểm tra nếu đối tượng trả về là một thể hiện của KhachHangDAO
        Assert.assertTrue("Object should be an instance of KhachHangDAO", dao instanceof KhachHangDAO);
    }

    /**
     * KH01
     * Mục tiêu: Kiểm tra chèn khách hàng hợp lệ vào DB → kết quả mong đợi: 1
     */
    @Test
    public void insertKhachHang_HopLe() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();

        kh.setHoten("Khách hàng TNHH ABC");
        kh.setDiachi("123 Đường A, Quận B");
        kh.setSdt("0909123456");

        dao.con.setAutoCommit(false);
        int result = dao.insert(kh);
        Assert.assertEquals(1, result);
        dao.con.rollback();
    }

    /**
     * KH02
     * Mục tiêu: Kiểm tra chèn khách hàng với mã đã tồn tại → kết quả mong đợi: 0 (thất bại)
     */
    @Test
    public void insertKhachHang_TrungMa() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setMaKH(1); // giả sử mã này đã tồn tại
        kh.setHoten("Khách hàng Trùng Mã");
        kh.setDiachi("Số 1 XYZ");
        kh.setSdt("0123456789");

        dao.con.setAutoCommit(false);
        int result = dao.insert(kh);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    /**
     * KH03
     * Mục tiêu: Kiểm tra khi tên khách hàng bị thiếu (null) → kết quả mong đợi: 0
     */
    @Test
    public void insertKhachHang_ThieuTen() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setMaKH(1002);
        kh.setHoten(null); // thiếu tên
        kh.setDiachi("456 Phố ABC");
        kh.setSdt("0911222333");

        dao.con.setAutoCommit(false);
        int result = dao.insert(kh);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    /**
     * KH04
     * Mục tiêu: Kiểm tra khi số điện thoại null → kết quả mong đợi: 0
     */
    @Test
    public void insertKhachHang_SdtNull() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setHoten("Thiếu SĐT Co.");
        kh.setDiachi("111 XYZ");
        kh.setSdt(null); // thiếu SĐT

        dao.con.setAutoCommit(false);
        int result = dao.insert(kh);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    /**
     * KH05
     * Mục tiêu: Kiểm tra khi mã khách hàng là số âm → kết quả mong đợi: 0
     */
    @Test
    public void insertKhachHang_MaAm() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setMaKH(-10); // mã âm
        kh.setHoten("Mã Âm Co.");
        kh.setDiachi("12 Đường Vô Danh");
        kh.setSdt("0999888777");

        dao.con.setAutoCommit(false);
        int result = dao.insert(kh);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    /**
     * KH06
     * Mục tiêu: Kiểm tra khi số điện thoại chứa toàn chữ cái → kết quả mong đợi: 0 (thất bại nếu DB có validate)
     */
    @Test
    public void insertKhachHang_SdtToanChu() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setHoten("Khách hàng Sai SĐT");
        kh.setDiachi("01 Đường 123");
        kh.setSdt("abcdefghij"); // sai định dạng

        dao.con.setAutoCommit(false);
        int result = dao.insert(kh);
        Assert.assertEquals(0, result); // nếu không validate ở DB có thể là 1, tuỳ yêu cầu hệ thống
        dao.con.rollback();
    }

    /**
     * KH07
     * Mục tiêu: Kiểm tra khi số điện thoại chỉ có 3 ký tự → kết quả mong đợi: 0 (thất bại nếu có kiểm tra độ dài)
     */
    @Test
    public void insertKhachHang_SdtQuaNgan() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setHoten("Khách hàng SĐT Ngắn");
        kh.setDiachi("22 Lê Lợi, Quận 1");
        kh.setSdt("123"); // quá ngắn

        dao.con.setAutoCommit(false);
        int result = dao.insert(kh);
        Assert.assertEquals(0, result); // nếu DB hoặc logic có kiểm tra độ dài
        dao.con.rollback();
    }

    /**
     * KH08
     * Mục tiêu: Kiểm tra khi tên khách hàng chỉ gồm toàn khoảng trắng → mong đợi: thất bại (0)
     */
    @Test
    public void insertKhachHang_TenToanKhoangTrang() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setHoten("     "); // tên toàn dấu cách
        kh.setDiachi("12 Nguyễn Huệ, TP.HCM");
        kh.setSdt("0909090909");

        dao.con.setAutoCommit(false);
        int result = dao.insert(kh);
        Assert.assertEquals(0, result); // Mong đợi: lỗi do tên không hợp lệ
        dao.con.rollback();
    }

    /**
     * KH09
     * Mục tiêu: Kiểm tra khi địa chỉ chỉ gồm toàn khoảng trắng → mong đợi: thất bại (0)
     */
    @Test
    public void insertKhachHang_DiaChiToanKhoangTrang() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setHoten("Abc");
        kh.setDiachi("    ");    // địa chỉ trắng
        kh.setSdt("0909123456"); // sđt hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(kh);
        Assert.assertEquals(0, result); // Mong đợi: thất bại do dữ liệu không hợp lệ
        dao.con.rollback();
    }

    /**
     * KH10
     * Mục tiêu: Kiểm tra trường hợp cập nhật hợp lệ
     */
    @Test
    public void updateKhachHang_HopLe() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setMaKH(1001); // Giả sử khách hàng với mã này đã tồn tại trong DB
        kh.setHoten("Khách hàng ABC");
        kh.setDiachi("123 Đường A, Quận B");
        kh.setSdt("0909123456");

        dao.con.setAutoCommit(false);
        int result = dao.update(kh);
        Assert.assertEquals(1, result); // Mong đợi: 1 nếu cập nhật thành công
        dao.con.rollback();
    }

    /**
     * KH11
     * Mục tiêu: Kiểm tra khi tên khách hàng đã tồn tại trong DB (giả sử DB không cho phép trùng tên)
     */
    @Test
    public void updateKhachHang_TenTrung() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setMaKH(1002); // Giả sử mã này đã tồn tại
        kh.setHoten("Khách hàng ABC"); // Tên trùng với khách hàng đã có
        kh.setDiachi("456 Đường C, Quận D");
        kh.setSdt("0909123457");

        dao.con.setAutoCommit(false);
        int result = dao.update(kh);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu không thể cập nhật do trùng tên
        dao.con.rollback();
    }

    /**
     * KH12
     * Mục tiêu: Kiểm tra khi số điện thoại sai định dạng
     */
    @Test
    public void updateKhachHang_SdtSai() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setMaKH(1004);
        kh.setHoten("Loan Test");
        kh.setDiachi("321 Đường G, Quận H");
        kh.setSdt("abc123"); // Số điện thoại sai định dạng

        dao.con.setAutoCommit(false);
        int result = dao.update(kh);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu số điện thoại không hợp lệ
        dao.con.rollback();
    }

    /**
     * KH13
     * Mục tiêu: Kiểm tra khi mã khách hàng không tồn tại trong DB
     */
    @Test
    public void updateKhachHang_KhongTonTai() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setMaKH(9999); // Mã không tồn tại trong DB
        kh.setHoten("Hoàng Thị Mai Loan");
        kh.setDiachi("100 Không Tồn Tại");
        kh.setSdt("0909123460");

        dao.con.setAutoCommit(false);
        int result = dao.update(kh);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu không tìm thấy mã để cập nhật
        dao.con.rollback();
    }

    /**
     * KH14
     * Mục tiêu: Kiểm tra khi tên khách hàng chỉ toàn khoảng trắng
     */
    @Test
    public void updateKhachHang_TenToanKhoangTrang() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setMaKH(1006); // Giả sử mã này đã tồn tại
        kh.setHoten("     "); // Tên khách hàng là khoảng trắng
        kh.setDiachi("200 Đường Z, Quận W");
        kh.setSdt("0909123461");

        dao.con.setAutoCommit(false);
        int result = dao.update(kh);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu tên không hợp lệ
        dao.con.rollback();
    }

    /**
     * KH15
     * Mục tiêu: Kiểm tra trường hợp tất cả các thông tin hợp lệ
     */
    @Test
    public void updateKhachHang_ThanhCong() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setMaKH(1010); // Giả sử mã này tồn tại trong DB
        kh.setHoten("Khách hàng MNO");
        kh.setDiachi("123 Đường L, Quận M");
        kh.setSdt("0909123464");

        dao.con.setAutoCommit(false);
        int result = dao.update(kh);
        Assert.assertEquals(1, result); // Mong đợi: 1 nếu cập nhật thành công
        dao.con.rollback();
    }

    /**
     * KH16
     * Mục tiêu: Kiểm tra khi mã khách hàng là số âm
     */
    @Test
    public void updateKhachHang_MaKHAm() throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        KhachHangDAO dao = new KhachHangDAO();
        kh.setMaKH(-1); // Mã khách hàng âm (không hợp lệ)
        kh.setHoten("Khách hàng PQR");
        kh.setDiachi("234 Đường P, Quận Q");
        kh.setSdt("0909123466");

        dao.con.setAutoCommit(false);
        int result = dao.update(kh);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu mã khách hàng không hợp lệ
        dao.con.rollback();
    }

    /**
     * KH17
     * Mục tiêu: Xóa khách hàng với mã hợp lệ (tồn tại trong DB)
     */
    @Test
    public void deleteKhachHang_HopLe() throws SQLException {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = "1001"; // Giả sử mã này tồn tại
        dao.con.setAutoCommit(false);
        int result = dao.delete(maKH);
        Assert.assertEquals(1, result); // Mong đợi: 1 dòng được cập nhật
        dao.con.rollback();
    }

    /**
     * KH18
     * Mục tiêu: Xóa khách hàng với mã không tồn tại
     */
    @Test
    public void deleteKhachHang_KhongTonTai() throws SQLException {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = "9999"; // Mã này không tồn tại
        dao.con.setAutoCommit(false);
        int result = dao.delete(maKH);
        Assert.assertEquals(0, result); // Mong đợi: 0 dòng được cập nhật
        dao.con.rollback();
    }

    /**
     * KH19
     * Mục tiêu: Xóa khách hàng với mã rỗng
     */
    @Test
    public void deleteKhachHang_MaRong() throws SQLException {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = ""; // Rỗng
        dao.con.setAutoCommit(false);
        int result = dao.delete(maKH);
        Assert.assertEquals(0, result); // Mong đợi: 0 hoặc exception tuỳ kiểm tra
        dao.con.rollback();
    }

    /**
     * KH20
     * Mục tiêu: Xóa khách hàng với mã là null
     */
    @Test
    public void deleteKhachHang_MaNull() throws SQLException {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = null; // null
        dao.con.setAutoCommit(false);
        int result = dao.delete(maKH);
        Assert.assertEquals(0, result); // Mong đợi: 0 hoặc exception nếu không xử lý null
        dao.con.rollback();
    }

    /**
     * KH21
     * Mục tiêu: Xóa khách hàng với mã là ký tự đặc biệt
     */
    @Test
    public void deleteKhachHang_MaKyTuDacBiet() throws SQLException {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = "@#%$"; // Không hợp lệ
        dao.con.setAutoCommit(false);
        int result = dao.delete(maKH);
        Assert.assertEquals(0, result); // Mong đợi: không cập nhật
        dao.con.rollback();
    }

    /**
     * KH22
     * Mục tiêu: Xóa khách hàng đã bị xóa trước đó (trạng thái = 0)
     */
    @Test
    public void deleteKhachHang_DaXoaTruoc() throws SQLException {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = "1002"; // Mã này đã có trạng thái = 0
        dao.con.setAutoCommit(false);
        int result = dao.delete(maKH);
        Assert.assertEquals(1, result); // Có thể vẫn trả về 1 (vì vẫn UPDATE được), hoặc 0 nếu có điều kiện tránh trùng
        dao.con.rollback();
    }

    /**
     * KH23
     * Mục tiêu: Xóa khách hàng với mã chứa khoảng trắng
     */
    @Test
    public void deleteKhachHang_MaKhoangTrang() throws SQLException {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = "   ";
        dao.con.setAutoCommit(false);
        int result = dao.delete(maKH);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    /**
     * KH24
     * Mục tiêu: Xóa khách hàng với mã là số âm (nếu dữ liệu là số)
     */
    @Test
    public void deleteKhachHang_MaAm() throws SQLException {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = "-5";
        dao.con.setAutoCommit(false);
        int result = dao.delete(maKH);
        Assert.assertEquals(0, result); // Không nên có NCC có mã âm
        dao.con.rollback();
    }

    /**
     * KH25: Lấy danh sách khách hàng khi có dữ liệu
     * Mục đích: Kiểm tra phương thức có trả về danh sách chứa các khách hàng hợp lệ không
     */
    @Test
    public void selectAll_CoDuLieu() {
        KhachHangDAO dao = new KhachHangDAO();
        ArrayList<KhachHangDTO> list = dao.selectAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0); // Có ít nhất 1 khách hàng hợp lệ
    }

    /**
     * KH26: Lấy danh sách khách hàng khi không có dữ liệu (tất cả đã bị xóa mềm)
     * Mục đích: Kiểm tra nếu không có khách hàng nào có trạng thái = 1 thì danh sách trả về rỗng
     */
    @Test
    public void selectAll_KhongCoDuLieu() throws SQLException {
        KhachHangDAO dao = new KhachHangDAO();
        dao.con.setAutoCommit(false);

        // Giả sử đã xóa mềm toàn bộ khách hàng để kiểm tra
        ArrayList<KhachHangDTO> list = dao.selectAll();
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size()); // Không có NCC nào đang hoạt động

        dao.con.rollback();
    }

    /**
     * KH27: Kiểm tra dữ liệu trả về có đầy đủ thông tin từng khách hàng
     * Mục đích: Đảm bảo từng đối tượng trả về đều có thông tin đầy đủ, không bị null
     */
    @Test
    public void selectAll_KiemTraNoiDung() {
        KhachHangDAO dao = new KhachHangDAO();
        ArrayList<KhachHangDTO> list = dao.selectAll();
        Assert.assertNotNull(list);
        for (KhachHangDTO kh : list) {
            Assert.assertNotNull(kh.getMaKH());
            Assert.assertNotNull(kh.getHoten());
            Assert.assertNotNull(kh.getDiachi());
            Assert.assertNotNull(kh.getSdt());
        }
    }

    /**
     * KH28: Không có lỗi xảy ra khi gọi selectAll
     * Mục đích: Kiểm tra rằng hàm hoạt động ổn định, không ném exception
     */
    @Test
    public void selectAll_KhongLoi() {
        KhachHangDAO dao = new KhachHangDAO();
        try {
            ArrayList<KhachHangDTO> list = dao.selectAll();
            Assert.assertNotNull(list); // Hàm không ném exception và trả về danh sách (có thể rỗng)
        } catch (Exception e) {
            Assert.fail("Không mong đợi có lỗi xảy ra: " + e.getMessage());
        }
    }

    /**
     * KH29: Truy xuất với mã khách hàng hợp lệ
     * Mục đích: Kiểm tra hàm trả về đúng đối tượng khi mã KH tồn tại
     */
    @Test
    public void selectById_HopLe() {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = "1"; // Giả sử "1" tồn tại trong CSDL

        KhachHangDTO kh = dao.selectById(maKH);
        Assert.assertNotNull(kh);
        Assert.assertEquals(1, kh.getMaKH());
    }

    /**
     * KH30: Truy xuất với mã không tồn tại
     * Mục đích: Kiểm tra hàm trả về null khi mã không tồn tại
     */
    @Test
    public void selectById_KhongTonTai() {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = "99999"; // Giả sử không có mã này trong DB

        KhachHangDTO kh = dao.selectById(maKH);
        Assert.assertNull(kh);
    }

    /**
     * KH31: Truy xuất với mã là null
     * Mục đích: Đảm bảo không bị crash và trả về null nếu mã là null
     */
    @Test
    public void selectById_Null() {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = null;

        KhachHangDTO kh = dao.selectById(maKH);
        Assert.assertNull(kh);
    }

    /**
     * KH32: Truy xuất với chuỗi rỗng ""
     * Mục đích: Đảm bảo hệ thống xử lý an toàn nếu mã rỗng
     */
    @Test
    public void selectById_Rong() {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = "";

        KhachHangDTO kh = dao.selectById(maKH);
        Assert.assertNull(kh);
    }

    /**
     * KH33: Truy xuất với mã có ký tự không hợp lệ (ký tự chữ)
     * Mục đích: Kiểm tra khi truyền chuỗi không phải số vào
     */
    @Test
    public void selectById_KyTuChu() {
        KhachHangDAO dao = new KhachHangDAO();
        String maKH = "abc"; // Giả sử DB đang lưu mã dạng số

        KhachHangDTO kh = dao.selectById(maKH);
        Assert.assertNull(kh); // Không nên ném exception
    }

    /**
     * KH34: Lấy giá trị AUTO_INCREMENT hợp lệ
     * Mục đích: Kiểm tra hàm trả về giá trị > 0 nếu bảng tồn tại và có AUTO_INCREMENT
     */
    @Test
    public void getAutoIncrement_HopLe() {
        KhachHangDAO dao = new KhachHangDAO();
        int autoIncrement = dao.getAutoIncrement();
        Assert.assertTrue(autoIncrement > 0);
    }

}