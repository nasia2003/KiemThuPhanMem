package DAO;

import DTO.ThuocTinhSanPham.XuatXuDTO;
import static org.junit.Assert.*;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Unit Test cho lớp XuatXuDAO
 */
public class XuatXuDAOTest {

    private static Connection connection;
    private static XuatXuDAO xuatXuDAO;
    private static final String TEN_TEST = "XX_Test";
    private static int maXuatXuTest;

    @BeforeClass
    public static void setUpClass() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        xuatXuDAO = XuatXuDAO.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        connection.setAutoCommit(false);

        String sql = "INSERT INTO xuatxu (tenxuatxu, trangthai) VALUES (?, 1)";
        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, TEN_TEST);
        pst.executeUpdate();

        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            maXuatXuTest = rs.getInt(1);
        }
        rs.close();
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
     * Mục tiêu: Insert xuất xứ mới vào CSDL
     * Input: maxuatxu = bất kỳ (ví dụ 0), tenxuatxu = "XX Insert"
     * Expected output: insert trả về 1, dữ liệu tồn tại trong DB
     */
    @Test
    public void testInsertThanhCong() throws Exception {
        XuatXuDTO dto = new XuatXuDTO(0, "XX Insert");
        int result = xuatXuDAO.insert(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM xuatxu WHERE tenxuatxu = ?");
        pst.setString(1, "XX Insert");
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("XX Insert", rs.getString("tenxuatxu"));
        rs.close();
        pst.close();
    }

    /**
     * TC02 - Insert trùng ID
     * Mục tiêu: Không insert khi ID đã tồn tại
     * Input: maxuatxu = maXuatXuTest (ID đã có), tenxuatxu = "XX Trùng"
     * Expected output: insert trả về 0
     */
    @Test
    public void testInsertTrungID() throws Exception {
        XuatXuDTO dto = new XuatXuDTO(maXuatXuTest, "XX Trùng");
        int result = xuatXuDAO.insert(dto);
        assertEquals(0, result);
    }

    /**
     * TC03 - Update thành công
     * Mục tiêu: Cập nhật tên xuất xứ theo ID đã có
     * Input: maxuatxu = maXuatXuTest, tenxuatxu = "XX Updated"
     * Expected output: update trả về 1, tên được cập nhật
     */
    @Test
    public void testUpdateThanhCong() throws Exception {
        XuatXuDTO dto = new XuatXuDTO(maXuatXuTest, "XX Updated");
        int result = xuatXuDAO.update(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM xuatxu WHERE maxuatxu = ?");
        pst.setInt(1, maXuatXuTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("XX Updated", rs.getString("tenxuatxu"));
        rs.close();
        pst.close();
    }

    /**
     * TC04 - Update không tồn tại
     * Mục tiêu: Không cập nhật khi ID không tồn tại
     * Input: maxuatxu = 999999, tenxuatxu = "XXX"
     * Expected output: update trả về 0
     */
    @Test
    public void testUpdateKhongTonTai() throws Exception {
        XuatXuDTO dto = new XuatXuDTO(999999, "XXX");
        int result = xuatXuDAO.update(dto);
        assertEquals(0, result);
    }

    /**
     * TC05 - Delete thành công
     * Mục tiêu: Xóa mềm xuất xứ (đổi trạng thái = 0)
     * Input: maxuatxu = maXuatXuTest
     * Expected output: delete trả về 1, trạng thái = 0
     */
    @Test
    public void testDeleteThanhCong() throws Exception {
        int result = xuatXuDAO.delete(String.valueOf(maXuatXuTest));
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT trangthai FROM xuatxu WHERE maxuatxu = ?");
        pst.setInt(1, maXuatXuTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("trangthai"));
        rs.close();
        pst.close();
    }

    /**
     * TC06 - Delete không tồn tại
     * Mục tiêu: Không xóa khi ID không tồn tại
     * Input: maxuatxu = 999999
     * Expected output: delete trả về 0
     */
    @Test
    public void testDeleteKhongTonTai() throws Exception {
        int result = xuatXuDAO.delete("999999");
        assertEquals(0, result);
    }

    /**
     * TC07 - SelectAll
     * Mục tiêu: Lấy danh sách tất cả xuất xứ đang hoạt động
     * Input: Không có
     * Expected output: Trả về list chứa phần tử có maxuatxu = maXuatXuTest
     */
    @Test
    public void testSelectAll() throws Exception {
        ArrayList<XuatXuDTO> ds = xuatXuDAO.selectAll();
        assertNotNull(ds);

        boolean found = false;
        for (XuatXuDTO xx : ds) {
            if (xx.getMaxuatxu() == maXuatXuTest) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * TC08 - SelectById thành công
     * Mục tiêu: Lấy thông tin xuất xứ theo ID
     * Input: maxuatxu = maXuatXuTest
     * Expected output: DTO trả về != null và đúng ID
     */
    @Test
    public void testSelectByIdThanhCong() throws Exception {
        XuatXuDTO dto = xuatXuDAO.selectById(String.valueOf(maXuatXuTest));
        assertNotNull(dto);
        assertEquals(maXuatXuTest, dto.getMaxuatxu());
    }

    /**
     * TC09 - SelectById không tồn tại
     * Mục tiêu: Không trả về kết quả khi ID không tồn tại
     * Input: maxuatxu = 999999
     * Expected output: Trả về null
     */
    @Test
    public void testSelectByIdKhongTonTai() throws Exception {
        XuatXuDTO dto = xuatXuDAO.selectById("999999");
        assertNull(dto);
    }

    /**
     * TC10 - GetAutoIncrement
     * Mục tiêu: Lấy giá trị auto increment tiếp theo
     * Input: Không có
     * Expected output: Trả về giá trị bằng với AUTO_INCREMENT trong DB
     */
    @Test
    public void testGetAutoIncrement() throws Exception {
        int value = xuatXuDAO.getAutoIncrement();

        PreparedStatement pst = connection.prepareStatement(
                "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'xuatxu'"
        );
        ResultSet rs = pst.executeQuery();
        rs.next();
        int dbValue = rs.getInt("AUTO_INCREMENT");

        assertEquals(dbValue, value);
        rs.close();
        pst.close();
    }

    /**
     * TC11 - Exception khi selectById truyền null
     * Mục tiêu: Đảm bảo không ném exception khi truyền null
     * Input: null
     * Expected output: Trả về null (xử lý an toàn)
     */
    @Test
    public void testSelectByIdNull() {
        XuatXuDTO dto = xuatXuDAO.selectById(null);
        assertNull(dto);
    }
}
