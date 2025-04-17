package DAO;

import DTO.ThuocTinhSanPham.DungLuongRomDTO;
import static org.junit.Assert.*;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

public class DungLuongRomDAOTest {

    private static Connection connection;
    private static DungLuongRomDAO dungLuongRomDAO;
    private static final int DUNGLUONG_TEST = 99;
    private static int madlTest;

    @BeforeClass
    public static void setUpClass() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        dungLuongRomDAO = DungLuongRomDAO.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        connection.setAutoCommit(false);

        String sql = "INSERT INTO dungluongrom (kichthuocrom, trangthai) VALUES (?, 1)";
        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setInt(1, DUNGLUONG_TEST);
        pst.executeUpdate();

        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            madlTest = rs.getInt(1);
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
     * Mục tiêu: Thêm mới dung lượng ROM khi dữ liệu hợp lệ
     * Input: dungluongrom = 256
     * Expected Output: trả về 1, có bản ghi trong DB
     */
    @Test
    public void testInsertThanhCong() throws Exception {
        DungLuongRomDTO dto = new DungLuongRomDTO(0, 256);
        int result = dungLuongRomDAO.insert(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM dungluongrom WHERE kichthuocrom = ?");
        pst.setInt(1, 256);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(256, rs.getInt("kichthuocrom"));
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
        dungLuongRomDAO.insert(null);
    }

    /**
     * TC03 - Insert dung lượng trùng
     * Mục tiêu: Không cho phép thêm dung lượng trùng nếu hệ thống không cho phép
     * Input: dungluongrom = DUNGLUONG_TEST
     * Expected Output: tùy logic xử lý trùng, test này nên trả về exception hoặc result = 0
     */
    @Test
    public void testInsertTrungDungLuong() throws Exception {
        DungLuongRomDTO dto = new DungLuongRomDTO(0, DUNGLUONG_TEST);
        try {
            int result = dungLuongRomDAO.insert(dto);
            assertEquals(0, result);
        } catch (Exception e) {
            // Nếu hệ thống throw thì pass
            assertTrue(true);
        }
    }

    /**
     * TC04 - Update thành công
     * Mục tiêu: Cập nhật dung lượng ROM thành công theo mã
     * Input: madlrom = madlTest, dungluongrom = 512
     * Expected Output: trả về 1, DB lưu dung lượng mới
     */
    @Test
    public void testUpdateThanhCong() throws Exception {
        DungLuongRomDTO dto = new DungLuongRomDTO(madlTest, 999);
        int result = dungLuongRomDAO.update(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM dungluongrom WHERE madlrom = ?");
        pst.setInt(1, madlTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(999, rs.getInt("kichthuocrom"));
        rs.close();
        pst.close();
    }

    /**
     * TC05 - Update với mã không tồn tại
     * Mục tiêu: Không cập nhật khi mã không tồn tại
     * Input: madlrom = 9999, dungluongrom = 1024
     * Expected Output: trả về 0
     */
    @Test
    public void testUpdateIdNotFound() throws Exception {
        DungLuongRomDTO dto = new DungLuongRomDTO(9999, 1024);
        int result = dungLuongRomDAO.update(dto);
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
        dungLuongRomDAO.update(null);
    }

    /**
     * TC07 - Delete thành công
     * Mục tiêu: Xóa mềm dung lượng ROM (chuyển trạng thái về 0)
     * Input: madlrom = madlTest
     * Expected Output: trả về 1, trạng thái = 0 trong DB
     */
    @Test
    public void testDeleteThanhCong() throws Exception {
        int result = dungLuongRomDAO.delete(String.valueOf(madlTest));
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM dungluongrom WHERE madlrom = ?");
        pst.setInt(1, madlTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("trangthai"));
        rs.close();
        pst.close();
    }

    /**
     * TC08 - Delete với mã không tồn tại
     * Mục tiêu: Không thực hiện xóa nếu mã không tồn tại
     * Input: madlrom = 9999
     * Expected Output: trả về 0
     */
    @Test
    public void testDeleteIdNotFound() throws Exception {
        int result = dungLuongRomDAO.delete("9999");
        assertEquals(0, result);
    }

    /**
     * TC09 - SelectById thành công
     * Mục tiêu: Trả về đúng bản ghi khi mã tồn tại
     * Input: madlrom = madlTest
     * Expected Output: DTO có madlrom = madlTest
     */
    @Test
    public void testSelectByIdThanhCong() throws Exception {
        DungLuongRomDTO result = dungLuongRomDAO.selectById(String.valueOf(madlTest));
        assertNotNull(result);
        assertEquals(madlTest, result.getMadungluongrom());
        assertEquals(DUNGLUONG_TEST, result.getDungluongrom());
    }

    /**
     * TC10 - SelectById với id không tồn tại
     * Mục tiêu: Không trả về kết quả khi mã không tồn tại
     * Input: madlrom = 9999
     * Expected Output: null
     */
    @Test
    public void testSelectByIdKhongTonTai() {
        DungLuongRomDTO result = dungLuongRomDAO.selectById("9999");
        assertNull(result);
    }

    /**
     * TC11 - SelectAll thành công
     * Mục tiêu: Lấy danh sách dung lượng ROM có trạng thái = 1
     * Input: none
     * Expected Output: danh sách > 0 phần tử
     */
    @Test
    public void testSelectAllThanhCong() throws Exception {
        ArrayList<DungLuongRomDTO> list = dungLuongRomDAO.selectAll();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    /**
     * TC12 - GetAutoIncrement
     * Mục tiêu: Lấy đúng giá trị AUTO_INCREMENT từ bảng dungluongrom
     * Input: none
     * Expected Output: trùng khớp với giá trị từ INFORMATION_SCHEMA
     */
    @Test
    public void testGetAutoIncrement() throws Exception {
        int value = dungLuongRomDAO.getAutoIncrement();

        PreparedStatement pst = connection.prepareStatement(
                "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'dungluongrom'");
        ResultSet rs = pst.executeQuery();
        rs.next();
        int dbValue = rs.getInt("AUTO_INCREMENT");

        assertEquals(dbValue, value);
        rs.close();
        pst.close();
    }
}