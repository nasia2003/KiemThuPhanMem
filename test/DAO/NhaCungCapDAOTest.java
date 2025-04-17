package DAO;

import DTO.NhaCungCapDTO;
import DTO.NhanVienDTO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class NhaCungCapDAOTest {

    @Test
    public void testGetInstance() {
        // Gọi phương thức getInstance()
        NhaCungCapDAO dao = NhaCungCapDAO.getInstance();

        // Kiểm tra nếu đối tượng trả về không phải là null
        Assert.assertNotNull("NhaCungCapDAO instance should not be null", dao);

        // Kiểm tra nếu đối tượng trả về là một thể hiện của NhaCungCapDAO
        Assert.assertTrue("Object should be an instance of NhaCungCapDAO", dao instanceof NhaCungCapDAO);
    }

    @Test
    public void insertNhaCungCap_HopLe() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp hợp lệ vào DB → kết quả mong đợi: 1
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();

        ncc.setTenncc("Công ty TNHH ABC");
        ncc.setDiachi("123 Đường A, Quận B");
        ncc.setEmail("abc@example.com");
        ncc.setSdt("0909123456");

        dao.con.setAutoCommit(false);
        int result = dao.insert(ncc);
        Assert.assertEquals(1, result);
        dao.con.rollback();
    }

    @Test
    public void insertNhaCungCap_TrungMa() throws SQLException {
        // Mục đích: Kiểm tra chèn nhà cung cấp với mã đã tồn tại → kết quả mong đợi: 0 (thất bại)
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(1); // giả sử mã này đã tồn tại
        ncc.setTenncc("Công ty Trùng Mã");
        ncc.setDiachi("Số 1 XYZ");
        ncc.setEmail("duplicate@example.com");
        ncc.setSdt("0123456789");

        dao.con.setAutoCommit(false);
        int result = dao.insert(ncc);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    @Test
    public void insertNhaCungCap_ThieuTen() throws SQLException {
        // Mục đích: Kiểm tra khi tên nhà cung cấp bị thiếu (null) → kết quả mong đợi: 0
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(1002);
        ncc.setTenncc(null); // thiếu tên
        ncc.setDiachi("456 Phố ABC");
        ncc.setEmail("abc2@example.com");
        ncc.setSdt("0911222333");

        dao.con.setAutoCommit(false);
        int result = dao.insert(ncc);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    @Test
    public void insertNhaCungCap_EmailSaiDinhDang() throws SQLException {
        // Mục đích: Kiểm tra khi email không đúng định dạng → kết quả mong đợi: 0 (nếu DB có validate)
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setTenncc("Sai Email Co.");
        ncc.setDiachi("789 DEF");
        ncc.setEmail("invalid-email"); // không đúng định dạng
        ncc.setSdt("0933445566");

        dao.con.setAutoCommit(false);
        int result = dao.insert(ncc);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    @Test
    public void insertNhaCungCap_SdtNull() throws SQLException {
        // Mục đích: Kiểm tra khi số điện thoại null → kết quả mong đợi: 0
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setTenncc("Thiếu SĐT Co.");
        ncc.setDiachi("111 XYZ");
        ncc.setEmail("sdtmissing@example.com");
        ncc.setSdt(null); // thiếu SĐT

        dao.con.setAutoCommit(false);
        int result = dao.insert(ncc);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    @Test
    public void insertNhaCungCap_MaAm() throws SQLException {
        // Mục đích: Kiểm tra khi mã nhà cung cấp là số âm → kết quả mong đợi: 0
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(-10); // mã âm
        ncc.setTenncc("Mã Âm Co.");
        ncc.setDiachi("12 Đường Vô Danh");
        ncc.setEmail("negative@example.com");
        ncc.setSdt("0999888777");

        dao.con.setAutoCommit(false);
        int result = dao.insert(ncc);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    @Test
    public void insertNhaCungCap_SdtToanChu() throws SQLException {
        // Mục đích: Kiểm tra khi số điện thoại chứa toàn chữ cái → kết quả mong đợi: 0 (thất bại nếu DB có validate)
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setTenncc("Công ty Sai SĐT");
        ncc.setDiachi("01 Đường 123");
        ncc.setEmail("charphone@example.com");
        ncc.setSdt("abcdefghij"); // sai định dạng

        dao.con.setAutoCommit(false);
        int result = dao.insert(ncc);
        Assert.assertEquals(0, result); // nếu không validate ở DB có thể là 1, tuỳ yêu cầu hệ thống
        dao.con.rollback();
    }

    @Test
    public void insertNhaCungCap_SdtQuaNgan() throws SQLException {
        // Mục đích: Kiểm tra khi số điện thoại chỉ có 3 ký tự → kết quả mong đợi: 0 (thất bại nếu có kiểm tra độ dài)
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setTenncc("Công ty SĐT Ngắn");
        ncc.setDiachi("22 Lê Lợi, Quận 1");
        ncc.setEmail("shortphone@example.com");
        ncc.setSdt("123"); // quá ngắn

        dao.con.setAutoCommit(false);
        int result = dao.insert(ncc);
        Assert.assertEquals(0, result); // nếu DB hoặc logic có kiểm tra độ dài
        dao.con.rollback();
    }

    @Test
    public void insertNhaCungCap_TenToanKhoangTrang() throws SQLException {
        // Mục đích: Kiểm tra khi tên nhà cung cấp chỉ gồm toàn khoảng trắng → mong đợi: thất bại (0)

        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setTenncc("     "); // tên toàn dấu cách
        ncc.setDiachi("12 Nguyễn Huệ, TP.HCM");
        ncc.setEmail("white@example.com");
        ncc.setSdt("0909090909");

        dao.con.setAutoCommit(false);
        int result = dao.insert(ncc);
        Assert.assertEquals(0, result); // Mong đợi: lỗi do tên không hợp lệ
        dao.con.rollback();
    }

    @Test
    public void insertNhaCungCap_DiaChiToanKhoangTrang() throws SQLException {
        // Mục đích: Kiểm tra khi tên, địa chỉ và email chỉ gồm toàn khoảng trắng → mong đợi: thất bại (0)

        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setTenncc("Abc");   // tên trắng
        ncc.setDiachi("    ");    // địa chỉ trắng
        ncc.setEmail("yuu@gmail.com");     // email trắng
        ncc.setSdt("0909123456"); // sđt hợp lệ

        dao.con.setAutoCommit(false);
        int result = dao.insert(ncc);
        Assert.assertEquals(0, result); // Mong đợi: thất bại do dữ liệu không hợp lệ
        dao.con.rollback();
    }
    @Test
    public void updateNhaCungCap_HopLe() throws SQLException {
        // Mục đích: Kiểm tra trường hợp cập nhật hợp lệ
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(1001); // Giả sử nhà cung cấp với mã này đã tồn tại trong DB
        ncc.setTenncc("Công ty ABC");
        ncc.setDiachi("123 Đường A, Quận B");
        ncc.setEmail("abc@example.com");
        ncc.setSdt("0909123456");

        dao.con.setAutoCommit(false);
        int result = dao.update(ncc);
        Assert.assertEquals(1, result); // Mong đợi: 1 nếu cập nhật thành công
        dao.con.rollback();
    }

    @Test
    public void updateNhaCungCap_TenTrung() throws SQLException {
        // Mục đích: Kiểm tra khi tên nhà cung cấp đã tồn tại trong DB (giả sử DB không cho phép trùng tên)
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(1002); // Giả sử mã này đã tồn tại
        ncc.setTenncc("Công ty ABC"); // Tên trùng với nhà cung cấp đã có
        ncc.setDiachi("456 Đường C, Quận D");
        ncc.setEmail("abc2@example.com");
        ncc.setSdt("0909123457");

        dao.con.setAutoCommit(false);
        int result = dao.update(ncc);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu không thể cập nhật do trùng tên
        dao.con.rollback();
    }

    @Test
    public void updateNhaCungCap_EmailSai() throws SQLException {
        // Mục đích: Kiểm tra khi email không hợp lệ
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(1003);
        ncc.setTenncc("Công ty XYZ");
        ncc.setDiachi("789 Đường E, Quận F");
        ncc.setEmail("invalid-email"); // email sai định dạng
        ncc.setSdt("0909123458");

        dao.con.setAutoCommit(false);
        int result = dao.update(ncc);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu email không hợp lệ
        dao.con.rollback();
    }

    @Test
    public void updateNhaCungCap_SdtSai() throws SQLException {
        // Mục đích: Kiểm tra khi số điện thoại sai định dạng
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(1004);
        ncc.setTenncc("Công ty LMN");
        ncc.setDiachi("321 Đường G, Quận H");
        ncc.setEmail("lmn@example.com");
        ncc.setSdt("abc123"); // Số điện thoại sai định dạng

        dao.con.setAutoCommit(false);
        int result = dao.update(ncc);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu số điện thoại không hợp lệ
        dao.con.rollback();
    }

    @Test
    public void updateNhaCungCap_KhongTonTai() throws SQLException {
        // Mục đích: Kiểm tra khi mã nhà cung cấp không tồn tại trong DB
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(9999); // Mã không tồn tại trong DB
        ncc.setTenncc("Công ty Nonexistent");
        ncc.setDiachi("100 Không Tồn Tại");
        ncc.setEmail("nonexistent@example.com");
        ncc.setSdt("0909123460");

        dao.con.setAutoCommit(false);
        int result = dao.update(ncc);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu không tìm thấy mã để cập nhật
        dao.con.rollback();
    }

    @Test
    public void updateNhaCungCap_TenToanKhoangTrang() throws SQLException {
        // Mục đích: Kiểm tra khi tên nhà cung cấp chỉ toàn khoảng trắng
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(1006); // Giả sử mã này đã tồn tại
        ncc.setTenncc("     "); // Tên nhà cung cấp là khoảng trắng
        ncc.setDiachi("200 Đường Z, Quận W");
        ncc.setEmail("space@example.com");
        ncc.setSdt("0909123461");

        dao.con.setAutoCommit(false);
        int result = dao.update(ncc);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu tên không hợp lệ
        dao.con.rollback();
    }

    @Test
    public void updateNhaCungCap_ThanhCong() throws SQLException {
        // Mục đích: Kiểm tra trường hợp tất cả các thông tin hợp lệ
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(1010); // Giả sử mã này tồn tại trong DB
        ncc.setTenncc("Công ty MNO");
        ncc.setDiachi("123 Đường L, Quận M");
        ncc.setEmail("mno@example.com");
        ncc.setSdt("0909123464");

        dao.con.setAutoCommit(false);
        int result = dao.update(ncc);
        Assert.assertEquals(1, result); // Mong đợi: 1 nếu cập nhật thành công
        dao.con.rollback();
    }

    @Test
    public void updateNhaCungCap_MaNccAm() throws SQLException {
        // Mục đích: Kiểm tra khi mã nhà cung cấp là số âm
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ncc.setMancc(-1); // Mã nhà cung cấp âm (không hợp lệ)
        ncc.setTenncc("Công ty PQR");
        ncc.setDiachi("234 Đường P, Quận Q");
        ncc.setEmail("pqr@example.com");
        ncc.setSdt("0909123466");

        dao.con.setAutoCommit(false);
        int result = dao.update(ncc);
        Assert.assertEquals(0, result); // Mong đợi: 0 nếu mã nhà cung cấp không hợp lệ
        dao.con.rollback();
    }

    @Test
    public void deleteNhaCungCap_HopLe() throws SQLException {
        // Xóa nhà cung cấp với mã hợp lệ (tồn tại trong DB)
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String mancc = "1001"; // Giả sử mã này tồn tại
        dao.con.setAutoCommit(false);
        int result = dao.delete(mancc);
        Assert.assertEquals(1, result); // Mong đợi: 1 dòng được cập nhật
        dao.con.rollback();
    }

    @Test
    public void deleteNhaCungCap_KhongTonTai() throws SQLException {
        // Xóa nhà cung cấp với mã không tồn tại
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String mancc = "9999"; // Mã này không tồn tại
        dao.con.setAutoCommit(false);
        int result = dao.delete(mancc);
        Assert.assertEquals(0, result); // Mong đợi: 0 dòng được cập nhật
        dao.con.rollback();
    }

    @Test
    public void deleteNhaCungCap_MaRong() throws SQLException {
        // Xóa nhà cung cấp với mã rỗng
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String mancc = ""; // Rỗng
        dao.con.setAutoCommit(false);
        int result = dao.delete(mancc);
        Assert.assertEquals(0, result); // Mong đợi: 0 hoặc exception tuỳ kiểm tra
        dao.con.rollback();
    }

    @Test
    public void deleteNhaCungCap_MaNull() throws SQLException {
        // Xóa nhà cung cấp với mã là null
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String mancc = null; // null
        dao.con.setAutoCommit(false);
        int result = dao.delete(mancc);
        Assert.assertEquals(0, result); // Mong đợi: 0 hoặc exception nếu không xử lý null
        dao.con.rollback();
    }

    @Test
    public void deleteNhaCungCap_MaKyTuDacBiet() throws SQLException {
        // Xóa nhà cung cấp với mã là ký tự đặc biệt
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String mancc = "@#%$"; // Không hợp lệ
        dao.con.setAutoCommit(false);
        int result = dao.delete(mancc);
        Assert.assertEquals(0, result); // Mong đợi: không cập nhật
        dao.con.rollback();
    }

    @Test
    public void deleteNhaCungCap_DaXoaTruoc() throws SQLException {
        // Xóa nhà cung cấp đã bị xóa trước đó (trạng thái = 0)
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String mancc = "1002"; // Mã này đã có trạng thái = 0
        dao.con.setAutoCommit(false);
        int result = dao.delete(mancc);
        Assert.assertEquals(1, result); // Có thể vẫn trả về 1 (vì vẫn UPDATE được), hoặc 0 nếu có điều kiện tránh trùng
        dao.con.rollback();
    }

    @Test
    public void deleteNhaCungCap_MaKhoangTrang() throws SQLException {
        // Xóa nhà cung cấp với mã chứa khoảng trắng
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String mancc = "   ";
        dao.con.setAutoCommit(false);
        int result = dao.delete(mancc);
        Assert.assertEquals(0, result);
        dao.con.rollback();
    }

    @Test
    public void deleteNhaCungCap_MaAm() throws SQLException {
        // Xóa nhà cung cấp với mã là số âm (nếu dữ liệu là số)
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String mancc = "-5";
        dao.con.setAutoCommit(false);
        int result = dao.delete(mancc);
        Assert.assertEquals(0, result); // Không nên có NCC có mã âm
        dao.con.rollback();
    }

    /**
     * TC1: Lấy danh sách nhà cung cấp khi có dữ liệu
     * Mục đích: Kiểm tra phương thức có trả về danh sách chứa các nhà cung cấp hợp lệ không
     */
    @Test
    public void selectAll_CoDuLieu() {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ArrayList<NhaCungCapDTO> list = dao.selectAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0); // Có ít nhất 1 nhà cung cấp hợp lệ
    }

    /**
     * TC2: Lấy danh sách nhà cung cấp khi không có dữ liệu (tất cả đã bị xóa mềm)
     * Mục đích: Kiểm tra nếu không có nhà cung cấp nào có trạng thái = 1 thì danh sách trả về rỗng
     */
    @Test
    public void selectAll_KhongCoDuLieu() throws SQLException {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        dao.con.setAutoCommit(false);

        // Giả sử đã xóa mềm toàn bộ nhà cung cấp để kiểm tra
        ArrayList<NhaCungCapDTO> list = dao.selectAll();
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size()); // Không có NCC nào đang hoạt động

        dao.con.rollback();
    }

    /**
     * TC3: Kiểm tra dữ liệu trả về có đầy đủ thông tin từng nhà cung cấp
     * Mục đích: Đảm bảo từng đối tượng trả về đều có thông tin đầy đủ, không bị null
     */
    @Test
    public void selectAll_KiemTraNoiDung() {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        ArrayList<NhaCungCapDTO> list = dao.selectAll();
        Assert.assertNotNull(list);
        for (NhaCungCapDTO ncc : list) {
            Assert.assertNotNull(ncc.getMancc());
            Assert.assertNotNull(ncc.getTenncc());
            Assert.assertNotNull(ncc.getDiachi());
            Assert.assertNotNull(ncc.getEmail());
            Assert.assertNotNull(ncc.getSdt());
        }
    }

    /**
     * TC4: Không có lỗi xảy ra khi gọi selectAll
     * Mục đích: Kiểm tra rằng hàm hoạt động ổn định, không ném exception
     */
    @Test
    public void selectAll_KhongLoi() {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        try {
            ArrayList<NhaCungCapDTO> list = dao.selectAll();
            Assert.assertNotNull(list); // Hàm không ném exception và trả về danh sách (có thể rỗng)
        } catch (Exception e) {
            Assert.fail("Không mong đợi có lỗi xảy ra: " + e.getMessage());
        }
    }

    /**
     * TC1: Truy xuất với mã nhà cung cấp hợp lệ
     * Mục đích: Kiểm tra hàm trả về đúng đối tượng khi mã NCC tồn tại
     */
    @Test
    public void selectById_HopLe() {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String maNCC = "1"; // Giả sử "1" tồn tại trong CSDL

        NhaCungCapDTO ncc = dao.selectById(maNCC);
        Assert.assertNotNull(ncc);
        Assert.assertEquals(1, ncc.getMancc());
    }

    /**
     * TC2: Truy xuất với mã không tồn tại
     * Mục đích: Kiểm tra hàm trả về null khi mã không tồn tại
     */
    @Test
    public void selectById_KhongTonTai() {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String maNCC = "99999"; // Giả sử không có mã này trong DB

        NhaCungCapDTO ncc = dao.selectById(maNCC);
        Assert.assertNull(ncc);
    }

    /**
     * TC3: Truy xuất với mã là null
     * Mục đích: Đảm bảo không bị crash và trả về null nếu mã là null
     */
    @Test
    public void selectById_Null() {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String maNCC = null;

        NhaCungCapDTO ncc = dao.selectById(maNCC);
        Assert.assertNull(ncc);
    }

    /**
     * TC4: Truy xuất với chuỗi rỗng ""
     * Mục đích: Đảm bảo hệ thống xử lý an toàn nếu mã rỗng
     */
    @Test
    public void selectById_Rong() {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String maNCC = "";

        NhaCungCapDTO ncc = dao.selectById(maNCC);
        Assert.assertNull(ncc);
    }

    /**
     * TC5: Truy xuất với mã có ký tự không hợp lệ (ký tự chữ)
     * Mục đích: Kiểm tra khi truyền chuỗi không phải số vào
     */
    @Test
    public void selectById_KyTuChu() {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        String maNCC = "abc"; // Giả sử DB đang lưu mã dạng số

        NhaCungCapDTO ncc = dao.selectById(maNCC);
        Assert.assertNull(ncc); // Không nên ném exception
    }

    /**
     * TC1: Lấy giá trị AUTO_INCREMENT hợp lệ
     * Mục đích: Kiểm tra hàm trả về giá trị > 0 nếu bảng tồn tại và có AUTO_INCREMENT
     */
    @Test
    public void getAutoIncrement_HopLe() {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        int autoIncrement = dao.getAutoIncrement();
        Assert.assertTrue(autoIncrement > 0);
    }

}