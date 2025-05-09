package DAO;

import DTO.KhuVucKhoDTO;
import static org.junit.Assert.*;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Unit Test cho lớp KhuVucKhoDAO
 */
public class KhuVucKhoDAOTest {

    private static Connection connection;
    private static KhuVucKhoDAO khuVucKhoDAO;
    private static final String TEN_TEST = "KVK_Test";
    private static final String GHI_CHU_TEST = "GhiChu_Test";
    private static int maKhuVucTest;

    @BeforeClass
    public static void setUpClass() throws Exception {
        connection = ConnectionCustom.getInstance().getConnect();
        khuVucKhoDAO = KhuVucKhoDAO.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        connection.setAutoCommit(false);

        // Insert dữ liệu test
        String sql = "INSERT INTO khuvuckho (tenkhuvuc, ghichu, trangthai) VALUES (?, ?, 1)";
        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, TEN_TEST);
        pst.setString(2, GHI_CHU_TEST);
        pst.executeUpdate();

        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            maKhuVucTest = rs.getInt(1);
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
     * Mục tiêu: Insert khu vực kho mới vào CSDL
     * Input: tenkhuvuc = "KVK Insert", ghichu = "Insert Test"
     * Expected output: insert trả về 1, dữ liệu tồn tại trong DB
     */
    @Test
    public void testInsertThanhCong() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(0, "KVK Insert", "Insert Test");
        int result = khuVucKhoDAO.insert(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM khuvuckho WHERE tenkhuvuc = ?");
        pst.setString(1, "KVK Insert");
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("Insert Test", rs.getString("ghichu"));
        rs.close();
        pst.close();
    }

    /**
     * TC02 - Insert trùng ID
     * Mục tiêu: Không insert khi ID đã tồn tại
     * Input: makhuvuc = maKhuVucTest (ID đã có), tenkhuvuc = "KVK Trùng"
     * Expected output: insert trả về 0
     */
    @Test
    public void testInsertTrungID() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(maKhuVucTest, "KVK Trùng", "Note");
        int result = khuVucKhoDAO.insert(dto);
        assertEquals(0, result);
    }

    /**
     * TC03 - Update thành công
     * Mục tiêu: Cập nhật dữ liệu khu vực đã có
     * Input: makhuvuc = maKhuVucTest, tenkhuvuc = "KVK Updated", ghichu = "Note Updated"
     * Expected output: update trả về 1, dữ liệu trong DB được cập nhật
     */
    @Test
    public void testUpdateThanhCong() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(maKhuVucTest, "KVK Updated", "Note Updated");
        int result = khuVucKhoDAO.update(dto);
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM khuvuckho WHERE makhuvuc = ?");
        pst.setInt(1, maKhuVucTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals("KVK Updated", rs.getString("tenkhuvuc"));
        rs.close();
        pst.close();
    }

    /**
     * TC04 - Update không tồn tại
     * Mục tiêu: Không cập nhật khi ID không tồn tại
     * Input: makhuvuc = 999999, tenkhuvuc = "XXX"
     * Expected output: update trả về 0
     */
    @Test
    public void testUpdateKhongTonTai() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(999999, "XXX", "YYY");
        int result = khuVucKhoDAO.update(dto);
        assertEquals(0, result);
    }

    /**
     * TC05 - Delete thành công
     * Mục tiêu: Xóa mềm khu vực kho (đổi trạng thái = 0)
     * Input: makhuvuc = maKhuVucTest
     * Expected output: delete trả về 1, trạng thái = 0
     */
    @Test
    public void testDeleteThanhCong() throws Exception {
        int result = khuVucKhoDAO.delete(String.valueOf(maKhuVucTest));
        assertEquals(1, result);

        PreparedStatement pst = connection.prepareStatement("SELECT trangthai FROM khuvuckho WHERE makhuvuc = ?");
        pst.setInt(1, maKhuVucTest);
        ResultSet rs = pst.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("trangthai"));
        rs.close();
        pst.close();
    }

    /**
     * TC06 - Delete không tồn tại
     * Mục tiêu: Không xóa khi ID không có trong DB
     * Input: makhuvuc = 999999
     * Expected output: delete trả về 0
     */
    @Test
    public void testDeleteKhongTonTai() throws Exception {
        int result = khuVucKhoDAO.delete("999999");
        assertEquals(0, result);
    }

    /**
     * TC07 - SelectAll
     * Mục tiêu: Lấy danh sách tất cả khu vực đang hoạt động
     * Input: Không có
     * Expected output: Trả về list chứa phần tử có makhuvuc = maKhuVucTest
     */
    @Test
    public void testSelectAll() throws Exception {
        ArrayList<KhuVucKhoDTO> ds = khuVucKhoDAO.selectAll();
        assertNotNull(ds);

        boolean found = false;
        for (KhuVucKhoDTO kvk : ds) {
            if (kvk.getMakhuvuc() == maKhuVucTest) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * TC08 - SelectById thành công
     * Mục tiêu: Lấy thông tin khu vực theo ID
     * Input: makhuvuc = maKhuVucTest
     * Expected output: DTO trả về != null và đúng ID
     */
    @Test
    public void testSelectByIdThanhCong() throws Exception {
        KhuVucKhoDTO dto = khuVucKhoDAO.selectById(String.valueOf(maKhuVucTest));
        assertNotNull(dto);
        assertEquals(maKhuVucTest, dto.getMakhuvuc());
    }

    /**
     * TC09 - SelectById không tồn tại
     * Mục tiêu: Không trả về kết quả khi ID không có
     * Input: makhuvuc = 999999
     * Expected output: Trả về null
     */
    @Test
    public void testSelectByIdKhongTonTai() throws Exception {
        KhuVucKhoDTO dto = khuVucKhoDAO.selectById("999999");
        assertNull(dto);
    }

    /**
     * TC10 - SelectById null
     * Mục tiêu: Không trả về kết quả khi ID null
     * Input: makhuvuc = null
     * Expected output: Trả về null
     */
    @Test
    public void testSelectByIdNull() throws Exception {
        KhuVucKhoDTO dto = khuVucKhoDAO.selectById(null);
        assertNull(dto);
    }

    /**
     * TC11 - GetAutoIncrement
     * Mục tiêu: Lấy giá trị auto increment tiếp theo
     * Input: Không có
     * Expected output: Trả về giá trị bằng với AUTO_INCREMENT trong DB
     */
    @Test
    public void testGetAutoIncrement() throws Exception {
        int value = khuVucKhoDAO.getAutoIncrement();

        PreparedStatement pst = connection.prepareStatement(
                "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'khuvuckho'"
        );
        ResultSet rs = pst.executeQuery();
        rs.next();
        int dbValue = rs.getInt("AUTO_INCREMENT");

        assertEquals(dbValue, value);
        rs.close();
        pst.close();
    }
}
