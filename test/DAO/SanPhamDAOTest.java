package DAO;

import DTO.SanPhamDTO;
import static org.junit.Assert.*;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

public class SanPhamDAOTest {

    private static Connection connection;
    private static SanPhamDAO sanPhamDAO;

    @BeforeClass
    public static void setUpClass() throws Exception {
        sanPhamDAO = SanPhamDAO.getInstance();
        connection = sanPhamDAO.con;
    }

    @Before
    public void setUp() throws Exception {
        connection.setAutoCommit(false);  // Bắt đầu transaction
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            connection.setAutoCommit(true);
        }
    }

    @AfterClass
    public static void globalTearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // === INSERT TESTS ===

    /**
     * Mã testcase: SP_48
     * Mục tiêu: Test insert thất bại khi thêm 2 bản ghi giống nhau (khác mã)
     * Input: 2 bản ghi dữ liệu hợp lệ giống hệt nhau nhưng khác masp
     * Expected output: insert trả về 1 khi insert sp1, trả về 0 khi insert sp2
     */

    @Test
    public void insertSanPham_TrungSanPham() throws Exception {
        SanPhamDTO sp1 = new SanPhamDTO(98, "SP Insert", "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        SanPhamDTO sp2 = new SanPhamDTO(99, "SP Insert", "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result1 = sanPhamDAO.insert(sp1);
        assertEquals(1, result1);

        int result2 = sanPhamDAO.insert(sp2);
        assertEquals(0, result2);
    }

    /**
     * Mã testcase: SP_01
     * Mục tiêu: Test insert thành công với dữ liệu hợp lệ
     * Input: dữ liệu hợp lệ
     * Expected output: insert trả về 1, dữ liệu tồn tại trong DB
     */
    @Test
    public void insertSanPham_ThanhCong() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Insert", "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(1, result);

        String sql = "SELECT * FROM sanpham WHERE masp = 99";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        assertTrue(rs.next()); // Có ít nhất 1 bản ghi
        assertEquals("SP Insert", rs.getString("tensp"));
        assertEquals("img.png", rs.getString("hinhanh"));
        assertEquals("Chip", rs.getString("chipxuly"));
        assertEquals(4000, rs.getInt("dungluongpin"));
    }

    /**
     * Mã testcase: SP_02
     * Mục tiêu: Test insert thất bại khi tên sản phẩm null
     * Input: tên null (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_TenNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, null, "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_03
     * Mục tiêu: Test insert thất bại khi tên sản phẩm rỗng
     * Input: tên rỗng (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_TenRong() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "", "img.png", 1, "Chip", 4000, 6.0, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_04
     * Mục tiêu: Test insert thất bại khi pin âm
     * Input: pin âm (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_PinAm() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, "Chip", -100, 6.0, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_05
     * Mục tiêu: Test insert thất bại khi camera sau null
     * Input: camera sau null (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_CameraSauNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, "Chip", 4000, 6.1, 1, 1, null, "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_06
     * Mục tiêu: Test insert thất bại khi số lượng tồn âm
     * Input: số lượng tồn âm (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_SoLuongTonAm() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, -10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_07
     * Mục tiêu: Test insert thất bại khi hình ảnh null
     * Input: hình ảnh null (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_HinhAnhNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", null, 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_08
     * Mục tiêu: Test insert thất bại khi hình ảnh rỗng
     * Input: hình ảnh rỗng (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_HinhAnhEmpty() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_09
     * Mục tiêu: Test insert thất bại khi chip xử lý null
     * Input: chip xử lý null (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_ChipXuLyNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, null, 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_10
     * Mục tiêu: Test insert thất bại khi camera trước null
     * Input: camera trước null (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_CameraTruocNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", null, 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_11
     * Mục tiêu: Test insert thất bại khi thời gian bảo hành âm
     * Input: thời gian bảo hành âm (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_ThoiGianBaoHanhAm() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", -6, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_12
     * Mục tiêu: Test insert thất bại khi thương hiệu âm
     * Input: thương hiệu âm (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_ThuongHieuAm() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, -1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_13
     * Mục tiêu: Test insert thất bại khi khu vực kho âm
     * Input: khu vực kho âm (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_KhuVucKhoAm() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, -1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_14
     * Mục tiêu: Test insert thất bại khi kích thước âm
     * Input: kích thước âm (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_KichThuocAm() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, "Chip", 4000, -5.5, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_15
     * Mục tiêu: Test insert thất bại khi kích thước bằng 0
     * Input: kích thước bằng 0 (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_KichThuocZero() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, "Chip", 4000, 0.0, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_16
     * Mục tiêu: Test insert thất bại khi kích thước NaN
     * Input: kích thước NaN (các trường khác hợp lệ)
     * Expected output: insert trả về 0, không có dữ liệu mới trong DB
     */
    @Test
    public void insertSanPham_KichThuocNaN() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(99, "SP Test", "img.png", 1, "Chip", 4000, Double.NaN, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);

        String sql = "SELECT * FROM sanpham WHERE masp = 99";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        assertFalse("Bản ghi không hợp lệ vẫn được insert vào DB", rs.next());
    }

    // === UPDATE TESTS ===


    /**
     * Mã testcase: SP_17
     * Mục tiêu: Test update thành công với dữ liệu hợp lệ
     * Input: dữ liệu hợp lệ (masp hợp lệ)
     * Expected output: update trả về 1, dữ liệu được cập nhật trong DB
     */
    @Test
    public void updateSanPham_ThanhCong() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, "SP Update", "img_upd.png", 1, "ChipX", 5000, 6.5, 2, 2, "16MP", "10MP", 24, 2, 2, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(1, result);

        String sql = "SELECT * FROM sanpham WHERE masp = 1";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        assertTrue(rs.next()); // Có ít nhất 1 bản ghi
        assertEquals("SP Update", rs.getString("tensp"));
        assertEquals("img_upd.png", rs.getString("hinhanh"));
        assertEquals("ChipX", rs.getString("chipxuly"));
        assertEquals(5000, rs.getInt("dungluongpin"));
    }

    /**
     * Mã testcase: SP_18
     * Mục tiêu: Test update thất bại khi tên sản phẩm null
     * Input: tên null (các trường khác hợp lệ, masp hợp lệ)
     * Expected output: update trả về 0, không có thay đổi trong DB
     */
    @Test
    public void updateSanPham_TenNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, null, "img_upd.png", 1, "ChipX", 5000, 6.5, 2, 2, "16MP", "10MP", 24, 2, 2, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_19
     * Mục tiêu: Test update thất bại khi dung lượng pin âm
     * Input: dung lượng pin âm (các trường khác hợp lệ, masp hợp lệ)
     * Expected output: update trả về 0, không có thay đổi trong DB
     */
    @Test
    public void updateSanPham_PinAm() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, "SP Update", "img_upd.png", 1, "ChipX", -500, 6.5, 2, 2, "16MP", "10MP", 24, 2, 2, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_20
     * Mục tiêu: Test update thất bại khi kích thước màn hình quá lớn
     * Input: kích thước màn hình lớn (các trường khác hợp lệ, masp hợp lệ)
     * Expected output: update trả về 0, không có thay đổi trong DB
     */
    @Test
    public void updateSanPham_KichThuocLon() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, "SP Update", "img_upd.png", 1, "ChipX", 5000, 9999.99, 2, 2, "16MP", "10MP", 24, 2, 2, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_21
     * Mục tiêu: Test update thất bại khi camera sau null
     * Input: camera sau null (các trường khác hợp lệ, masp hợp lệ)
     * Expected output: update trả về 0, không có thay đổi trong DB
     */
    @Test
    public void updateSanPham_CameraSauNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, "SP Update", "img_upd.png", 1, "ChipX", 5000, 6.5, 2, 2, null, "10MP", 24, 2, 2, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_22
     * Mục tiêu: Test update thất bại khi camera trước null
     * Input: camera trước null (các trường khác hợp lệ, masp hợp lệ)
     * Expected output: update trả về 0, không có thay đổi trong DB
     */
    @Test
    public void updateSanPham_CameraTruocNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, "SP Update", "img_upd.png", 1, "ChipX", 5000, 6.5, 2, 2, "16MP", null, 24, 2, 2, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_23
     * Mục tiêu: Test update thất bại khi hình ảnh null
     * Input: hình ảnh null (các trường khác hợp lệ, masp hợp lệ)
     * Expected output: update trả về 0, không có thay đổi trong DB
     */
    @Test
    public void updateSanPham_HinhAnhNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, "SP Update", null, 1, "ChipX", 5000, 6.5, 2, 2, "16MP", "10MP", 24, 2, 2, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_24
     * Mục tiêu: Test update thất bại khi chip xử lý null
     * Input: chip xử lý null (các trường khác hợp lệ, masp hợp lệ)
     * Expected output: update trả về 0, không có thay đổi trong DB
     */
    @Test
    public void updateSanPham_ChipXuLyNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, "SP Update", "img_upd.png", 1, null, 5000, 6.5, 2, 2, "16MP", "10MP", 24, 2, 2, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_25
     * Mục tiêu: Test update thất bại khi thời gian bảo hành âm
     * Input: thời gian bảo hành âm (các trường khác hợp lệ, masp hợp lệ)
     * Expected output: update trả về 0, không có thay đổi trong DB
     */
    @Test
    public void updateSanPham_ThoiGianBaoHanhAm() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, "SP Update", "img_upd.png", 1, "ChipX", 5000, 6.5, 2, 2, "16MP", "10MP", -12, 2, 2, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_26
     * Mục tiêu: Test update thất bại khi thương hiệu âm
     * Input: thương hiệu âm (các trường khác hợp lệ, masp hợp lệ)
     * Expected output: update trả về 0, không có thay đổi trong DB
     */
    @Test
    public void updateSanPham_ThuongHieuAm() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, "SP Update", "img_upd.png", 1, "ChipX", 5000, 6.5, 2, 2, "16MP", "10MP", 24, -1, 2, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_27
     * Mục tiêu: Test update thất bại khi khu vực kho âm
     * Input: khu vực kho âm (các trường khác hợp lệ, masp hợp lệ)
     * Expected output: update trả về 0, không có thay đổi trong DB
     */
    @Test
    public void updateSanPham_KhuVucKhoAm() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(1, "SP Update", "img_upd.png", 1, "ChipX", 5000, 6.5, 2, 2, "16MP", "10MP", 24, 2, -1, 0);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    // === DELETE TESTS ===

    /**
     * Mã testcase: SP_28
     * Mục tiêu: Test delete (soft) thành công với masp hợp lệ
     * Input: masp = "1" (đã tồn tại và trangthai=1)
     * Expected output: delete trả về 1, trangthai trong DB được đặt = 0
     */
    @Test
    public void deleteSanPham_ThanhCong() throws Exception {
        String masp = "1";
        int result = sanPhamDAO.delete(masp);
        assertEquals(1, result);

        String sql = "SELECT * FROM sanpham WHERE masp = 1";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        assertTrue(rs.next()); // Có ít nhất 1 bản ghi
        assertEquals(0, rs.getInt("trangthai"));
    }

    /**
     * Mã testcase: SP_29
     * Mục tiêu: Test delete thất bại với masp không tồn tại
     * Input: masp = "9999" (không tồn tại trong DB)
     * Expected output: delete trả về 0, không có bản ghi nào bị ảnh hưởng
     */
    @Test
    public void deleteSanPham_KhongTonTai() throws Exception {
        String masp = "9999";
        int result = sanPhamDAO.delete(masp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_30
     * Mục tiêu: Test delete thất bại khi masp null
     * Input: masp = null
     * Expected output: delete trả về 0 (hoặc ném SQLException tuỳ implement), không có thay đổi trong DB
     */
    @Test
    public void deleteSanPham_MaspNull() throws Exception {
        String masp = null;
        int result = sanPhamDAO.delete(masp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_31
     * Mục tiêu: Test delete thất bại khi masp rỗng
     * Input: masp = ""
     * Expected output: delete trả về 0, không có bản ghi nào bị ảnh hưởng
     */
    @Test
    public void deleteSanPham_MaspEmpty() throws Exception {
        String masp = "";
        int result = sanPhamDAO.delete(masp);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_32
     * Mục tiêu: Test delete thất bại khi masp không phải số
     * Input: masp = "ABC"
     * Expected output: delete trả về 0, không có bản ghi nào bị ảnh hưởng (hoặc ném SQLException tuỳ implement)
     */
    @Test
    public void deleteSanPham_MaspInvalidFormat() throws Exception {
        String masp = "ABC";
        int result = sanPhamDAO.delete(masp);
        assertEquals(0, result);
    }

    // === SELECT ALL TESTS ===

    /**
     * Mã testcase: SP_33
     * Mục tiêu: Test selectAll trả về danh sách sản phẩm khi có dữ liệu active
     * Input: trong DB có ít nhất 1 sản phẩm với trangthai = 1
     * Expected output: ArrayList<SanPhamDTO> size > 0, chứa các DTO với trangthai = 1
     */
    @Test
    public void selectAll_CoSanPhamActive() throws Exception {
        ArrayList<SanPhamDTO> list = sanPhamDAO.selectAll();

        // Đếm số bản ghi active trực tiếp từ DB
        PreparedStatement pst = connection.prepareStatement(
                "SELECT COUNT(*) AS cnt FROM sanpham WHERE trangthai = 1"
        );
        ResultSet rs = pst.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt("cnt");
        }
        rs.close();
        pst.close();

        assertEquals(count, list.size());
    }

    // === SELECT TESTS ===
    /**
     * Mã testcase: SP_34
     * Mục tiêu: Test selectById trả về đúng DTO khi masp tồn tại
     * Input: masp = "1" (đã tồn tại trong DB)
     * Expected output: không null, các trường của DTO khớp với DB
     */
    @Test
    public void selectById_TonTai() throws Exception {
        String masp = "1";
        SanPhamDTO sp = sanPhamDAO.selectById(masp);
        assertNotNull(sp);
        assertEquals(1, sp.getMasp());
    }

    /**
     * Mã testcase: SP_35
     * Mục tiêu: Test selectById trả về null khi masp không tồn tại
     * Input: masp = "9999" (không tồn tại trong DB)
     * Expected output: null
     */
    @Test
    public void selectById_KhongTonTai() throws Exception {
        String masp = "9999";
        SanPhamDTO sp = sanPhamDAO.selectById(masp);
        assertNull(sp);
    }

    /**
     * Mã testcase: SP_36
     * Mục tiêu: Test selectById trả về null khi masp null
     * Input: masp = null
     * Expected output: null (không ném exception)
     */
    @Test
    public void selectById_MaspNull() throws Exception {
        String masp = null;
        SanPhamDTO sp = sanPhamDAO.selectById(masp);
        assertNull(sp);
    }

    /**
     * Mã testcase: SP_37
     * Mục tiêu: Test selectById trả về null khi masp rỗng
     * Input: masp = ""
     * Expected output: null
     */
    @Test
    public void selectById_MaspEmpty() throws Exception {
        String masp = "";
        SanPhamDTO sp = sanPhamDAO.selectById(masp);
        assertNull(sp);
    }

    // === SELECT BY PHIEN BAN TESTS ===

    /**
     * Mã testcase: SP_38
     * Mục tiêu: Test selectByPhienBan trả về đúng DTO khi maphienbansp tồn tại
     * Input: maphienbansp = 2 (tồn tại trong bảng phienbansanpham), ứng với sản phẩm có masp = 1
     * Expected output: không null, các trường của DTO khớp với bản ghi join
     */
    @Test
    public void selectByPhienBan_TonTai() throws Exception {
        String maphienbansp = "2";
        SanPhamDTO sp = sanPhamDAO.selectByPhienBan(maphienbansp);
        assertNotNull(sp);
        // Ví dụ kiểm tra một số trường mẫu:
        assertEquals(1, sp.getMasp());
    }

    /**
     * Mã testcase: SP_39
     * Mục tiêu: Test selectByPhienBan trả về null khi maphienbansp không tồn tại
     * Input: maphienbansp = "PB999" (không tồn tại)
     * Expected output: null
     */
    @Test
    public void selectByPhienBan_KhongTonTai() throws Exception {
        String maphienbansp = "PB999";
        SanPhamDTO sp = sanPhamDAO.selectByPhienBan(maphienbansp);
        assertNull(sp);
    }

    /**
     * Mã testcase: SP_40
     * Mục tiêu: Test selectByPhienBan trả về null khi maphienbansp null
     * Input: maphienbansp = null
     * Expected output: null (không ném exception)
     */
    @Test
    public void selectByPhienBan_Null() throws Exception {
        String maphienbansp = null;
        SanPhamDTO sp = sanPhamDAO.selectByPhienBan(maphienbansp);
        assertNull(sp);
    }

    /**
     * Mã testcase: SP_41
     * Mục tiêu: Test selectByPhienBan trả về null khi maphienbansp rỗng
     * Input: maphienbansp = ""
     * Expected output: null
     */
    @Test
    public void selectByPhienBan_Empty() throws Exception {
        String maphienbansp = "";
        SanPhamDTO sp = sanPhamDAO.selectByPhienBan(maphienbansp);
        assertNull(sp);
    }


    /**
     * Mã testcase: SP_42
     * Mục tiêu: Test getAutoIncrement trả về đúng giá trị và tăng dần sau mỗi lần insert
     * Input: Gọi getAutoIncrement hai lần trước mỗi lần insert
     * Expected output:
     *  - Gọi getAutoIncrement trả về ID1 > 0
     *  - Insert sản phẩm với ID1 thành công
     *  - Gọi lại getAutoIncrement trả về ID2 > ID1
     *  - Insert sản phẩm với ID2 thành công
     */
    @Test
    public void getAutoIncrement_HopLe() throws Exception {
        // Lần 1
        int id1 = sanPhamDAO.getAutoIncrement();
        System.out.println("id1: " + id1);
        assertTrue("AUTO_INCREMENT lần 1 phải > 0", id1 > 0);

        insertSanPhamBangSQL(id1, "SP auto 1");

        // Kiểm tra đã insert đúng
        SanPhamDTO sp1 = sanPhamDAO.selectById(String.valueOf(id1));
        assertNotNull("Phải tìm thấy sản phẩm 1", sp1);
        assertEquals("Tên sản phẩm 1 phải đúng", "SP auto 1", sp1.getTensp());

        // Lần 2
        int id2 = sanPhamDAO.getAutoIncrement();
        System.out.println("id2: " + id2);
        assertTrue("AUTO_INCREMENT lần 2 phải > id1", id2 > id1);

        insertSanPhamBangSQL(id2, "SP auto 2");

        // Kiểm tra đã insert đúng
        SanPhamDTO sp2 = sanPhamDAO.selectById(String.valueOf(id2));
        assertNotNull("Phải tìm thấy sản phẩm 2", sp2);
        assertEquals("Tên sản phẩm 2 phải đúng", "SP auto 2", sp2.getTensp());
    }

    private void insertSanPhamBangSQL(int id, String tensp) throws SQLException {
        String sql = "INSERT INTO sanpham (masp, tensp, hinhanh, xuatxu, chipxuly, dungluongpin, kichthuocman, hedieuhanh, phienbanhdh, camerasau, cameratruoc, thoigianbaohanh, thuonghieu, khuvuckho, soluongton, trangthai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, tensp);
        stmt.setString(3, "img.png");
        stmt.setInt(4, 1);
        stmt.setString(5, "Chip");
        stmt.setInt(6, 4000);
        stmt.setDouble(7, 6.1);
        stmt.setInt(8, 1);
        stmt.setInt(9, 1);
        stmt.setString(10, "12MP");
        stmt.setString(11, "8MP");
        stmt.setInt(12, 12);
        stmt.setInt(13, 1);
        stmt.setInt(14, 1);
        stmt.setInt(15, 10);
        stmt.setInt(16, 1); // trangthai

        int inserted = stmt.executeUpdate();
        assertEquals("Insert SQL phải thành công", 1, inserted);

        stmt.close();
    }

    // === UPDATE SO LUONG TON TESTS ===


    /**
     * Mã testcase: SP_43
     * Mục tiêu: Test tăng số lượng tồn thành công với số lượng dương
     * Input: masp = 1 (tồn tại), soluong = 5
     * Expected output: update trả về 1, và selectById trả về soluongton mới = cũ + 5
     */
    @Test
    public void updateSoLuongTon_TangThanhCong() throws Exception {
        int masp = 1;
        SanPhamDTO before = sanPhamDAO.selectById(String.valueOf(masp));
        int result = sanPhamDAO.updateSoLuongTon(masp, 5);
        assertEquals(1, result);
        SanPhamDTO after = sanPhamDAO.selectById(String.valueOf(masp));
        assertEquals(before.getSoluongton() + 5, after.getSoluongton());
    }

    /**
     * Mã testcase: SP_44
     * Mục tiêu: Test giảm số lượng tồn thành công với số lượng âm
     * Input: masp = 1 (tồn tại), soluong = -3
     * Expected output: update trả về 1, và selectById trả về soluongton mới = cũ - 3
     */
    @Test
    public void updateSoLuongTon_GiamThanhCong() throws Exception {
        int masp = 1;
        SanPhamDTO before = sanPhamDAO.selectById(String.valueOf(masp));
        int result = sanPhamDAO.updateSoLuongTon(masp, -3);
        assertEquals(1, result);
        SanPhamDTO after = sanPhamDAO.selectById(String.valueOf(masp));
        assertEquals(before.getSoluongton() - 3, after.getSoluongton());
    }

    /**
     * Mã testcase: SP_45
     * Mục tiêu: Test giảm số lượng vượt quá tồn (cho phép âm)
     * Input: masp = 1 (tồn tại), soluong = -(before.getSoluongton() + 10)
     * Expected output: update trả về 1, và selectById trả về soluongton mới âm
     */
    @Test
    public void updateSoLuongTon_VuotQuaTon() throws Exception {
        int masp = 1;
        SanPhamDTO before = sanPhamDAO.selectById(String.valueOf(masp));
        int decrease = -(before.getSoluongton() + 10);
        int result = sanPhamDAO.updateSoLuongTon(masp, decrease);
        assertEquals(1, result);
        SanPhamDTO after = sanPhamDAO.selectById(String.valueOf(masp));
        assertEquals(before.getSoluongton() + decrease, after.getSoluongton());
    }

    /**
     * Mã testcase: SP_46
     * Mục tiêu: Test update thất bại với masp không tồn tại
     * Input: masp = 9999, soluong = 5
     * Expected output: update trả về 0, không ném exception
     */
    @Test
    public void updateSoLuongTon_MaspKhongTonTai() throws Exception {
        int masp = 9999;
        int result = sanPhamDAO.updateSoLuongTon(masp, 5);
        assertEquals(0, result);
    }

    /**
     * Mã testcase: SP_47
     * Mục tiêu: Test update thất bại với masp âm
     * Input: masp = -1, soluong = 5
     * Expected output: update trả về 0, không ném exception
     */
    @Test
    public void updateSoLuongTon_MaspAm() throws Exception {
        int masp = -1;
        int result = sanPhamDAO.updateSoLuongTon(masp, 5);
        assertEquals(0, result);
    }


}
