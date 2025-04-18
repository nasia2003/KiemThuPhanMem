package DAO;

import DTO.ThuocTinhSanPham.MauSacDTO;
import static org.junit.Assert.*;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

public class MauSacDAOTest {

    private static Connection connection;
    private static MauSacDAO mauSacDAO;
    private static final String TENMAU_TEST = "Màu test";
    private static int mamauTest;

    @BeforeClass
    public static void setUpClass() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        mauSacDAO = MauSacDAO.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        connection.setAutoCommit(false);

        String sql = "INSERT INTO mausac (tenmau, trangthai) VALUES (?, 1)";
        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, TENMAU_TEST);
        pst.executeUpdate();

        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            mamauTest = rs.getInt(1);
        }

        rs.close();
        pst.close();
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * TC01 - Insert thành công
     * Mục tiêu: Thêm mới màu sắc khi dữ liệu hợp lệ
     * Input: tenmau = "MauSac Test"
     * Expected Output: trả về 1, có bản ghi trong DB
     */
    @Test
    public void testInsertThanhCong() throws Exception {
        MauSacDTO dto = new MauSacDTO(0, "MauSac Test");
        int result = mauSacDAO.insert(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM mausac WHERE tenmau = ?");
        pst.setString(1, "MauSac Test");
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("MauSac Test", rs.getString("tenmau"));
        rs.close();
        pst.close();
    }

    /**
     * TC02 - Insert null
     * Mục tiêu: Không cho phép insert khi DTO là null
     * Input: dto = null
     * Expected Output: ném ra exception
     */
    @Test(expected = Exception.class)
    public void testInsertNull() throws Exception {
        mauSacDAO.insert(null);
    }

    /**
     * TC03 - Insert tên trùng
     * Mục tiêu: Không cho phép thêm màu trùng tên nếu hệ thống không cho phép
     * Input: tenmau = TENMAU_TEST
     * Expected Output: tùy logic xử lý trùng, test này nên trả về exception hoặc result = 0
     */
    @Test
    public void testInsertTrungTen() throws Exception {
        MauSacDTO dto = new MauSacDTO(0, TENMAU_TEST);
        try {
            int result = mauSacDAO.insert(dto);
            assertTrue("Không được chấp nhận tên trùng nếu không cho phép", result == 0 || result == -1);
        } catch (Exception e) {
            // Nếu hệ thống throw thì pass
            assertTrue(true);
        }
    }

    /**
     * TC04 - Update thành công
     * Mục tiêu: Cập nhật tên màu thành công theo mã màu
     * Input: mamau = mamauTest, tenmau = "Blue"
     * Expected Output: trả về 1, DB lưu tênmàu mới
     */
    @Test
    public void testUpdateThanhCong() throws Exception {
        MauSacDTO dto = new MauSacDTO(mamauTest, "Blue");
        int result = mauSacDAO.update(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM mausac WHERE mamau = ?");
        pst.setInt(1, mamauTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("Blue", rs.getString("tenmau"));
        rs.close();
        pst.close();
    }

    /**
     * TC05 - Update với mã không tồn tại
     * Mục tiêu: Không cập nhật khi mã màu không tồn tại
     * Input: mamau = 9999, tenmau = "Green"
     * Expected Output: trả về 0
     */
    @Test
    public void testUpdateIdNotFound() throws Exception {
        MauSacDTO dto = new MauSacDTO(9999, "Green");
        int result = mauSacDAO.update(dto);
        assertEquals(0, result);
    }

    /**
     * TC06 - Update null
     * Mục tiêu: Không cho phép cập nhật khi DTO là null
     * Input: dto = null
     * Expected Output: ném ra exception
     */
    @Test(expected = Exception.class)
    public void testUpdateNull() throws Exception {
        mauSacDAO.update(null);
    }

    /**
     * TC07 - Delete thành công
     * Mục tiêu: Xóa mềm màu sắc (chuyển trạng thái về 0)
     * Input: mamau = mamauTest
     * Expected Output: trả về 1, trạng thái = 0 trong DB
     */
    @Test
    public void testDeleteThanhCong() throws Exception {
        int result = mauSacDAO.delete(String.valueOf(mamauTest));
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM mausac WHERE mamau = ?");
        pst.setInt(1, mamauTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("trangthai"));
        rs.close();
        pst.close();
    }

    /**
     * TC08 - Delete với mã không tồn tại
     * Mục tiêu: Không thực hiện xóa nếu mã không tồn tại
     * Input: mamau = 9999
     * Expected Output: trả về 0
     */
    @Test
    public void testDeleteIdNotFound() throws Exception {
        int result = mauSacDAO.delete("9999");
        assertEquals(0, result);
    }

    /**
     * TC09 - SelectById thành công
     * Mục tiêu: Trả về đúng bản ghi khi mã màu tồn tại
     * Input: mamau = 2
     * Expected Output: DTO có mamau = 2
     */
    @Test
    public void testSelectByIdThanhCong() throws Exception {
        MauSacDTO result = mauSacDAO.selectById("2");
        assertNotNull(result);
        assertEquals(2, result.getMamau());
    }

    /**
     * TC10 - SelectById với id không tồn tại
     * Mục tiêu: Không trả về kết quả khi mã màu không tồn tại
     * Input: mamau = 9999
     * Expected Output: null
     */
    @Test
    public void testSelectByIdKhongTonTai() {
        MauSacDTO result = mauSacDAO.selectById("9999");
        assertNull(result);
    }

    /**
     * TC11 - SelectAll thành công
     * Mục tiêu: Lấy danh sách màu có trạng thái = 1
     * Input: none
     * Expected Output: danh sách > 0 phần tử
     */
    @Test
    public void testSelectAllThanhCong() throws Exception {
        ArrayList<MauSacDTO> list = mauSacDAO.selectAll();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC12 - GetAll thành công
     * Mục tiêu: Lấy toàn bộ danh sách màu (kể cả đã bị ẩn)
     * Input: none
     * Expected Output: danh sách > 0 phần tử
     */
    @Test
    public void testGetAllThanhCong() throws Exception {
        ArrayList<MauSacDTO> list = mauSacDAO.getAll();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC13 - GetAutoIncrement
     * Mục tiêu: Lấy đúng giá trị AUTO_INCREMENT từ bảng mausac
     * Input: none
     * Expected Output: trùng khớp với giá trị từ INFORMATION_SCHEMA
     */
    @Test
    public void testGetAutoIncrement() throws Exception {
        int value = mauSacDAO.getAutoIncrement();

        PreparedStatement pst = connection.prepareStatement(
                "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'mausac'");
        ResultSet rs = pst.executeQuery();
        rs.next();
        int dbValue = rs.getInt("AUTO_INCREMENT");

        assertEquals(dbValue, value);
        rs.close();
        pst.close();
    }
}
