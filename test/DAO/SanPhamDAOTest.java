package DAO;

import DTO.SanPhamDTO;
import static org.junit.Assert.*;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Unit Test cho lớp SanPhamDAO
 */
public class SanPhamDAOTest {

    private static Connection connection;
    private static SanPhamDAO sanPhamDAO;
    private static int maspTest;

    @BeforeClass
    public static void setUpClass() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        sanPhamDAO = SanPhamDAO.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        connection.setAutoCommit(false);

        String sql = "INSERT INTO sanpham (masp, tensp, hinhanh, xuatxu, chipxuly, dungluongpin, kichthuocman, hedieuhanh, phienbanhdh, camerasau, cameratruoc, thoigianbaohanh, thuonghieu, khuvuckho, soluongton, trangthai) " +
                "VALUES (?, 'SP Test', 'img.png', 1, 'Chip', 5000, 6.5, 1, 1, '12MP', '8MP', 12, 1, 1, 10, 1)";
        PreparedStatement pst = connection.prepareStatement(sql);
        maspTest = sanPhamDAO.getAutoIncrement();
        pst.setInt(1, maspTest);
        pst.executeUpdate();
        pst.close();
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();
    }

    @AfterClass
    public static void globalTearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * TC01 - Insert thành công
     * Mục tiêu: Kiểm tra insert sản phẩm thành công với dữ liệu hợp lệ
     * Input: SanPhamDTO chứa đầy đủ thông tin
     * Expected output: Trả về 1 (thành công)
     */
    @Test
    public void testInsertThanhCong() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(0, "SP Insert", "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, 15);
        int result = sanPhamDAO.insert(sp);
        assertEquals(1, result);
    }

    /**
     * TC02 - Insert thất bại
     * Mục tiêu: Kiểm tra insert thất bại khi thiếu dữ liệu
     * Input: SanPhamDTO thiếu tên sản phẩm
     * Expected output: Trả về 0 (không thành công)
     */
    @Test
    public void testInsertTenNull() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(0, null, "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.insert(sp);
        assertEquals(0, result);
    }

    /**
     * TC03 - Update thành công
     * Mục tiêu: Kiểm tra cập nhật sản phẩm thành công
     * Input: SanPhamDTO hợp lệ với masp đã tồn tại
     * Expected output: Trả về 1
     */
    @Test
    public void testUpdateThanhCong() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(maspTest, "SP Updated", "img.png", 1, "Chip", 5000, 6.5, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.update(sp);
        assertEquals(1, result);
    }

    /**
     * TC04 - Update không tồn tại
     * Mục tiêu: Kiểm tra cập nhật thất bại với masp không tồn tại
     * Input: SanPhamDTO với masp không tồn tại
     * Expected output: Trả về 0
     */
    @Test
    public void testUpdateKhongTonTai() throws Exception {
        SanPhamDTO sp = new SanPhamDTO(999999, "SP No Exist", "img.png", 1, "Chip", 4000, 6.1, 1, 1, "12MP", "8MP", 12, 1, 1, 10);
        int result = sanPhamDAO.update(sp);
        assertEquals(0, result);
    }

    /**
     * TC05 - Delete thành công
     * Mục tiêu: Kiểm tra xóa mềm sản phẩm thành công
     * Input: masp đã tồn tại
     * Expected output: Trả về 1
     */
    @Test
    public void testDeleteThanhCong() throws Exception {
        int result = sanPhamDAO.delete(String.valueOf(maspTest));
        assertEquals(1, result);
    }

    /**
     * TC06 - Delete không tồn tại
     * Mục tiêu: Kiểm tra xóa mềm thất bại khi masp không tồn tại
     * Input: masp không tồn tại
     * Expected output: Trả về 0
     */
    @Test
    public void testDeleteKhongTonTai() throws Exception {
        int result = sanPhamDAO.delete("999999");
        assertEquals(0, result);
    }

    /**
     * TC07 - SelectAll
     * Mục tiêu: Kiểm tra lấy danh sách tất cả sản phẩm đang hoạt động
     * Input: Không có
     * Expected output: Danh sách chứa sản phẩm test
     */
    @Test
    public void testSelectAll() throws Exception {
        ArrayList<SanPhamDTO> list = sanPhamDAO.selectAll();
        assertNotNull(list);
        boolean found = list.stream().anyMatch(sp -> sp.getMasp() == maspTest);
        assertTrue(found);
    }

    /**
     * TC08 - SelectById thành công
     * Mục tiêu: Kiểm tra lấy sản phẩm theo ID thành công
     * Input: masp đã tồn tại
     * Expected output: Trả về DTO != null và đúng masp
     */
    @Test
    public void testSelectByIdThanhCong() throws Exception {
        SanPhamDTO sp = sanPhamDAO.selectById(String.valueOf(maspTest));
        assertNotNull(sp);
        assertEquals(maspTest, sp.getMasp());
    }

    /**
     * TC09 - SelectById không tồn tại
     * Mục tiêu: Kiểm tra lấy sản phẩm với ID không tồn tại
     * Input: masp không tồn tại
     * Expected output: Trả về null
     */
    @Test
    public void testSelectByIdKhongTonTai() throws Exception {
        SanPhamDTO sp = sanPhamDAO.selectById("999999");
        assertNull(sp);
    }

    /**
     * TC10 - GetAutoIncrement
     * Mục tiêu: Lấy giá trị AUTO_INCREMENT tiếp theo của bảng sanpham
     * Input: Không có
     * Expected output: Giá trị đúng với INFORMATION_SCHEMA
     */
    @Test
    public void testGetAutoIncrement() throws Exception {
        int value = sanPhamDAO.getAutoIncrement();

        PreparedStatement pst = connection.prepareStatement(
                "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'sanpham'"
        );
        ResultSet rs = pst.executeQuery();
        rs.next();
        int dbValue = rs.getInt("AUTO_INCREMENT");

        assertEquals(dbValue, value);
        rs.close();
        pst.close();
    }

    /**
     * TC11 - Update số lượng tồn thành công
     * Mục tiêu: Kiểm tra update số lượng tồn thành công
     * Input: masp tồn tại, số lượng cộng thêm
     * Expected output: Trả về 1
     */
    @Test
    public void testUpdateSoLuongTonThanhCong() throws Exception {
        int result = sanPhamDAO.updateSoLuongTon(maspTest, 5);
        assertEquals(1, result);
    }

    /**
     * TC12 - SelectByPhienBan không tồn tại
     * Mục tiêu: Kiểm tra lấy sản phẩm theo phiên bản không tồn tại
     * Input: Mã phiên bản không tồn tại
     * Expected output: Trả về null
     */
    @Test
    public void testSelectByPhienBanKhongTonTai() {
        SanPhamDTO sp = sanPhamDAO.selectByPhienBan("PB_NOT_EXIST");
        assertNull(sp);
    }
}
